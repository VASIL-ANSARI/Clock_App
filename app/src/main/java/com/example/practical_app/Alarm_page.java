package com.example.practical_app;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

public class Alarm_page extends AppCompatActivity {



    private Button button_hr_up,button_hr_down,button_min_up,button_min_down,button_date_up,button_date_down,button_mon_up,button_mon_down
            ,button_year_up,button_year_down,button_sec_up,button_sec_down;
    private TextView hr,min,month,date,sec,year;
    private EditText editText;
    Toolbar toolbar;

    Alarm_setter alarm_setter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_page);
        toolbar=findViewById(R.id.toolbar_id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setActionBar(toolbar);
        }
        alarm_setter=new Alarm_setter();
        button_hr_up=findViewById(R.id.arrow_up_id_1);
        button_hr_down=findViewById(R.id.arrow_down_id_1);
        button_min_up=findViewById(R.id.arrow_up_id_2);
        button_min_down=findViewById(R.id.arrow_down_id_2);
        button_sec_up=findViewById(R.id.arrow_up_id_3);
        button_sec_down=findViewById(R.id.arrow_down_id_3);

        button_date_up=findViewById(R.id.arrow_up_id_11);
        button_date_down=findViewById(R.id.arrow_down_id_11);
        button_mon_up=findViewById(R.id.arrow_up_id_21);
        button_mon_down=findViewById(R.id.arrow_down_id_21);
        button_year_up=findViewById(R.id.arrow_up_id_31);
        button_year_down=findViewById(R.id.arrow_down_id_31);

        editText=findViewById(R.id.alarm_title_edit_text_id);


        hr=findViewById(R.id.hour_id);
        min=findViewById(R.id.minute_id);
        sec=findViewById(R.id.second_id);

        date=findViewById(R.id.date_id);
        month=findViewById(R.id.month_id);
        year=findViewById(R.id.year_id);

        hr.setText(alarm_setter.getHour());
        min.setText(alarm_setter.getMinute());
        sec.setText(alarm_setter.getSecond());

        date.setText(alarm_setter.getDate());
        month.setText(alarm_setter.getMonth());
        year.setText(alarm_setter.getYear());

        button_hr_up.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                alarm_setter.setHour(alarm_setter.increment_hour());
                hr.setText(alarm_setter.getHour());
            }
        });
        button_hr_down.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                alarm_setter.setHour(alarm_setter.decrement_hour());
                hr.setText(alarm_setter.getHour());
            }
        });
        button_min_up.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                alarm_setter.setMinute(alarm_setter.increment_minute());
                min.setText(alarm_setter.getMinute());
            }
        });
        button_min_down.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                alarm_setter.setMinute(alarm_setter.decrement_minute());
                min.setText(alarm_setter.getMinute());
            }
        });
        button_sec_up.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                alarm_setter.setSecond(alarm_setter.increment_second());
                sec.setText(alarm_setter.getSecond());
            }
        });
        button_sec_down.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                alarm_setter.setSecond(alarm_setter.decrement_second());
                sec.setText(alarm_setter.getSecond());
            }
        });

        button_date_up.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                alarm_setter.setDate(alarm_setter.increment_date());
                date.setText(alarm_setter.getDate());
            }
        });
        button_date_down.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                alarm_setter.setDate(alarm_setter.decrement_date());
                date.setText(alarm_setter.getDate());
            }
        });
        button_mon_up.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                alarm_setter.setMonth(alarm_setter.increment_month());
                month.setText(alarm_setter.getMonth());
            }
        });
        button_mon_down.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                alarm_setter.setMonth(alarm_setter.decrement_month());
                month.setText(alarm_setter.getMonth());
            }
        });
        button_year_up.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                alarm_setter.setYear(alarm_setter.increment_year());
                year.setText(alarm_setter.getYear());
            }
        });
        button_year_down.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                alarm_setter.setYear(alarm_setter.decrement_year());
                year.setText(alarm_setter.getYear());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.alarm_page_meu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {



        switch (item.getItemId()){
            case R.id.add_alarm_menu_id:

                alarm_setter.setTitle(editText.getText().toString());
                if(alarm_setter.getTitle().length()==0){
                    Snackbar snackbar=Snackbar.make(findViewById(R.id.alarm_page_id),"Alarm title not set!",Snackbar.LENGTH_LONG);
                    View view=snackbar.getView();

                    snackbar.setTextColor(Color.RED);
                    snackbar.setBackgroundTint(Color.BLACK);
                    FrameLayout.LayoutParams params=(FrameLayout.LayoutParams) view.getLayoutParams();
                    params.gravity= Gravity.TOP;
                    params.gravity=Gravity.CENTER_HORIZONTAL;
                    view.setLayoutParams(params);
                    snackbar.show();

                    return false;
                }
                Intent intent=new Intent();
                intent.putExtra("key_for_add_alarm",alarm_setter);
                setResult(RESULT_OK,intent);



                Toast.makeText(this,"Alarm Added Successfully...",Toast.LENGTH_SHORT).show();

                finish();

                return true;
            case R.id.cancel_menu_id:
                finish();
                /*Intent get_intent=new Intent(this,MainActivity.class);
                startActivity(get_intent);*/
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
        Log.d("message","pause method");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("message","destroy method");
    }


}