package kr.project.backend.common;

import com.google.firebase.messaging.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class PushContent {

    public static Message makeMessage(String targetToken, String title, String body, long alarmCnt) {
        Notification notification = Notification
                .builder()
                .setTitle(title)
                .setBody(body)
                .build();
        return Message
                .builder()
                .setNotification(notification)
                .setToken(targetToken)
                .setApnsConfig(ApnsConfig.builder()
                        .setAps(Aps.builder()
                                .setBadge((int) alarmCnt)
                                .build())
                        .build())
                .build();
    }

    public static MulticastMessage makeMessages(String title, String body, List<String> targetTokens) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();
        return MulticastMessage.builder()
                .setNotification(notification)
                .addAllTokens(targetTokens)
                .build();

    }

    public static List<Message> makeMessageSendAll(String title, String body, List<String> targetTokens, List<Integer> alarmCnts) {

        List<Message> messageList = new ArrayList<>();

        Notification notification = Notification
                .builder()
                .setTitle(title)
                .setBody(body)
                .build();

        IntStream.range(0, targetTokens.size())
                .forEach(index -> {
                    messageList.add(
                        Message.builder()
                                .setNotification(notification)
                                .setToken(targetTokens.get(index))
                                .setApnsConfig(ApnsConfig.builder()
                                        .setAps(Aps.builder()
                                                .setBadge(alarmCnts.get(index))
                                                .build())
                                        .build())
                                .build()
                    );
                });

        return messageList;
    }
}
