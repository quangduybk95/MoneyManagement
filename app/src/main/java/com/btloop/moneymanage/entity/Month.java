package com.btloop.moneymanage.entity;

/**
 * Created by thede on 5/10/2016.
 */
public class Month {
   private int month;
    private int year;
    private int max_money;
    private int used_money;
    private int month_id;

    public int getMonth_id() {
        return month_id;
    }

    public void setMonth_id(int month_id) {
        this.month_id = month_id;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMax_money() {
        return max_money;
    }

    public void setMax_money(int max_money) {
        this.max_money = max_money;
    }

    public int getUsed_money() {
        return used_money;
    }

    public void setUsed_money(int used_money) {
        this.used_money = used_money;
    }


    public Month(int month, int year, int max_money, int used_money, int month_id) {
        this.month = month;
        this.year = year;
        this.max_money = max_money;
        this.used_money = used_money;
        this.month_id = month_id;
    }

    public Month(int month, int year) {
        this.month = month;
        this.year = year;
        this.max_money = 0;
        this.used_money = 0;

    }

    public Month() {
    }
}
