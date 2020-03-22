package com.example.lakshmi.alarm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.Calendar;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;



import android.widget.RadioButton;

public class SetAlarm extends Activity{
    String s;
    DatePicker pickerDate;
    TimePicker pickerTime;
    Button buttonSetAlarm;
    TextView info;
    EditText editText;
    final static int RQS_1 = 1;
    RadioButton optAlarm, optNotification, optRingtone;
    Button btnStart, btnStop;
    Ringtone ringTone;


    Uri uriAlarm, uriNotification, uriRingtone,uchecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        optAlarm = (RadioButton)findViewById(R.id.optAlarm);
        optNotification = (RadioButton)findViewById(R.id.optNotification);
        optRingtone = (RadioButton)findViewById(R.id.optRingtone);
        btnStart = (Button)findViewById(R.id.start);
        btnStop = (Button)findViewById(R.id.stop);


        uriAlarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        uriNotification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        uriRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);


        btnStart.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                if(ringTone!=null){
                    //previous ringTone is playing,
                    //stop it first
                    ringTone.stop();
                }

                if(optAlarm.isChecked()){
                    ringTone = RingtoneManager
                            .getRingtone(getApplicationContext(), uriAlarm);

                }else if(optNotification.isChecked()){
                    ringTone = RingtoneManager
                            .getRingtone(getApplicationContext(), uriNotification);

                }else if(optRingtone.isChecked()){
                    ringTone = RingtoneManager
                            .getRingtone(getApplicationContext(), uriRingtone);

                }

                Toast.makeText(SetAlarm.this,
                        ringTone.getTitle(SetAlarm.this),
                        Toast.LENGTH_LONG).show();
                ringTone.play();
            }
        });

        btnStop.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                if(ringTone!=null){
                    ringTone.stop();
                    ringTone=null;
                }
            }
        });

        info = (TextView)findViewById(R.id.info);
        pickerDate = (DatePicker)findViewById(R.id.pickerdate);
        pickerTime = (TimePicker)findViewById(R.id.pickertime);
        editText=(EditText)findViewById(R.id.et);

        Calendar now = Calendar.getInstance();

        pickerDate.init(
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH),
                null);

        pickerTime.setCurrentHour(now.get(Calendar.HOUR_OF_DAY));
        pickerTime.setCurrentMinute(now.get(Calendar.MINUTE));

        buttonSetAlarm = (Button)findViewById(R.id.setalarm);
        buttonSetAlarm.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View arg0) {
                s=editText.getText().toString();
                if(optAlarm.isChecked()){

                    uchecked=uriAlarm;
                }else if(optNotification.isChecked()){

                    uchecked=uriNotification;
                }else if(optRingtone.isChecked()){

                    uchecked=uriRingtone;
                }

                Calendar current = Calendar.getInstance();

                Calendar cal = Calendar.getInstance();
                cal.set(pickerDate.getYear(),
                        pickerDate.getMonth(),
                        pickerDate.getDayOfMonth(),
                        pickerTime.getCurrentHour(),
                        pickerTime.getCurrentMinute(),
                        00);

                if(cal.compareTo(current) <= 0){
                    //The set Date/Time already passed
                    Toast.makeText(getApplicationContext(),
                            "Invalid Date/Time",
                            Toast.LENGTH_LONG).show();
                }else{



                    setAlarm(cal);
                    int s=(int)(cal.getTimeInMillis()-current.getTimeInMillis())/1000;
                    int m=s/60;
                    s=s%60;
                    int h=m/60;
                    m=m%60;
                    Toast.makeText(getApplicationContext(),editText.getText().toString()+" Alarm will trigger after  "+ h+" hrs :"+m+" min :"+s+" secs ",
                            Toast.LENGTH_LONG).show();
                }

            }});
    }

    private void setAlarm(Calendar targetCal){

        info.setText("\n\n***\n"
                + "Alarm is set@ " + targetCal.getTime() + "\n"
                + "***\n");

        Intent intent = new Intent(getBaseContext(),AlarmReceiver.class );
        intent.putExtra("st",s);
        intent.putExtra("ring",uchecked);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);


    }

}

