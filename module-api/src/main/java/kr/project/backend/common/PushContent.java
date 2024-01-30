package kr.project.backend.common;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

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
}
