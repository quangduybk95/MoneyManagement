package com.btloop.moneymanage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.btloop.moneymanage.entity.Event;
import com.btloop.moneymanage.entity.Month;

import java.util.ArrayList;

/**
 * Created by thede on 4/24/2016.
 */
public class DatabaseOperation extends SQLiteOpenHelper {
    public static final int database_version = 1;
    private Context context;

    public DatabaseOperation(Context context) {
        super(context, TableData.TableInfo.DATABASE_NAME, null, database_version);
        this.context = context;
    }

    public String CREATE_MONTH_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TableData.TableInfo.TABLE_NAME_MONTH + " ("
            + TableData.TableInfo.MONTH_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TableData.TableInfo.MONTH_MM + " INTEGER,"
            + TableData.TableInfo.MONTH_YY + " INTEGER,"
            + TableData.TableInfo.MONTH_MAXMONEY + " INTEGER,"
            + TableData.TableInfo.MONTH_USED + " INTEGER);";
    public String CREATE_EVENT_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TableData.TableInfo.TABLE_NAME_EVENT + " (" + TableData.TableInfo.EVENT_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TableData.TableInfo.EVENT_DAY + " INTEGER,"
            + TableData.TableInfo.EVENT_NAME + " TEXT,"
            + TableData.TableInfo.EVENT_MONEY + " INTEGER,"
            + TableData.TableInfo.EVENT_START_TIME + " TEXT,"
            + TableData.TableInfo.EVENT_END_TIME + " TEXT,"
            + TableData.TableInfo.MONTH_ID + " INTEGER);";

    @Override
    public void onCreate(SQLiteDatabase sdb) {
        sdb.execSQL(CREATE_MONTH_TABLE);
        sdb.execSQL(CREATE_EVENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // them 1 thang
    public void putMonth(DatabaseOperation dop, Month mm) {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableData.TableInfo.MONTH_MM, mm.getMonth());
        cv.put(TableData.TableInfo.MONTH_YY, mm.getYear());
        cv.put(TableData.TableInfo.MONTH_MAXMONEY, mm.getMax_money());
        cv.put(TableData.TableInfo.MONTH_USED, mm.getUsed_money());
        long k = SQ.insert(TableData.TableInfo.TABLE_NAME_MONTH, null, cv);
        Log.e("Put month OK", "" + mm.getMonth());

    }

    //check month exist
    public int checkMonth(DatabaseOperation dop, int month, int year) {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        String qe = "SElECT * FROM " + TableData.TableInfo.TABLE_NAME_MONTH + " WHERE "
                + TableData.TableInfo.MONTH_YY + " = " + year
                + " AND "
                + TableData.TableInfo.MONTH_MM + " = " + month + ";";
        Cursor CR = SQ.rawQuery(qe, null);
        if (CR.getCount() == 0) return 0;
        else return 1;
    }


    // them 1 event
    public void putEvent(DatabaseOperation dop, Event ev) {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableData.TableInfo.MONTH_ID, ev.getMonth_id());
        cv.put(TableData.TableInfo.EVENT_DAY, ev.getDay());
        cv.put(TableData.TableInfo.EVENT_NAME, ev.getEventName());
        cv.put(TableData.TableInfo.EVENT_MONEY, ev.getMoney());
        cv.put(TableData.TableInfo.EVENT_START_TIME, ev.getStart_time());
        cv.put(TableData.TableInfo.EVENT_END_TIME, ev.getEnd_time());
        long k = SQ.insert(TableData.TableInfo.TABLE_NAME_EVENT, null, cv);
        Log.e("Put Event OK", "" + ev.getDay());

    }

    //get monthid by month
    public int getMonthId(DatabaseOperation dop, int month, int year) {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        String qe = "select " + TableData.TableInfo.MONTH_ID + " FROM " + TableData.TableInfo.TABLE_NAME_MONTH + " WHERE "
                + TableData.TableInfo.MONTH_YY + "= " + year + " and " + TableData.TableInfo.MONTH_MM + " = " + month;
        Cursor CR = SQ.rawQuery(qe, null);
        CR.moveToFirst();
        int month_id = 0;
        if (CR.getCount() > 0) {
            month_id = CR.getInt(0);
        }
        return month_id;
    }

    //get all event by day
    public Cursor getEventByDay(DatabaseOperation dop, int day, int month_id) {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        String qe = "select * FROM " + TableData.TableInfo.TABLE_NAME_EVENT + " WHERE "
                + TableData.TableInfo.EVENT_DAY + " = " + day + " AND " + TableData.TableInfo.MONTH_ID + " = " + month_id;
        Cursor CR = SQ.rawQuery(qe, null);
        CR.moveToFirst();
        return CR;
    }

    //get max money by monthid
    public int getMaxMoney(DatabaseOperation dop, int month_id) {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        String qe = "select " + TableData.TableInfo.MONTH_MAXMONEY + " from " + TableData.TableInfo.TABLE_NAME_MONTH
                + " where " + TableData.TableInfo.MONTH_ID + " = " + month_id;
        Cursor CR = SQ.rawQuery(qe, null);
        CR.moveToFirst();
        int max = 0;
        if (CR.getCount() > 0)
            max = CR.getInt(0);
        return max;
    }

    //update money
    public void updateMoneyInMonth(DatabaseOperation dop, int month_id, int new_money) {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableData.TableInfo.MONTH_MAXMONEY, new_money);
        SQ.update(TableData.TableInfo.TABLE_NAME_MONTH, cv, TableData.TableInfo.MONTH_ID + "=" + month_id, null);
    }

    //get all money used in month
    public int getAllMoneyUsed(DatabaseOperation dop, int month_id) {
        int sum = 0;
        SQLiteDatabase SQ = dop.getWritableDatabase();
        String qe = "select  " + TableData.TableInfo.EVENT_MONEY + " FROM " + TableData.TableInfo.TABLE_NAME_EVENT + " WHERE "
                + TableData.TableInfo.MONTH_ID + " = " + month_id;
        Cursor CR = SQ.rawQuery(qe, null);
        CR.moveToFirst();
        if (CR.getCount() > 0) {
            do {
                Log.e("USed", CR.getInt(0) + "");
                sum = sum + CR.getInt(0);
            }
            while (CR.moveToNext());
        }
        Log.e("SUM USED", "" + sum);
        return sum;
    }

    public void deleteEvent(DatabaseOperation dop, int event_id) {
    SQLiteDatabase SQ = dop.getWritableDatabase();
        SQ.delete(TableData.TableInfo.TABLE_NAME_EVENT,TableData.TableInfo.EVENT_ID +" = "+
                event_id,null);
    }
    public void updateEvent(DatabaseOperation dop,Event event,int event_id)
    {
        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableData.TableInfo.EVENT_START_TIME,event.getStart_time());
        cv.put(TableData.TableInfo.EVENT_END_TIME,event.getEnd_time());
        cv.put(TableData.TableInfo.EVENT_MONEY,event.getMoney());
        cv.put(TableData.TableInfo.EVENT_NAME,event.getEventName());
        SQ.update(TableData.TableInfo.TABLE_NAME_EVENT, cv, TableData.TableInfo.EVENT_ID + "=" + event_id, null);
    }
}
