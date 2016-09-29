package com.btloop.moneymanage.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.btloop.moneymanage.MainActivity;
import com.btloop.moneymanage.R;
import com.btloop.moneymanage.adapter.ListEventAdapter;
import com.btloop.moneymanage.database.DatabaseOperation;
import com.btloop.moneymanage.entity.Event;
import com.btloop.moneymanage.entity.ExpandableHeightListView;
import com.btloop.moneymanage.entity.Month;
import com.btloop.moneymanage.ulti.ConerButton;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by thede on 4/17/2016.
 */
public class MainFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    public static MainFragment newInstance(CalendarDay calendarDay) {
        MainFragment a = new MainFragment();
        if (calendarDay != null) {
            MainFragment.selectedDate = calendarDay;
        }
        return a;
    }

    public static int kt_notification;
    public static final String DATEPICKER_TAG = "datepicker";
    int money = 0;
    int money_all_used = 0;
    ArrayList<Event> listEvent;
    ListEventAdapter adapter;
    ExpandableHeightListView list;
    EditText input_money;
    TextView money_used, money_rest, no_item;
    MaterialCalendarView calendarView;
    public static DatabaseOperation databaseOperation;
    Button setMoney, choose_day, choose_today;
    public static CalendarDay selectedDate;
    int month_id;

    View view;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mainfragment, container, false);
        start();
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        //chon ngay nhanh
        choose_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.setVibrate(false);
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCloseOnSingleTapDay(false);
                datePickerDialog.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });
        if (savedInstanceState != null) {
            DatePickerDialog dpd = (DatePickerDialog) getActivity().getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
            if (dpd != null) {
                dpd.setOnDateSetListener(this);
            }
        }
        MainActivity.add.setVisibility(View.VISIBLE);
        databaseOperation = new DatabaseOperation(getActivity());
        listEvent = new ArrayList<>();

        adapter = new ListEventAdapter(getActivity(), listEvent);
        //tat ban phim
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        //calendar any setting
//        Date a = new Date();
//        a.setMonth(y.getMonth());
//        a.setYear(y.getYear());
//        a.setDate(1);
//        //calendarView.setMinimumDate(a);
//        calendarView.setSelectedDate(y);
        calendarView.setSelectedDate(selectedDate);
        calendarView.setCurrentDate(selectedDate);
        //selectedDate = calendarView.getSelectedDate();
        month_id = databaseOperation.getMonthId(databaseOperation, selectedDate.getMonth(), selectedDate.getYear());
        //
        money = databaseOperation.getMaxMoney(databaseOperation, month_id);
        update();
        if (databaseOperation.checkMonth(databaseOperation, selectedDate.getMonth(), selectedDate.getYear()) == 0) {
            Month m = new Month(selectedDate.getMonth(), selectedDate.getYear());
            databaseOperation.putMonth(databaseOperation, m);
        }
        listEvent.clear();
        Cursor CR = databaseOperation.getEventByDay(databaseOperation, selectedDate.getDay(), month_id);
        if (CR.getCount() > 0) {
            do {
                Event e = new Event(CR.getInt(0), CR.getInt(1), CR.getString(2), CR.getInt(3), CR.getString(4), CR.getString(5), CR.getInt(6));
                listEvent.add(e);
                Log.e("EVENT ", e.getEvent_id() + "");
            }
            while (CR.moveToNext());
        } else if (CR.getCount() == 0) {
            no_item.setVisibility(View.VISIBLE);
        }

        //list su kien
        list.setExpanded(true);
        list.setAdapter(adapter);

        //khi thay doi seleted date
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectedDate = date;
                listEvent.clear();
                no_item.setVisibility(View.GONE);
                Cursor CR = databaseOperation.getEventByDay(databaseOperation, selectedDate.getDay(), month_id);
                if (CR.getCount() > 0) {
                    do {
                        Event e = new Event(CR.getInt(0), CR.getInt(1), CR.getString(2), CR.getInt(3), CR.getString(4), CR.getString(5), CR.getInt(6));
                        listEvent.add(e);
                        Log.e("EVENT ", e.getEvent_id() + "");
                    }
                    while (CR.moveToNext());
                } else {
                    no_item.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
                //  Toast.makeText(getActivity(),listEvent.get(0).getEventName(),Toast.LENGTH_LONG).show();

            }
        });
        //khi swipe thay doi thang hien hanh`
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                Log.e("Month change", "");
                calendarView.setSelectedDate(date);
                selectedDate = date;
                month_id = databaseOperation.getMonthId(databaseOperation, selectedDate.getMonth(), selectedDate.getYear());

                listEvent.clear();
                no_item.setVisibility(View.GONE);
                Cursor CR = databaseOperation.getEventByDay(databaseOperation, selectedDate.getDay(), month_id);
                if (CR.getCount() > 0) {
                    do {
                        Event e = new Event(CR.getInt(0), CR.getInt(1), CR.getString(2), CR.getInt(3), CR.getString(4), CR.getString(5), CR.getInt(6));
                        listEvent.add(e);
                        Log.e("EVENT ", e.getEvent_id() + "");
                    }
                    while (CR.moveToNext());
                } else {
                    no_item.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
                if (databaseOperation.checkMonth(databaseOperation, date.getMonth(), date.getYear()) == 0) {
                    Month m = new Month(date.getMonth(), date.getYear());
                    databaseOperation.putMonth(databaseOperation, m);
                }
                Log.e("MONTH_ID", "" + month_id);
                money = databaseOperation.getMaxMoney(databaseOperation, month_id);
                update();
            }

        });
        //
        //set goc cho btn
        new ConerButton().setCorner(setMoney, "#00C853");
        //khi click nut hom nay
        choose_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Time today = new Time(Time.getCurrentTimezone());
                today.setToNow();
                long x = today.toMillis(false);
                Date today1 = new Date(x);
                calendarView.setSelectedDate(today1);
                calendarView.setCurrentDate(today1);
                selectedDate = calendarView.getSelectedDate();
                listEvent.clear();
                no_item.setVisibility(View.GONE);
                month_id = databaseOperation.getMonthId(databaseOperation, selectedDate.getMonth(), selectedDate.getYear());
                Cursor CR = databaseOperation.getEventByDay(databaseOperation, selectedDate.getDay(), month_id);
                if (CR.getCount() > 0) {
                    do {
                        Event e = new Event(CR.getInt(0), CR.getInt(1), CR.getString(2), CR.getInt(3), CR.getString(4), CR.getString(5), CR.getInt(6));
                        listEvent.add(e);
                        Log.e("EVENT ", e.getEvent_id() + "");
                    }
                    while (CR.moveToNext());
                } else {
                    no_item.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }
        });
        //khi bam nut de thay doi gia tri tong to tien
        setMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setMoney.getText().toString().compareTo("Sửa") == 0) {
                    input_money.setText("0");
                    input_money.setEnabled(true);
                    setMoney.setText("Đặt");
                } else {
                    String str = input_money.getText().toString();
                    if (str.compareTo("") == 0)
                        str = "0";
                    money = Integer.valueOf(str);
                    update();
                    setMoney.setText("Sửa");
                }
            }
        });
        //list
        // click vao xem chi tiet 1 su kien
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right, R.anim.slide_in_left, R.anim.slide_out_left);
                EditEventFragment e = EditEventFragment.newInstance();
                e.setEvent(listEvent.get(position));
                fragmentTransaction.replace(R.id.container, e);
                fragmentTransaction.addToBackStack("Main");
                fragmentTransaction.commit();
            }
        });
//        list.addHeaderView(new View(getActivity()));
//        list.addFooterView(new View(getActivity()));

        return view;
    }

    //cap nhat tong so tien , da tieu , con lai tu database
    public void update() {

        String s = NumberFormat.getIntegerInstance().format(money);
        databaseOperation.updateMoneyInMonth(databaseOperation, month_id, money);
        input_money.setText(s);
        input_money.setEnabled(false);
        money_all_used = databaseOperation.getAllMoneyUsed(databaseOperation, month_id);
        s = NumberFormat.getIntegerInstance().format(money_all_used);
        money_used.setText(String.valueOf(s));
        s = NumberFormat.getIntegerInstance().format(money - money_all_used);
        money_rest.setText(String.valueOf(s));
        if ((money - money_all_used) < 0 && kt_notification == 1) {
            Onsend("Vượt mức chi tiêu " + (selectedDate.getMonth() + 1) + "/" + selectedDate.getYear());
        } else if ((money - money_all_used) < money / 5 && kt_notification == 1) {
            Onsend("Sắp vượt mức chi tiêu! Còn ít hơn 20% " + (selectedDate.getMonth() + 1) + "/" + selectedDate.getYear());
        }

    }

    //khoi tao view
    public void start() {
        no_item = (TextView) view.findViewById(R.id.no_item);
        no_item.setVisibility(View.GONE);
        calendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        //text view
        money_used = (TextView) view.findViewById(R.id.money_used);
        money_rest = (TextView) view.findViewById(R.id.money_rest);
        //khung nhap so tien
        input_money = (EditText) view.findViewById(R.id.input_money);
        input_money.setEnabled(false);
        setMoney = (Button) view.findViewById(R.id.set_money);
        list = (ExpandableHeightListView) view.findViewById(R.id.list_event);
        choose_day = (Button) view.findViewById(R.id.choose_day);
        choose_today = (Button) view.findViewById(R.id.today);
    }
//khi thay doi ngay trong "Chon ngay nhanh"
    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        CalendarDay a = new CalendarDay(year, month, day);
        calendarView.setCurrentDate(a);
        calendarView.setSelectedDate(a);
    }
//tao notification
    public void Onsend(String noti) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity())
                .setSmallIcon(R.drawable.notification_template_icon_bg).setContentTitle("Money Management").setContentText(noti);
        mBuilder.setSmallIcon(R.drawable.a32);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);
        mBuilder.setVibrate(new long[]{0, 200, 200, 600, 600});
        mBuilder.setLights(Color.RED, 3000, 3000);
        mBuilder.setAutoCancel(true);
        //khi can tao pending intent
//        Intent intent = new Intent(getActivity(),MainActivity.class);
//        PendingIntent pending = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//       mBuilder.setContentIntent(pending);
        NotificationManager nm = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(001, mBuilder.build());
    }

}
