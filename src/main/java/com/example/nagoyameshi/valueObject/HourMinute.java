package com.example.nagoyameshi.valueObject;

import java.time.LocalTime;

public class HourMinute {
    private String hour;
    private String minute;

    public HourMinute(){

    }

    public HourMinute(String hour, String minute) {
        this.hour = hour;
        this.minute = minute;
    }

    // LocalTime を受け取るコンストラクタ
    public HourMinute(LocalTime localTime) {
        this.hour = String.format("%02d", localTime.getHour()); // 時を 2 桁で保存
        this.minute = String.format("%02d", localTime.getMinute()); // 分を 2 桁で保存
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    // LocalTimeへの変換
    public LocalTime toLocalTime() {
        return LocalTime.of(Integer.parseInt(hour), Integer.parseInt(minute));
    }

    @Override
    public String toString() {
        return String.format("%s:%s", hour, minute);
    }
}
