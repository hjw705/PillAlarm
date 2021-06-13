package com.SE.team12.pillalarm2021;

import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.SE.team12.pillalarm2021.DB;
import com.SE.team12.pillalarm2021.Module;
import com.SE.team12.pillalarm2021.MyBroadcastReceiver;

import org.json.JSONArray;
import org.json.JSONObject;

public class AlarmService extends Service {

    public AlarmManager alarm;
    public Context context;

    public AlarmService(Context context) {
        this.context = context;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.SE.team12.pillalarm2021.AlarmRinging");

        context.registerReceiver(new MyBroadcastReceiver(), intentFilter);
        alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    }

    /**
     * @brief make alarm with data that read from database
     */
    public void setFromDB() {
        DB db = new DB(getApplicationContext(), "Alarm.db", null, 1);
        try {
            JSONArray alarmSet = new JSONArray(db.mySelect("medicine_alarm", "*", "1 = 1"));
            for (int json = 0; json < alarmSet.length(); json++) {
                JSONObject alarmInfo = alarmSet.getJSONObject(json);
                Module.settingAlarm(context, alarmInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief when save button is clicked in alarm setting screen, make alarm with data that user input
     * @param alarms
     */
    public void setFromButton(JSONArray alarms) {
        try {
            Log.d("JSON", alarms.toString());
            JSONObject alarmInfo = alarms.getJSONObject(0);
            Module.settingAlarm(context, alarmInfo);
        } catch(Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}