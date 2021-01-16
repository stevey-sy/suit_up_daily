package com.example.suitupdaily.styling;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimeConverter {
    public final static int _SEC = 60;
    public final static int _MIN = 60;
    public final static int _HOUR = 24;
    public final static int _DAY = 7;
    public final static int _MONTH = 12;

    public static String CreateDataWithCheck(String dataString) {
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
        java.util.Date date = null;

        try {
            date = format.parse(dataString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long curTime = System.currentTimeMillis();
        long regTime = date.getTime();
        long diffTime = (curTime - regTime) / 1000;

        String msg = null;
        if(diffTime < _SEC) {
            msg = "방금 전";
        } else if ((diffTime /= _SEC) < _MIN) {
            msg = diffTime + "분 전";
        } else if ((diffTime /= _MIN) < _HOUR) {
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= _HOUR) < _DAY) {
            msg = (diffTime) + "일 전";
        } else {
            SimpleDateFormat aformat = new SimpleDateFormat("yyyy-MM-dd");
            msg = aformat.format(date);
        }

        return msg;
    }

}
