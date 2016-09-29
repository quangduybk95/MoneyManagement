package com.btloop.moneymanage.fragment;

/**
 * Created by thede on 5/7/2016.
 */

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.btloop.moneymanage.MainActivity;
import com.btloop.moneymanage.R;
import com.btloop.moneymanage.entity.Event;
import com.btloop.moneymanage.ulti.ConerButton;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.text.NumberFormat;
import java.util.Calendar;

/**
 * Created by thede on 4/17/2016.
 */
public class AddEventFragment extends Fragment implements TimePickerDialog.OnTimeSetListener {
    public static AddEventFragment newInstance() {
        AddEventFragment a = new AddEventFragment();
        return a;
    }

    int money = 0;
    int kt = 0;
    Event event;
    public static final String TIMEPICKER_TAG = "timepicker";
    Button set_money, submit, setTimeStart, setTimeFinish;
    EditText input_money, input_name;
    TextView showDate, showTimeStart, showTimeFinish;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.create_event, container, false);
        start();
        MainActivity.add.setVisibility(View.GONE);
        event = new Event();
        showDate.setText(MainFragment.selectedDate.getDay() + "/" + (MainFragment.selectedDate.getMonth() + 1) + "/" + MainFragment.selectedDate.getYear());
        ConerButton conerButton = new ConerButton();
        conerButton.setCorner(set_money, "#00C853");
        conerButton.setCorner(setTimeStart, "#00C853");
        conerButton.setCorner(setTimeFinish, "#00C853");


        //khi set money cho event
        set_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (set_money.getText().toString().compareTo("Sửa") == 0) {
                    input_money.setText("0");
                    input_money.setEnabled(true);
                    set_money.setText("Đặt");

                } else {

                    String str = input_money.getText().toString();
                    if (str.compareTo("") == 0)
                        str = "0";
                    money = Integer.valueOf(str);
                    String s = NumberFormat.getIntegerInstance().format(money);
                    input_money.setText(s);
                    input_money.setEnabled(false);
                    set_money.setText("Sửa");
                }
            }
        });
        //setup set date
        final Calendar calendar = Calendar.getInstance();
        final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);
        setTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.setVibrate(false);
                timePickerDialog.setCloseOnSingleTapMinute(false);
                timePickerDialog.show(getActivity().getSupportFragmentManager(), TIMEPICKER_TAG);
                kt = 1;
            }
        });
        setTimeFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.setVibrate(false);
                timePickerDialog.setCloseOnSingleTapMinute(false);
                timePickerDialog.show(getActivity().getSupportFragmentManager(), TIMEPICKER_TAG);
                kt = 2;

            }
        });
        if (savedInstanceState != null) {
            TimePickerDialog tpd = (TimePickerDialog) getActivity().getSupportFragmentManager().findFragmentByTag(TIMEPICKER_TAG);
            if (tpd != null) {
                tpd.setOnTimeSetListener(this);
            }

        }
        //submit to create event
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn muốn tạo sự kiện này ?");
                builder.setPositiveButton("Có",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (input_name.getText().toString().trim().compareTo("") == 0) {
                                    input_name.setError("Bạn cần nhập tên sự kiện");
                                } else if (input_money.getText().toString().trim().compareTo("") == 0) {
                                    input_money.setError("Bạn cần nhập số tiền sử dụng");
                                    Toast.makeText(getActivity(), "Bạn cần nhập số tiền sử dụng", Toast.LENGTH_LONG).show();
                                } else {
                                    event.setDay(MainFragment.selectedDate.getDay());
                                    int month_id = MainFragment.databaseOperation.getMonthId(MainFragment.databaseOperation,
                                            MainFragment.selectedDate.getMonth(), MainFragment.selectedDate.getYear());
                                    event.setMonth_id(month_id);
                                    event.setEventName(input_name.getText().toString());
                                    event.setStart_time(showTimeStart.getText().toString());
                                    event.setEnd_time(showTimeFinish.getText().toString());
                                    event.setMoney(money);
                                    MainFragment.databaseOperation.putEvent(MainFragment.databaseOperation, event);
                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    getActivity().getSupportFragmentManager().popBackStack("Main", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right, R.anim.slide_in_left, R.anim.slide_out_left)
                                            .replace(R.id.container, MainFragment.newInstance(MainFragment.selectedDate));
                                    fragmentTransaction.commit();
                                }
                            }
                        });
                builder.setNegativeButton("Không",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
            }
        });
        return view;
    }
//    private boolean isVibrate() {
//        return ((CheckBox) findViewById(R.id.checkBoxVibrate)).isChecked();
//    }
//
//    private boolean isCloseOnSingleTapDay() {
//        return ((CheckBox) findViewById(R.id.checkBoxCloseOnSingleTapDay)).isChecked();
//    }
//
//    private boolean isCloseOnSingleTapMinute() {
//        return ((CheckBox) findViewById(R.id.checkBoxCloseOnSingleTapMinute)).isChecked();
//    }

//change time
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        if (kt == 1) {
            String start_time = hourOfDay + ":" + minute;
            showTimeStart.setText(start_time);
            event.setStart_time(start_time);
        } else if (kt == 2) {
            String end_time = hourOfDay + ":" + minute;
            showTimeFinish.setText(end_time);
             event.setEnd_time(end_time);
        }
    }
    //khoi tao view
    public void start()
    {
        showDate = (TextView) view.findViewById(R.id.display_day);
        showTimeFinish = (TextView) view.findViewById(R.id.show_time_finish);
        showTimeStart = (TextView) view.findViewById(R.id.show_time_start);
        showTimeStart.setText("00:00");
        showTimeFinish.setText("00:00");
        setTimeStart = (Button) view.findViewById(R.id.set_time_start);
        setTimeFinish = (Button) view.findViewById(R.id.set_time_end);
        set_money = (Button) view.findViewById(R.id.set_money);
        input_money = (EditText) view.findViewById(R.id.input_money);
        input_money.setEnabled(false);
        input_name = (EditText) view.findViewById(R.id.input_event_name);
        submit = (Button) view.findViewById(R.id.submit);

    }
}
