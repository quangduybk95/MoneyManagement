package com.btloop.moneymanage.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.text.NumberFormat;
import java.util.Calendar;

/**
 * Created by thede on 5/12/2016.
 */
public class EditEventFragment extends Fragment implements TimePickerDialog.OnTimeSetListener {
    public static EditEventFragment newInstance() {
        EditEventFragment a = new EditEventFragment();
        return a;
    }
    int kt = 0;
    public static final String TIMEPICKER_TAG = "timepicker";
    Button set_money, submit, setTimeStart, setTimeFinish,delete;
    EditText input_money, input_name;
    TextView showDate, showTimeStart, showTimeFinish;
    View view;

    public static Event getEvent() {
        return event;
    }

    public static void setEvent(Event event) {
        EditEventFragment.event = event;
    }
    public static Event event;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.editevent, container, false);
        start();
        MainActivity.add.setVisibility(View.GONE);
        //setup set date
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
                    event.setMoney(Integer.valueOf(str));
                    String s = NumberFormat.getIntegerInstance().format(event.getMoney());
                    input_money.setText(s);
                    input_money.setEnabled(false);
                    set_money.setText("Sửa");
                }
            }
        });
        //khoi tao time dialog
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
        set(event);
        //an nut xoa
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete.setTextColor(0xFF000000);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn muốn xóa sự kiện này ?");
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
                                    MainFragment.databaseOperation.deleteEvent(MainFragment.databaseOperation,event.getEvent_id());                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    getActivity().getSupportFragmentManager().popBackStack("Main", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right, R.anim.slide_in_left, R.anim.slide_out_left)
                                            .replace(R.id.container, MainFragment.newInstance(MainFragment.selectedDate));
                                    fragmentTransaction.commit();
                                }
                                delete.setTextColor(0xFFFFFFFF);
                            }
                        });
                builder.setNegativeButton("Không",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                delete.setTextColor(0xFFFFFFFF);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();

            }
        });
        //an nut cap nhat
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setTextColor(0xFF000000);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn muốn cập nhật sự kiện này ?");
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
                                    MainFragment.databaseOperation.updateEvent(MainFragment.databaseOperation,event,event.getEvent_id());
                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    getActivity().getSupportFragmentManager().popBackStack("Main", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right, R.anim.slide_in_left, R.anim.slide_out_left)
                                            .replace(R.id.container, MainFragment.newInstance(MainFragment.selectedDate));
                                    fragmentTransaction.commit();
                                }
                                submit.setTextColor(0xFFFFFFFF);
                            }
                        });
                builder.setNegativeButton("Không",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                submit.setTextColor(0xFFFFFFFF);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
            }
        });
        return view;
    }
    //khoi tao gia tri show ra man hinh
    public void set(Event event)
    {
        ConerButton conerButton = new ConerButton();
        conerButton.setCorner(set_money, "#00C853");
        conerButton.setCorner(setTimeStart, "#00C853");
        conerButton.setCorner(setTimeFinish, "#00C853");
        showDate.setText(MainFragment.selectedDate.getDay()+"/"+
                (MainFragment.selectedDate.getMonth()+1)+"/"+MainFragment.selectedDate.getYear());
        showTimeFinish.setText(event.getEnd_time());
        showTimeStart.setText(event.getStart_time());
        input_money.setText(String.valueOf(event.getMoney()));
        input_name.setText(event.getEventName());
    }
    //khoi tao view
    public void start()
    {
        showDate = (TextView) view.findViewById(R.id.display_day);
        showTimeFinish = (TextView) view.findViewById(R.id.show_time_finish);
        showTimeStart = (TextView) view.findViewById(R.id.show_time_start);
        setTimeStart = (Button) view.findViewById(R.id.set_time_start);
        setTimeFinish = (Button) view.findViewById(R.id.set_time_end);
        set_money = (Button) view.findViewById(R.id.set_money);
        input_money = (EditText) view.findViewById(R.id.input_money);
        input_money.setEnabled(false);
        input_name = (EditText) view.findViewById(R.id.input_event_name);
        submit = (Button) view.findViewById(R.id.submit);
        delete = (Button) view.findViewById(R.id.delete);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        if (kt == 1) {
            String start_time = hourOfDay + ":" + minute;
            showTimeStart.setText(start_time);
        } else if (kt == 2) {
            String end_time = hourOfDay + ":" + minute;
            showTimeFinish.setText(end_time);
        }
    }
}
