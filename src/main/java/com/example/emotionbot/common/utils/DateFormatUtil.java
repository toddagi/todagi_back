package com.example.emotionbot.common.utils;

import org.springframework.stereotype.Component;


@Component
public class DateFormatUtil {

    public int yearFormat(String date) {
        return Integer.parseInt(dateFormat(date)[0]);
    }

    public int monthFormat(String date) {
       return Integer.parseInt(dateFormat(date)[1]);
    }

    private String[] dateFormat(String date){
        return date.split("-");
    }

}
