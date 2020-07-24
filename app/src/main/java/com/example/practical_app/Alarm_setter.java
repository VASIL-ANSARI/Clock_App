package com.example.practical_app;


import android.content.Intent;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.io.SyncFailedException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class Alarm_setter implements Serializable {
    private String date,month,year,hour,minute,second,title;
    private String curr_date,curr_month,curr_year,curr_hour,curr_minute,curr_second;

    public Alarm_setter() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd_hhmmaa", Locale.getDefault());
        String current=simpleDateFormat.format(new Date());
        Log.d("date",current);
        String[] array=current.split("_");
        hour=array[1].substring(0,2);
        minute=array[1].substring(2,4);
        second=array[1].substring(4,6);
        date=array[0].substring(6);
        month=array[0].substring(4,6);
        year=array[0].substring(0,4);
        curr_date=date;
        curr_month=month;
        curr_year=year;

        curr_hour=hour;
        curr_minute=minute;
        curr_second=second;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
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

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String increment_hour(){
        String ans=hour;
        Integer mhour=Integer.parseInt(hour);
        if(mhour==12){
            mhour=01;
            if(check_date_time(mhour.toString(),minute,second,date,month,year)){
                return "01";
            }
            else{
                return hour;
            }
        }
        mhour+=1;
        ans=mhour.toString();
        if(ans.length()==1){
            ans="0"+ans;
        }
        if(check_date_time(ans,minute,second,date,month,year)){
            return ans;
        }
        return hour;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String decrement_hour(){
        String ans=hour;
        Integer mhour=Integer.parseInt(hour);
        if(mhour==1){
            mhour=12;
            if(check_date_time(mhour.toString(),minute,second,date,month,year)){
                return "12";
            }
            else{
                return hour;
            }
        }
        mhour-=1;
        ans=mhour.toString();
        if(ans.length()==1){
            ans="0"+ans;
        }
        if(check_date_time(ans,minute,second,date,month,year)){
            return ans;
        }
        return hour;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String increment_minute(){
        Integer mminute=Integer.parseInt(minute);
        Integer hhour=Integer.parseInt(hour);
        String ans=minute;
        if((mminute==0 && hhour==24) || mminute==59){
            ans="00";
            if(check_date_time(hour,ans,second,date,month,year)){
                return ans;
            }
            return minute;
        }
        mminute+=1;
        ans=mminute.toString();
        if(ans.length()==1){
            ans="0"+ans;
        }
        if(check_date_time(hour,ans,second,date,month,year)){
            return ans;
        }
        return minute;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String decrement_minute(){
        Integer mminute=Integer.parseInt(minute);
        String ans=minute;
        if(mminute==0){
            ans="00";
            if(check_date_time(hour,ans,second,date,month,year)){
                return ans;
            }
            return minute;
        }
        mminute-=1;
        ans=mminute.toString();
        if(ans.length()==1){
            ans="0"+ans;
        }
        if(check_date_time(hour,ans,second,date,month,year)){
            return ans;
        }
        return minute;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String increment_second(){
        if(second=="PM"){
            if(check_date_time(hour,minute,"AM",date,month,year)){
                return "AM";
            }
            return second;
        }
        if(check_date_time(hour,minute,"PM",date,month,year)){
            return "PM";
        }
        return second;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String decrement_second(){
        if(second=="PM"){
            if(check_date_time(hour,minute,"AM",date,month,year)){
                return "AM";
            }
            return second;
        }
        if(check_date_time(hour,minute,"PM",date,month,year)){
            return "PM";
        }
        return second;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String increment_date(){
        Integer mdate=Integer.parseInt(date);
        Integer mmonth=Integer.parseInt(month);
        Integer myear=Integer.parseInt(year);
        String ans=date;
        if(mmonth==1 || mmonth==3 || mmonth==5 || mmonth==7 || mmonth==8 || mmonth==10 || mmonth==12){
            if(mdate==31){
                return mdate.toString();
            }
        }
        else if(mmonth==4 || mmonth==6 || mmonth==9 || mmonth==11){
            if(mdate==30){
                return mdate.toString();
            }
        }
        else{
            if(myear%4==0){
                if(mdate==29){
                    return mdate.toString();
                }
            }
            else{
                if(mdate==28){
                    return mdate.toString();
                }
            }
        }
        mdate+=1;
        ans=mdate.toString();
        if(ans.length()==1){
            ans= "0"+ans;
        }
        if(check_date_time(hour,minute,second,ans,month,year)){
            return ans;
        }
        return date;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String decrement_date(){
        Integer mdate=Integer.parseInt(date);
        String ans=date;
        if(mdate==0)
            return date;
        mdate-=1;
        ans=mdate.toString();
        if(ans.length()==1){
            ans= "0"+ans;
        }
        if(check_date_time(hour,minute,second,ans,month,year)){
            return ans;
        }
        return date;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String increment_month(){
        Integer mmonth=Integer.parseInt(month);
        String ans=month;
        if(mmonth==12)
            return mmonth.toString();
        mmonth+=1;
        ans=mmonth.toString();
        if(ans.length()==1){
            ans= "0"+ans;
        }
        if(check_date_time(hour,minute,second,date,ans,year)){
            return ans;
        }
        return month;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String decrement_month(){
        Integer mmonth=Integer.parseInt(month);
        String ans=month;
        if(mmonth==0)
            return "00";
        mmonth-=1;
        ans=mmonth.toString();
        if(ans.length()==1){
            ans= "0"+ans;
        }
        if(check_date_time(hour,minute,second,date,ans,year)){
            return ans;
        }
        return month;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String increment_year(){
        Integer myear=Integer.parseInt(year);
        myear+=1;
        String ans=myear.toString();
        if(ans.length()==1){
            ans= "000"+ans;
        }
        else if(ans.length()==2){
            ans= "00"+ans;
        }
        else if(ans.length()==3){
            ans= "0"+ans;
        }
        if(check_date_time(hour,minute,second,date,month,ans)){
            return ans;
        }
        return year;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String decrement_year(){
        Integer myear=Integer.parseInt(year);
        if(myear==0){
            return year;
        }
        myear-=1;
        String ans=myear.toString();
        if(ans.length()==1){
            ans= "000"+ans;
        }
        else if(ans.length()==2){
            ans= "00"+ans;
        }
        else if(ans.length()==3){
            ans= "0"+ans;
        }
        if(check_date_time(hour,minute,second,date,month,ans)){
            return ans;
        }
        return year;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean check_date_time(String hr,String min,String sec,String date,String mon,String yr) {


        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd_hhmmaa", Locale.getDefault());
        String current=simpleDateFormat.format(new Date());
        String[] array=current.split("_");
        curr_hour=array[1].substring(0,2);
        curr_minute=array[1].substring(2,4);
        curr_second=array[1].substring(4,6);
        curr_date=array[0].substring(6);
        curr_month=array[0].substring(4,6);
        curr_year=array[0].substring(0,4);

        try {
            String date1 = curr_year+curr_month+curr_date;
            String time1 = curr_hour+curr_minute+curr_second;
            String date2 = yr+mon+date;
            String time2 = hr+min+sec;

            String format = "yyyyMMdd_hhmmaa";

            SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.getDefault());

            Date dateObj1 = sdf.parse(date1 + "_" + time1);
            Date dateObj2 = sdf.parse(date2 + "_" + time2);
            System.out.println(dateObj1);
            System.out.println(dateObj2 + "\n");

            //DecimalFormat crunchifyFormatter = new DecimalFormat("###,###");

            long diff = dateObj2.getTime() - dateObj1.getTime();
            System.out.println(diff);

            if(diff<0)
                return false;
            else
                return true;

/**
            int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
            System.out.println("difference between days: " + diffDays);

            int diffhours = (int) (diff / (60 * 60 * 1000));
            System.out.println("difference between hours: " + crunchifyFormatter.format(diffhours));

            int diffmin = (int) (diff / (60 * 1000));
            System.out.println("difference between minutues: " + crunchifyFormatter.format(diffmin));

            int diffsec = (int) (diff / (1000));
            System.out.println("difference between seconds: " + crunchifyFormatter.format(diffsec));

            System.out.println("difference between milliseconds: " + crunchifyFormatter.format(diff));
**/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public int get_duration(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd_hhmmss", Locale.getDefault());
        String current=simpleDateFormat.format(new Date());
        String[] array=current.split("_");
        curr_hour=array[1].substring(0,2);
        curr_minute=array[1].substring(2,4);
        curr_second=array[1].substring(4,6);
        curr_date=array[0].substring(6);
        curr_month=array[0].substring(4,6);
        curr_year=array[0].substring(0,4);
        try {
            String date1 = curr_year + curr_month + curr_date;
            String time1 = curr_hour + curr_minute + curr_second;
            String date2 = year + month + date;
            String time2 = hour + minute + "00";

            String format = "yyyyMMdd_hhmmss";

            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());

            Date dateObj1 = sdf.parse(date1 + "_" + time1);
            Date dateObj2 = sdf.parse(date2 + "_" + time2);
            System.out.println(dateObj1);
            System.out.println(dateObj2 + "\n");

            long diff = dateObj2.getTime() - dateObj1.getTime();
            int diffsec = (int) (diff / (1000));
            return diffsec;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

}
