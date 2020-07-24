package com.example.practical_app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static java.lang.Integer.min;
import static java.lang.Integer.parseInt;

public class FirstFragment extends Fragment {


    public static ListViewAdapter recyclerViewAdapter;
    private ListView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    public static ArrayList<Alarm_setter> fragment_list=new ArrayList<>();
    private Handler handler=new Handler();
    private static TextView hour;
    private static TextView minute;
    private static TextView second;
    private static Button cancel_timer_button;
    private static Button start_timer_button;
    public String message;



    public static List<Alarm_setter> userSelection=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        message=getArguments().getString("message");
        if(message.equals("2")){
            View view=inflater.inflate(R.layout.fragment_second,container,false);

            set_up_second_view(view);

            return view;
        }


        View view =inflater.inflate(R.layout.fragment_first,container,false);
        recyclerView=view.findViewById(R.id.list_item_id);

        System.out.println(fragment_list.size());

        recyclerViewAdapter=new ListViewAdapter(getActivity(),R.layout.list_view_item,fragment_list);
        recyclerView.setAdapter(recyclerViewAdapter);

        return view;
    }



    public void set_up_second_view(View view){
        start_timer_button=view.findViewById(R.id.timer_start_button);
        cancel_timer_button=view.findViewById(R.id.timer_cancel_button);
        cancel_timer_button.setEnabled(false);


        hour=view.findViewById(R.id.timer_hour_id);
        minute=view.findViewById(R.id.timer_minute_id);
        second=view.findViewById(R.id.timer_second_id);

        SharedPreferences preferences=getActivity().getSharedPreferences("timer_storage",getActivity().MODE_PRIVATE);
        String string=preferences.getString("MyTimer",null);
        if(string!=null){
            Log.d("message",string);
            hour.setText(string.substring(0,2));
            minute.setText(string.substring(3,5));
            second.setText(string.substring(6));
        }

        Button hr_up=view.findViewById(R.id.arrow_up_timer_hour);
        Button min_up=view.findViewById(R.id.arrow_up_timer_minute);
        Button sec_up=view.findViewById(R.id.arrow_up_timer_second);

        Button hr_down=view.findViewById(R.id.arrow_down_timer_hour);
        final Button min_down=view.findViewById(R.id.arrow_down_timer_minute);
        final Button sec_down=view.findViewById(R.id.arrow_down_timer_second);

        final Timer timer=new Timer(hour.getText().toString(),minute.getText().toString(),second.getText().toString());

        hr_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.setHour(hour.getText().toString());

                timer.setHour(timer.increment_hour());
                hour.setText(timer.getHour());
            }
        });

        hr_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.setHour(hour.getText().toString());
                timer.setHour(timer.decrement_hour());
                hour.setText(timer.getHour());
            }
        });

        min_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.setMinute(minute.getText().toString());
                timer.setMinute(timer.increment_minute());
                minute.setText(timer.getMinute());
            }
        });

        min_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.setMinute(minute.getText().toString());
                timer.setMinute(timer.decrement_minute());
                minute.setText(timer.getMinute());
            }
        });

        sec_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.setSecond(second.getText().toString());
                timer.setSecond(timer.increment_second());
                second.setText(timer.getSecond());
            }
        });

        sec_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.setSecond(second.getText().toString());
                timer.setSecond(timer.decrement_second());
                second.setText(timer.getSecond());
            }
        });

        final Example example=new Example(Integer.parseInt(hour.getText().toString()),Integer.parseInt(minute.getText().toString()),Integer.parseInt(second.getText().toString()));
        start_timer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(start_timer_button.getText().toString().equals("START")) {
                    example.setTimer_hr(Integer.parseInt(hour.getText().toString()));
                    example.setTimer_min(Integer.parseInt(minute.getText().toString()));
                    example.setTimer_sec(Integer.parseInt(second.getText().toString()));

                    cancel_timer_button.setEnabled(true);
                    example.started();

                    start_timer_button.setText("STOP");
                }
                else{
                    if(example!=null){
                        example.stopped();
                        start_timer_button.setText("START");
                        cancel_timer_button.setEnabled(true);
                    }

                }

                //start_timer_method();
            }
        });
        cancel_timer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                example.stopped();
                start_timer_button.setText("START");
                cancel_timer_button.setEnabled(false);
                hour.setText("00");
                minute.setText("00");
                second.setText("00");
            }
        });


    }

    public class Example extends Thread {
        private int timer_sec;
        private int timer_min;
        private int timer_hr;
        private boolean exit;
        final String GET_CHANNEL_ID="timer_id";
        final int NOTIFICATION_ID=001;
        private Thread t;

        private SharedPreferences preferences=getActivity().getSharedPreferences("timer_storage",getActivity().MODE_PRIVATE);

        private SharedPreferences.Editor editor=preferences.edit();

        public int getTimer_sec() {
            return timer_sec;
        }

        public void setTimer_sec(int timer_sec) {
            this.timer_sec = timer_sec;
        }

        public int getTimer_min() {
            return timer_min;
        }

        public void setTimer_min(int timer_min) {
            this.timer_min = timer_min;
        }

        public int getTimer_hr() {
            return timer_hr;
        }

        public void setTimer_hr(int timer_hr) {
            this.timer_hr = timer_hr;
        }

        public Example(int timer_hr, int timer_min, int timer_sec) {
            this.timer_sec = timer_sec;
            this.timer_min = timer_min;
            this.timer_hr = timer_hr;
            System.out.println(timer_hr+" "+timer_min+" "+timer_sec);
        }

        public void started() {
            t = new Thread(this);
            exit = false;
            t.start();
        }


        @Override
        public void run() {
            while (!exit) {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        if(timer_sec==0 && timer_min==0 && timer_hr==0){
                            cancel_timer_button.setEnabled(false);
                            start_timer_button.setText("START");
                        }
                        String name=new Integer(timer_hr).toString();
                        if(name.length()==1)
                            name="0"+name;
                        hour.setText(name);
                        name=new Integer(timer_min).toString();
                        if(name.length()==1)
                            name="0"+name;
                        minute.setText(name);
                        name=new Integer(timer_sec).toString();
                        if(name.length()==1)
                            name="0"+name;
                        second.setText(name);
                        String string="";
                        if(hour!=null && minute!=null && second!=null){
                            string=hour.getText().toString()+" "+minute.getText().toString()+" "+second.getText().toString();
                        }

                        editor.putString("MyTimer",string);
                        editor.commit();
                        Log.d("message",string);
                    }
                });
                if(timer_sec==0 && timer_min==0 && timer_hr==0){
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(timer_sec==0){
                    if(timer_min==0){
                        if(timer_hr==0)
                            break;
                        timer_hr-=1;
                        timer_min=59;
                    }
                    else{
                        timer_min-=1;
                        timer_sec=59;
                    }
                }
                else{
                    timer_sec-=1;
                }


            }
            if(timer_sec==0 && timer_min==0 && timer_hr==0 && !exit){
                //add notification
                Log.d("message","completed");

                createNotificationChannel();

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), GET_CHANNEL_ID);
                builder.setSmallIcon(R.drawable.ic_baseline_timer);
                builder.setContentTitle("Timer");
                builder.setContentText("Your timer is up...");
                builder.setPriority(NotificationCompat.PRIORITY_HIGH);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getActivity());
                notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());




            }
        }

        private void createNotificationChannel(){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                CharSequence name="Personal Notification";
                String description ="Include personal information";
                int importance=NotificationManager.IMPORTANCE_DEFAULT;

                NotificationChannel notificationChannel=new NotificationChannel(GET_CHANNEL_ID,name,importance);

                notificationChannel.setDescription(description);

                NotificationManager notificationManager=(NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);

                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        public void stopped() {
            exit = true;

        }
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.d("message","first-fragment stopped");


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("message","first-fragment destroyed");
    }

    public static void set_up_timer(String str){
        cancel_timer_button.performClick();
        hour.setText(str.substring(0,2));
        minute.setText(str.substring(3,5));
        second.setText(str.substring(6));
    }

}