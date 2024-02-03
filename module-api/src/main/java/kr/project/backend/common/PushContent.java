package kr.project.backend.common;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;

import java.util.List;

public class PushContent {

    public static Message makeMessage(String targetToken, String title, String body) {
        Notification notification = Notification
                .builder()
                .setTitle(title)
                .setBody(body)
                .build();
        return Message
                .builder()
                .setNotification(notification)
                .setToken(targetToken)
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
