package com.example.practical_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.nio.file.FileStore;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.zip.Inflater;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private FragmentPageAdapter fragmentPageAdapter;
    private TabLayout tabLayout;
    private Menu context_menu;
    private Handler handler=new Handler();
    private List<Alarm_thread_class> alarm_thread_classes=new ArrayList<>();
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("ALARM");
        preferences=getSharedPreferences("storage",MODE_PRIVATE);
        editor=preferences.edit();

        toolbar=findViewById(R.id.toolbar_id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setActionBar(toolbar);
        }


        SharedPreferences sharedPreferences=getSharedPreferences("storage",MODE_PRIVATE);
        String objList=sharedPreferences.getString("MyObj",null);
        if(objList!=null){
            Gson gson=new Gson();
            Type type=new TypeToken<List<Alarm_setter>>(){}.getType();

            FirstFragment.fragment_list=gson.fromJson(objList,type);
        }


            viewPager=findViewById(R.id.pager_id);
            fragmentPageAdapter=new FragmentPageAdapter(getSupportFragmentManager(),FragmentPageAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            viewPager.setAdapter(fragmentPageAdapter);

            tabLayout=findViewById(R.id.tab);
            tabLayout.setupWithViewPager(viewPager);

            viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
                @Override
                public void onPageSelected(int position) {
                    ActionBar actionBar=getSupportActionBar();
                    switch(position){
                        case 1:

                            actionBar.setTitle("TIMER");
                            context_menu.getItem(0).setVisible(false);
                            context_menu.getItem(1).setVisible(false);
                            break;
                        case 0:

                            actionBar.setTitle("ALARM");
                            context_menu.getItem(0).setVisible(true);
                            if(FirstFragment.fragment_list.size()==0){
                                context_menu.getItem(1).setVisible(false);
                            }
                            else {
                                context_menu.getItem(1).setVisible(true);
                            }
                            break;
                    }
                }
            });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){
            if(resultCode==RESULT_OK){
                Alarm_setter alarm_setter=(Alarm_setter)data.getSerializableExtra("key_for_add_alarm");
                if(alarm_setter!=null) {
                    if (FirstFragment.fragment_list == null) {
                        FirstFragment.fragment_list = new ArrayList<>();
                    }
                    FirstFragment.fragment_list.add(alarm_setter);
                    if(context_menu.getItem(1).isVisible()==false)
                        context_menu.getItem(1).setVisible(true);
                    Alarm_thread_class alarm_thread_class=new Alarm_thread_class(alarm_setter);
                    alarm_thread_classes.add(alarm_thread_class);

                    alarm_thread_class.started();

                }
                fragmentPageAdapter=new FragmentPageAdapter(getSupportFragmentManager(),FragmentPageAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
                viewPager.setAdapter(fragmentPageAdapter);
            }
        }
        if(requestCode==10){

            if(resultCode==RESULT_OK && data!=null){
                ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                Log.d("message",result.get(0)+"-"+result.get(1));

                String ttimer=create_alarm(result.get(0));
                if(ttimer.length()>0) {
                    FirstFragment.set_up_timer(ttimer);
                }
                Log.d("message",result.get(0));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String create_alarm(String s) {
        String[] result=s.split(" ");
        for(String str:result){
            if(str.equals("timer")){
                String ttimer=go_to_timer(result);

                return ttimer;
            }
        }
        return go_to_alarm(result);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String go_to_alarm(String[] result) {

        Alarm_setter alarm_setter=new Alarm_setter();
        String alarm_hour=alarm_setter.getHour();
        String alarm_minute=alarm_setter.getMinute();
        String alarm_second=alarm_setter.getSecond();
        String alarm_date=alarm_setter.getDate();
        String alarm_month=alarm_setter.getMonth();
        String alarm_year=alarm_setter.getYear();

        int n=result.length;
        for(int i=0;i<n;i++){
            if(result[i].contains(":")){
                String[] str=result[i].split(":");
                if(str.length==2 && isNumber(str[0]) && isNumber(str[1])){
                    if(str[0].length()==1){
                        alarm_hour="0"+str[0];
                    }
                    else {
                        alarm_hour = str[0];
                    }
                    if(str[1].length()==1){
                        alarm_minute="0"+str[1];
                    }
                    else {
                        alarm_minute = str[1];
                    }
                    if(i+1<n && (result[i+1].toLowerCase().equals("a.m.")  || result[i+1].toLowerCase().equals("p.m.")  || result[i+1].toLowerCase().equals("morning")
                     || result[i+1].toLowerCase().equals("evening"))){
                        if(result[i+1].toLowerCase().equals("a.m.")  || result[i+1].toLowerCase().equals("morning")){
                            alarm_second="AM";
                        }
                        else {
                            alarm_second="PM";
                        }
                    }
                }
            }
            if(result[i].equals("date") || result[i].equals("dated")){
                if(i+1<n && result[i+1].equals("on")){
                    if(i+2<n && isNumber(result[i+2])){
                        if(result[i+2].length()==1){
                            alarm_date="0"+result[i+2];
                        }
                        else{
                            alarm_date=result[i+2];
                        }
                    }
                    if(i+3<n){
                        if(isNumber(result[i+3])){
                            if(result[i+3].length()==1){
                                alarm_month="0"+result[i+3];
                            }
                            else{
                                alarm_month=result[i+3];
                            }
                        }
                        else{
                            alarm_month=get_Month(result[i+3]);
                        }
                    }
                    if(i+4<n && isNumber(result[i+4])){
                        alarm_year=result[i+4];
                    }
                }
                else if(i+1<n){
                    if(i+1<n && isNumber(result[i+1])){
                        if(result[i+1].length()==1){
                            alarm_date="0"+result[i+1];
                        }
                        else{
                            alarm_date=result[i+1];
                        }
                    }
                    if(i+2<n){
                        if(isNumber(result[i+2])){
                            if(result[i+2].length()==1){
                                alarm_month="0"+result[i+2];
                            }
                            else{
                                alarm_month=result[i+2];
                            }
                        }
                        else{
                            alarm_month=get_Month(result[i+2]);
                        }
                    }
                    if(i+3<n && isNumber(result[i+3])){
                        alarm_year=result[i+3];
                    }
                }
            }
        }
        String res="";
        for(String s:result){
            res+=s;
            res+=" ";
        }
        res=res.toLowerCase();
        Log.d("message",res);
        if(res.contains("today")){
            alarm_date=alarm_setter.getDate();
            alarm_month=alarm_setter.getMonth();
            alarm_year=alarm_setter.getYear();
        }
        else if(res.contains("day after tomorrow")){
            alarm_date=alarm_setter.getDate();
            alarm_month=alarm_setter.getMonth();
            alarm_year=alarm_setter.getYear();

            alarm_date=alarm_setter.getDate();
            alarm_month=alarm_setter.getMonth();
            alarm_year=alarm_setter.getYear();

            String new_date=alarm_setter.increment_date();
            if(new_date.equals(alarm_date)){
                String new_month=alarm_setter.increment_month();
                if(new_month.equals(alarm_month)){
                    alarm_year=alarm_setter.increment_year();
                    alarm_date="01";
                    alarm_month="01";
                }
                else{
                    alarm_month=new_month;
                    alarm_date="01";
                }
            }
            else{
                alarm_date=new_date;
            }

            alarm_setter.setDate(alarm_date);
            alarm_setter.setMonth(alarm_month);
            alarm_setter.setYear(alarm_year);

            new_date=alarm_setter.increment_date();
            if(new_date.equals(alarm_date)){
                String new_month=alarm_setter.increment_month();
                if(new_month.equals(alarm_month)){
                    alarm_year=alarm_setter.increment_year();
                    alarm_date="01";
                    alarm_month="01";
                }
                else{
                    alarm_month=new_month;
                    alarm_date="01";
                }
            }
            else{
                alarm_date=new_date;
            }
        }
        else if(res.contains("tomorrow")){
            alarm_date=alarm_setter.getDate();
            alarm_month=alarm_setter.getMonth();
            alarm_year=alarm_setter.getYear();

            String new_date=alarm_setter.increment_date();
            if(new_date.equals(alarm_date)){
                String new_month=alarm_setter.increment_month();
                if(new_month.equals(alarm_month)){
                    alarm_year=alarm_setter.increment_year();
                    alarm_date="01";
                    alarm_month="01";
                }
                else{
                    alarm_month=new_month;
                    alarm_date="01";
                }
            }
            else{
                alarm_date=new_date;
            }
        }
        else if(res.contains("yesterday")){
            return "";
        }


        Log.d("message",alarm_hour+":"+alarm_minute+" "+alarm_second+" "+alarm_date+"_"+alarm_month+"_"+alarm_year);



        if(alarm_setter.check_date_time(alarm_hour,alarm_minute,alarm_second,alarm_date,alarm_month,alarm_year)){
            alarm_setter.setHour(alarm_hour);
            alarm_setter.setMinute(alarm_minute);
            alarm_setter.setSecond(alarm_second);
            alarm_setter.setDate(alarm_date);
            alarm_setter.setMonth(alarm_month);
            alarm_setter.setYear(alarm_year);
            alarm_setter.setTitle("Voice-alarm");
            FirstFragment.fragment_list.add(alarm_setter);
            FirstFragment.recyclerViewAdapter.notifyDataSetChanged();
            if(context_menu.getItem(1).isVisible()==false)
                context_menu.getItem(1).setVisible(true);
            Alarm_thread_class alarm_thread_class=new Alarm_thread_class(alarm_setter);
            alarm_thread_classes.add(alarm_thread_class);

            alarm_thread_class.started();
        }


        return "";
    }

    private String get_Month(String s) {
        HashMap<String,String> mp=new HashMap<>();
        mp.put("January","01");
        mp.put("February","02");
        mp.put("March","03");
        mp.put("April","04");
        mp.put("May","05");
        mp.put("June","06");
        mp.put("July","07");
        mp.put("August","08");
        mp.put("September","09");
        mp.put("October","10");
        mp.put("November","11");
        mp.put("December","12");
        return mp.get(s);
    }

    private String go_to_timer(String[] result) {
        int i,n;
        n=result.length;
        String ans="";
        String hour="00";
        String minute="00";
        String second="00";
        for(i=0;i<n;i++){
            if(isNumber(result[i])){
                if((i+1)<n && (result[i+1].equals("hour") || result[i+1].equals("hours"))){
                    if(result[i].length()==1){
                        hour="0"+result[i];
                    }
                    else{
                        hour=result[i];
                    }
                }
                if((i+1)<n && (result[i+1].equals("minutes") || result[i+1].equals("minute"))){
                    if(result[i].length()==1){
                        minute="0"+result[i];
                    }
                    else {
                        minute = result[i];
                    }
                }
                if((i+1)<n && (result[i+1].equals("seconds") || result[i+1].equals("second"))){
                    if(result[i].length()==1){
                        second="0"+result[i];
                    }
                    else {
                        second = result[i];
                    }
                }
            }
        }
        ans=hour+" "+minute+" "+second;
        return ans;


    }

    private boolean isNumber(String s) {
        try{
            Integer.parseInt(s);
        }catch (Exception e){
            return false;
        }

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_items,menu);
        context_menu=menu;
        if(FirstFragment.fragment_list.size()==0)
        {
            menu.getItem(1).setVisible(false);
        }
        else{
            menu.getItem(1).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.add_alarm_id){

            Intent intent=new Intent(this,Alarm_page.class);
            startActivityForResult(intent,1);
        }
        if(item.getItemId()==R.id.settings_id){
            Toast.makeText(this,"Go to settings",Toast.LENGTH_LONG).show();
        }
        if(item.getItemId()==R.id.action_delete){
            List<Alarm_setter> alarm_setterList=FirstFragment.userSelection;
            if(alarm_setterList==null || alarm_setterList.size()==0){
                Toast.makeText(this,"No item selected",Toast.LENGTH_LONG).show();
            }
            else {



                Toast.makeText(this,alarm_setterList.size()+" items deleted",Toast.LENGTH_LONG).show();
                for(Alarm_setter a:alarm_setterList){
                    for(Alarm_thread_class b:alarm_thread_classes){
                        if(b.alarmSetter==a){
                            b.stopped();
                        }
                    }
                }
                FirstFragment.recyclerViewAdapter.remove_items(alarm_setterList);
                if(FirstFragment.fragment_list.size()==0)
                    context_menu.getItem(1).setVisible(false);

            }
        }
        if(item.getItemId()==R.id.voice_id){

            Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
            try{
                startActivityForResult(intent,10);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("message","main=pause method");
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        Gson gson=new Gson();
        String json=gson.toJson(FirstFragment.fragment_list);
        editor.putString("MyObj",json);
        editor.commit();

        Log.d("message","main=destroy method");
    }

    public class Alarm_thread_class extends Thread{
        private int seconds;
        private String GET_CHANNEL_ID;
        private int NOTIFICATION_ID;
        private String name;
        private boolean exit;
        private Alarm_setter alarmSetter;

        public void setAlarmSetter(Alarm_setter alarmSetter) {
            this.alarmSetter = alarmSetter;
        }

        private Thread t;

        public Alarm_thread_class(Alarm_setter alarmSetter){
            this.alarmSetter=alarmSetter;
            seconds=alarmSetter.get_duration();
            name=alarmSetter.getTitle();
            GET_CHANNEL_ID=name+new Integer(seconds).toString();
            NOTIFICATION_ID=seconds;

        }

        public void started(){
            t=new Thread(this,name);
            exit=false;
            t.start();
        }


        @Override
        public void run() {
            if(seconds<=0){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        List<Alarm_setter> llist=new ArrayList<>();
                        llist.add(alarmSetter);
                        FirstFragment.recyclerViewAdapter.remove_items(llist);
                        if(FirstFragment.fragment_list.size()==0){
                            context_menu.getItem(1).setVisible(false);
                        }
                        Gson gson=new Gson();
                        String json=gson.toJson(FirstFragment.fragment_list);
                        editor.putString("MyObj",json);
                        editor.commit();
                    }
                });

                exit=true;
            }
            while (!exit) {

                seconds -= 1;
                Log.d("message",new Integer(seconds).toString());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        if(seconds==0 && !exit){
                            List<Alarm_setter> llist=new ArrayList<>();
                            llist.add(alarmSetter);
                            FirstFragment.recyclerViewAdapter.remove_items(llist);
                            if(FirstFragment.fragment_list.size()==0){
                                context_menu.getItem(1).setVisible(false);
                            }
                            Gson gson=new Gson();
                            String json=gson.toJson(FirstFragment.fragment_list);
                            editor.putString("MyObj",json);
                            editor.commit();
                        }
                    }
                });
                if(seconds==0)
                    break;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (seconds == 0 && !exit) {
                //Log.d("message","activity ended");

                createNotificationChannel();

                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, GET_CHANNEL_ID);
                builder.setSmallIcon(R.drawable.ic_alarm);
                builder.setContentTitle(name);
                builder.setContentText("Your alarm is triggered...");
                builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);
                notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());




            }
        }

        public void stopped(){
            exit=true;
        }

        private void createNotificationChannel(){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                CharSequence name="Personal Notification";
                String description ="Include personal information";
                int importance= NotificationManager.IMPORTANCE_DEFAULT;

                NotificationChannel notificationChannel=new NotificationChannel(GET_CHANNEL_ID,name,importance);

                notificationChannel.setDescription(description);

                NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
    }
}