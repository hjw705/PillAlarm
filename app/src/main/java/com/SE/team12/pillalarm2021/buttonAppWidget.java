package com.SE.team12.pillalarm2021;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.SE.team12.pillalarm2021.DB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;

public class buttonAppWidget extends AppWidgetProvider {

    DB db1, db2;
    private String medicine_name = "";
    Calendar c;

    @Override
    public void onReceive(Context context, Intent reIntent) {
        super.onReceive(context, reIntent);

        db1 = new DB(context, "Alarm.db", null, 1);
        db1.getWritableDatabase();

        try {
            JSONArray result = new JSONArray(db1.mySelect("medicine_alarm", "*", "1 = 1"));
            JSONObject tmp = result.getJSONObject(0);
            medicine_name += tmp.getString("medicine_name");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String action = reIntent.getAction();
        if(AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action))
        {
            Bundle extras = reIntent.getExtras();
            if(extras!=null)
            {
                int [] appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
                if(appWidgetIds!=null && appWidgetIds.length>0)
                    this.onUpdate(context,AppWidgetManager.getInstance(context),appWidgetIds);
            }
        }
        else if(action.equals("Click1"))
        {
            try {
                c = Calendar.getInstance();
                db2.myInsert("medicine_taken", "medicine_name, time", "\"" + medicine_name + "\", " + c.getTime());

                AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

                JSONArray result = new JSONArray(db1.mySelect("medicine_alarm", "*", "1 = 1"));
                JSONObject current = result.getJSONObject(0);
                String times = current.getString("time");
                String[] time = times.split(" ");

                if (current.get("auto") != "false" && !(c.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY || c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)){
                    double interval = 0.0;
                    for (int idx = 1; idx < time.length; idx++) {
                        String[] hhmm1 = time[idx].split(":");
                        String[] hhmm2 = time[idx - 1].split(":");
                        interval += (Double.parseDouble(hhmm1[0]) - Double.parseDouble(hhmm2[0])) * 3600;
                        interval += (Double.parseDouble(hhmm1[1]) - Double.parseDouble(hhmm2[1])) * 60;
                    }
                    interval *= 1000;
                    interval /= time.length - 1;

                    for (int idx = 1; idx < time.length; idx++) {
                        long tmpTime = c.getTimeInMillis() + (long) interval * idx;
                        Calendar currentTime = Calendar.getInstance();
                        currentTime.setTimeInMillis(tmpTime);
                        String curHHMM = String.valueOf(currentTime.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(currentTime.get(Calendar.MINUTE));
                        Intent intent = new Intent("com.mobile_term_project.knight.alarm.AlarmRinging");
                        intent.setClass(context, MyBroadcastReceiver.class);
                        intent.putExtra("Title", current.getString("medicine_name"));
                        intent.putExtra("Type", "Alarm");
                        intent.putExtra("repeat_no", current.getInt("repeat_no"));
                        intent.putExtra("original_repeat_no", current.getInt("repeat_no"));
                        intent.putExtra("repeat_time", current.getInt("repeat_time"));
                        intent.putExtra("date", current.getString("date"));
                        intent.putExtra("time", curHHMM);
                        intent.putExtra("weekOfDate", current.getInt("weekOfDate"));
                        intent.putExtra("auto", "true");
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                        Module.notiVersion(alarm, currentTime, pendingIntent);
                    }
                    Toast.makeText(context,"다음 복용 시간으로 설정되었습니다",Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        final int N = appWidgetIds.length;
        for(int i=0;i<N;i++)
        {
            int appWidgetId = appWidgetIds[i];
            RemoteViews views = buildViews(context);
            appWidgetManager.updateAppWidget(appWidgetId,views);
        }
    }


    private PendingIntent buildToastIntent(Context context)
    {
        Intent in = new Intent("Click1");

        PendingIntent pi = PendingIntent.getBroadcast(context,0,in, PendingIntent.FLAG_UPDATE_CURRENT);
        return pi;
    }


    private RemoteViews buildViews(Context context)
    {
        RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.button_app_widget);
        views.setOnClickPendingIntent(R.id.simple_btn2,buildToastIntent(context));

        return views;
    }

}

