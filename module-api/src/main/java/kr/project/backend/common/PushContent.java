package kr.project.backend.common;

import com.google.firebase.messaging.*;

import java.util.List;

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
}
