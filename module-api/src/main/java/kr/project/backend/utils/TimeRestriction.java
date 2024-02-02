package kr.project.backend.utils;


import java.time.LocalTime;

public class TimeRestriction {

    //21:00 ~ 8:00 로직 막는 메소드
    //True = 정책걸림
    //false = 정책안걸림
    public static boolean checkTimeRestriction(){
        LocalTime currentTime = LocalTime.now();
        LocalTime startTime = LocalTime.of(21, 0); // 21:00
        LocalTime endTime = LocalTime.of(7, 59, 59); // 07:59:59

        return currentTime.isAfter(startTime) || currentTime.isBefore(endTime);
        }
    }

