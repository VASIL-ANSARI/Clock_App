package com.example.practical_app;

public class Timer {

    private String hour,minute,second;

    public Timer(String hour, String minute, String second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
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

    public String increment_hour(){
        String ans=hour;
        if(ans.equals("23")){
            return ans;
        }
        ans=new Integer(Integer.parseInt(hour)+1).toString();
        if(ans.length()==1)
            ans="0"+ans;
        return ans;
    }
    public String decrement_hour(){
        String ans=hour;
        if(ans.equals("00")){
            return ans;
        }
        ans=new Integer(Integer.parseInt(hour)-1).toString();
        if(ans.length()==1)
            ans="0"+ans;
        return ans;
    }

    public String increment_minute(){
        String ans=minute;
        if(ans.equals("59")){
            return ans;
        }
        ans=new Integer(Integer.parseInt(minute)+1).toString();
        if(ans.length()==1)
            ans="0"+ans;
        return ans;
    }

    public String decrement_minute(){
        String ans=minute;
        if(ans.equals("00")){
            return ans;
        }
        ans=new Integer(Integer.parseInt(minute)-1).toString();
        if(ans.length()==1)
            ans="0"+ans;
        return ans;
    }

    public String increment_second(){
        String ans=second;
        if(ans.equals("59")){
            return ans;
        }
        ans=new Integer(Integer.parseInt(second)+1).toString();
        if(ans.length()==1)
            ans="0"+ans;
        return ans;
    }

    public String decrement_second(){
        String ans=second;
        if(ans.equals("00")){
            return ans;
        }
        ans=new Integer(Integer.parseInt(second)-1).toString();
        if(ans.length()==1)
            ans="0"+ans;
        return ans;
    }

}
