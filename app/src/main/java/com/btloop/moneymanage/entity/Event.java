package com.btloop.moneymanage.entity;

/**
 * Created by thede on 4/17/2016.
 */
public class Event{
    int day;
    String eventName;
    int money;
    String start_time;
    String end_time;
    int month_id;
    int event_id;
    public Event() {
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public Event(int event_id,int day, String eventName, int money, String start_time, String end_time, int month_id) {
        this.day = day;
        this.event_id = event_id;
        this.eventName = eventName;
        this.money = money;
        this.start_time = start_time;
        this.end_time = end_time;
        this.month_id = month_id;
    }

    public int getMonth_id() {
        return month_id;
    }

    public void setMonth_id(int month_id) {
        this.month_id = month_id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public Event(String eventName, int money) {
        this.eventName = eventName;
        this.money = money;
    }
}
