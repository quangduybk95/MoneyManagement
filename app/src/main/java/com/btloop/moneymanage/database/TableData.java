package com.btloop.moneymanage.database;

import android.provider.BaseColumns;

/**
 * Created by thede on 4/24/2016.
 */
public class TableData {
        public TableData() {

        }

        public static abstract class TableInfo implements BaseColumns {
            public static String DATABASE_NAME = "money_management";
            public static String TABLE_NAME_EVENT = "event";
            public static String TABLE_NAME_MONTH = "month";
            public static String MONTH_ID = "month_id";
            public static String MONTH_MM = "month_mm";
            public static String MONTH_YY = "month_yy";
            public static String MONTH_MAXMONEY = "month_maxmoney";
            public static String MONTH_USED = "month_used";

            public static String EVENT_ID = "event_id";
            public static String EVENT_DAY = "event_day";
            public static String EVENT_NAME = "event_name";
            public static String EVENT_MONEY = "event_money";
            public static String EVENT_START_TIME = "event_start_time";
            public static String EVENT_END_TIME = "event_end_time";

        }
    }

