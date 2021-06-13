package com.SE.team12.pillalarm2021;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


/**
 * @brief MainActivity that show first app screen with alarm list
 * @author Knight
 * @date 2018.04.30
 * @version 1.0.0.1
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton fab;
    private ListView list;
    private AlarmAdapter alarmAdapter;
    private ImageButton medicine_search_btn;
    private EditText medicine_name_edt;
    private ImageView img;
    private androidx.appcompat.widget.Toolbar toolbar;
    //    String res;
    String user_id;
    String user_pw;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int flag = 0;
    String data;
    EditText editText;
    String key = "13eDEekDciUSM2KaZG34XJRFPOtc0jXo24NDK00W01nVkaEuN797zCO7VM%2BYYIDf9A50rafXgA2%2BJHYpSjNw4g%3D%3D";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    /**
     * @description java class of main activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 이 부분이 XML을 자바 객체로 변경해주는 부분

        alarmAdapter = new AlarmAdapter(getApplicationContext());
        String url = "http://apis.data.go.kr/1471000/DrbEasyDrugInfoService/" +
                "getDrbEasyDrugList?serviceKey=13eDEekDciUSM2KaZG34XJRFPOtc0jXo24NDK00W01nVkaEuN797zCO7VM%2BYYIDf9A50rafXgA2%2BJHYpSjNw4g%3D%3D&entpName=%ED%95%9C%EB%AF%B8%EC%95%BD%ED%92%88(%EC%A3%BC)";
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list = findViewById(R.id.list);
        img = findViewById(R.id.img);
        fab = findViewById(R.id.fab);
        medicine_search_btn=findViewById(R.id.medicine_search_btn);
        editText=findViewById(R.id.medicine_name);
        //to_memo = findViewById(R.id.to_memo);  // Find button widget in layout
//        to_report = findViewById(R.id.to_report);
////        login = (Button) findViewById(R.id.login);

//        logout = findViewById(R.id.logout);
//        to_alarmList = findViewById(R.id.to_alarm_list);
        sharedPreferences = getSharedPreferences("Login_Session", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        medicine_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.medicine_search_btn:
                        // 쓰레드를 생성하여 돌리는 구간
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                data = getData(); // 하단의 getData 메소드를 통해 데이터를 파싱
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(MainActivity.this, Pillsearch.class);
                                        intent.putExtra("data",data);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }).start();
                        break;
                }

            }
        });
        list.setAdapter(alarmAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alarmAdapter.notifyDataSetChanged();
                JSONObject tmp = (JSONObject) alarmAdapter.getItem(position);

                switch ((int) id) {
                    case 0:
                        try {
                            Intent intent = new Intent(getApplicationContext(), SettingAlarm.class);
                            intent.putExtra("Exist", tmp.toString());
                            getApplicationContext().startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case 1:
                        try {
                            Intent intent = new Intent(getApplicationContext(), SettingAlarm.class);
                            intent.putExtra("Exist", tmp.toString());
                            getApplicationContext().startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case 2:
                        try {
                            DB db;
                            db = new DB(getApplicationContext(), "Alarm.db", null, 1);
                            db.getWritableDatabase();
                            db.myDelete("medicine_alarm", "medicine_name = \"" + tmp.getString("medicine_name") + "\"");
                            db.close();
                            alarmAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case 3:
                        AlarmManager alarm = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                        Calendar c = Calendar.getInstance();

                        try {
                            String times = tmp.getString("time");
                            String[] time = times.split(" ");

                            if (tmp.get("auto") == "false")
                                return;
                            if (c.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
                                return;

                            Log.d("AUTO_ALARM", tmp.getString("auto") + ", " + c.get(Calendar.DAY_OF_WEEK));
                            Toast.makeText(getApplicationContext(), tmp.getString("medicine_name") + " 약을 먹었습니다. 점심, 저녁 약시간에 알려드리도록 하겠습니다.", Toast.LENGTH_SHORT).show();

                            String current = String.valueOf(Calendar.getInstance().getTimeInMillis()/1000.0);

                            DB db = new DB(getApplicationContext(), "Taken.db", null, 1);
                            db.getWritableDatabase();
                            db.myInsert("medicine_taken", "medicine_name, time", "\"" + tmp.getString("medicine_name") + "\", \"" + current +"\"");
                            try {
                                db = new DB(getApplicationContext(), "Alarm.db", null, 1);
                                db.getWritableDatabase();
                                JSONArray res = new JSONArray(db.mySelect("medicine_alarm", "remain", "alarm_id = "+tmp.getInt("alarm_id")));
                                JSONObject cur = res.getJSONObject(0);
                                db.myUpdate("medicine_alarm", "remain = "+(cur.getInt("remain")-1), "alarm_id = "+tmp.getInt("alarm_id"));
                                db.close();

                                if (cur.getInt("remain")-1 < 10) {
                                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    NotificationCompat.Builder builder = null;

                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                        int importance = NotificationManager.IMPORTANCE_HIGH;
                                        NotificationChannel notificationChannel = new NotificationChannel("ID", "Name", importance);
                                        notificationManager.createNotificationChannel(notificationChannel);
                                        builder = new NotificationCompat.Builder(getApplicationContext(), notificationChannel.getId());
                                    } else {
                                        builder = new NotificationCompat.Builder(getApplicationContext());
                                    }

                                    builder = builder
                                            .setSmallIcon(R.drawable.ic_launcher_background)
                                            .setColor(Color.BLUE)
                                            .setContentTitle(tmp.getString("medicine_name"))
                                            .setTicker("Capsule Timer")
                                            .setContentText(tmp.getString("Title") + "약이 " + (cur.getInt("remain")-1) + "개만 남았습니다.")
                                            .setDefaults(Notification.DEFAULT_ALL)
                                            .setAutoCancel(true);
                                    notificationManager.notify(123, builder.build());

                                }

                                JSONObject info = new JSONObject();
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Login_Session", MODE_PRIVATE);
                                String user_id = sharedPreferences.getString("Id", "None");


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            double interval = 0.0;
                            for (int idx = 1; idx < time.length; idx ++) {
                                String []hhmm1 = time[idx].split(":");
                                String []hhmm2 = time[idx-1].split(":");
                                interval += (Double.parseDouble(hhmm1[0]) - Double.parseDouble(hhmm2[0]))*3600;
                                interval += (Double.parseDouble(hhmm1[1]) - Double.parseDouble(hhmm2[1]))*60;
                            }
                            interval *= 1000;
                            interval /= time.length - 1;

                            for (int idx = 1; idx < time.length; idx ++) {
                                long tmpTime = c.getTimeInMillis() + (long) interval * idx;
                                Calendar currentTime = Calendar.getInstance();
                                currentTime.setTimeInMillis(tmpTime);
                                String curHHMM = String.valueOf(currentTime.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(currentTime.get(Calendar.MINUTE));
                                Intent intent = new Intent("com.mobile_term_project.knight.alarm.AlarmRinging");
                                intent.setClass(getApplicationContext(), MyBroadcastReceiver.class);
                                intent.putExtra("Id", tmp.getInt("alarm_id"));
                                intent.putExtra("Title", tmp.getString("medicine_name"));
                                intent.putExtra("Type", "Alarm");
                                intent.putExtra("repeat_no", tmp.getInt("repeat_no"));
                                intent.putExtra("original_repeat_no", tmp.getInt("repeat_no"));
                                intent.putExtra("repeat_time", tmp.getInt("repeat_type"));
                                intent.putExtra("date", tmp.getString("date"));
                                intent.putExtra("time", curHHMM);
                                intent.putExtra("weekOfDate", tmp.getInt("weekOfDate"));
                                intent.putExtra("auto", "true");

                                String reqId = Module.genPendingIntentId(tmp.getInt("alarm_id"), curHHMM);
                                Log.d("Alarm", curHHMM.split(":")[1]);
//                                if (curHHMM.split(":")[0].length() == 1)
//                                    reqId = tmp.getInt("alarm_id")+"0"+curHHMM.split(":")[0]+curHHMM.split(":")[1]+"0";
//                                else if (curHHMM.split(":")[0].length() == 2)
//                                    reqId = tmp.getInt("alarm_id")+curHHMM.split(":")[0]+curHHMM.split(":")[1]+"0";


                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), Integer.parseInt(reqId), intent, PendingIntent.FLAG_ONE_SHOT);

                                Log.d("DATA", currentTime.getTime()+", ");
//                                if (Build.VERSION.SDK_INT >= 23) {
//                                    alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, tmpTime, pendingIntent);
//                                }else if (Build.VERSION.SDK_INT >= 19){
//                                    alarm.setExact(AlarmManager.RTC_WAKEUP, tmpTime, pendingIntent);
//                                }
//                                else {
//                                    alarm.set(AlarmManager.RTC_WAKEUP, tmpTime, pendingIntent);
//                                }
                                Module.notiVersion(alarm, currentTime, pendingIntent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                }

                list.setAdapter(new AlarmAdapter(getApplicationContext()));
            }
        });

        /**
         * @description add button event click listener
         */


        // 약 검색 시 키보드에서 완료 버튼 누르면 약 검색 이벤트가 완료된것

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingAlarm.class);
                startActivity(intent);
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }



    /**
     * @brief set alarm list to the list view and get id&password data from sharedPreferences
     */
    @Override
    protected void onResume() {
        alarmAdapter = new AlarmAdapter(getApplicationContext());
        list.setAdapter(alarmAdapter);

        user_id = sharedPreferences.getString("Id", "None");
        user_pw = sharedPreferences.getString("Password", "None");

        super.onResume();
    }

    /**
     * @brief process back button action
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    /**
     * @brief when user select menu in navigation, then execute proper action
     * @param item
     * @return boolean value
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_memo) {
            // Handle the camera action
            Intent intent = new Intent(getApplicationContext(), memomain.class);  // Create intent and move to memo activity
            startActivity(intent);
        } else if (id == R.id.nav_homepage) {
            Intent intent = new Intent(getApplicationContext(), AppHomepage.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            editor.remove("Id");
            editor.remove("Password");
            editor.commit();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    String getData(){

        StringBuffer buffer = new StringBuffer();
        String str =  editText.getText().toString();
        String location = URLEncoder.encode(str);
        String queryUrl="http://apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList?"
                +"serviceKey=13eDEekDciUSM2KaZG34XJRFPOtc0jXo24NDK00W01nVkaEuN797zCO7VM%2BYYIDf9A50rafXgA2%2BJHYpSjNw4g%3D%3D"+
                "&itemName="+location+"&pageNo=1&startPage=1&numOfRows=3";

        try {
            URL url= new URL(queryUrl); // 문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); // url 위치로 인풋스트림 연결
            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();

            // inputstream 으로부터 xml 입력받기

            xpp.setInput( new InputStreamReader(is, "UTF-8") );
            String tag;
            xpp.next();
            int eventType= xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작 단계 \n\n");
                        break;
                    case XmlPullParser.START_TAG:
                        tag= xpp.getName(); // 태그 이름 얻어오기
                        if(tag.equals("item")) ;
                        else if(tag.equals("entpName")){
                            buffer.append("제조사 : ");
                            xpp.next();
                            // addr 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append(xpp.getText());
                            buffer.append("\n"); // 줄바꿈 문자 추가
                        }
                        else if(tag.equals("itemName")){
                            buffer.append("제품명 : ");
                            xpp.next();
                            // addr 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append(xpp.getText());
                            buffer.append("\n"); // 줄바꿈 문자 추가
                        }
                        else if(tag.equals("useMethodQesitm")){
                            buffer.append("복용법 : ");
                            xpp.next();
                            // addr 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            str = xpp.getText().replace("<p>","");
                            str = str.replace("</p>","");
                            buffer.append(str);
                            buffer.append("\n"); // 줄바꿈 문자 추가
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); // 태그 이름 얻어오기
                        if(tag.equals("item")) buffer.append("\n"); // 첫번째 검색결과종료 후 줄바꿈
                        break;
                }
                eventType= xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        buffer.append("파싱 종료 단계 \n");
        return buffer.toString(); // 파싱 다 종료 후 StringBuffer 문자열 객체 반환
    }
}
