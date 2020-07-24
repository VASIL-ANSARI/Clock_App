package com.example.practical_app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<Alarm_setter> {
    private List<Alarm_setter> list;
    private Context context;


    public ListViewAdapter(@NonNull Context context, int resId,List<Alarm_setter> list) {
        super(context,R.layout.list_view_item,resId,list);
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater=((Activity)context).getLayoutInflater();
        View view=layoutInflater.inflate(R.layout.list_view_item,parent,false);

        ImageView imageView=view.findViewById(R.id.alertTitle_id);


        TextView txt=view.findViewById(R.id.alarm_text_id);
        TextView bt_add_date=view.findViewById(R.id.add_alarm_date_id);
        TextView bt_add_time=view.findViewById(R.id.add_alarm_time_id);

        String date=list.get(position).getDate()+"/"+list.get(position).getMonth()+"/"+list.get(position).getYear();
        String time=list.get(position).getHour()+":"+list.get(position).getMinute()+" "+list.get(position).getSecond();
        bt_add_date.setText(date);
        bt_add_time.setText(time);
        txt.setText(list.get(position).getTitle());

        final CheckBox checkBox=view.findViewById(R.id.checkbox_id);
        checkBox.setTag(position);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.getVisibility()==CheckBox.GONE) {
                    checkBox.setVisibility(CheckBox.VISIBLE);
                }
                else
                    checkBox.setVisibility(CheckBox.GONE);
            }
        });


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int position=(int)buttonView.getTag();

                if(FirstFragment.userSelection.contains(list.get(position))){
                    FirstFragment.userSelection.remove(list.get(position));
                }
                else{
                    FirstFragment.userSelection.add(list.get(position));
                }

                //FirstFragment.actionMode.setTitle(FirstFragment.userSelection.size()+" items selected..");
            }
        });
        return view;
    }

    public void remove_items(List<Alarm_setter> items){
        System.out.println(items);
        System.out.println(list);
        for(Alarm_setter a:items){
            list.remove(a);
        }
        FirstFragment.userSelection.clear();
        notifyDataSetChanged();
    }
}
