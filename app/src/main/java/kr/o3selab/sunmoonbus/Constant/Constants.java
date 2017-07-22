package kr.o3selab.sunmoonbus.Constant;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import kr.o3selab.sunmoonbus.Model.DBManager;

public class Constants {

    // 어플리케이션 정보
    public static Activity activity;
    public static Context context;
    public static DBManager mDBManager;
    public static CharSequence tabTitles[];

    // 버전정보
    public static double appVersion = 2.1;
    public static long timeTableVersion;
    public static long vacationPeriodStart;
    public static long vacationPeriodEnd;

    // 개인 식별 정보
    public static String deviceID;

    // 요일/휴일 타입
    public static final int HOLIDAY = 10;
    public static final int NOHOLIDAY = 20;
    public static final int DONTKNOW = 30;
    public static final int WEEKDAY_NOHOLIDAY = 1;
    public static final int SATURDAY_NOHOLIDAY = 2;
    public static final int SUNDAY_NOHOLIDAY = 3;
    public static final int WEEKDAY_HOLIDAY = 4;
    public static final int WEEKEND_HOLIDAY = 5;

    // 설정정보
    private static String MY_SHARED_PREF = "my_shared";
    public static final String HOLIDAY_PERIOD_START = "holiday_start";
    public static final String HOLIDAY_PERIOD_END = "holiday_end";

    public static final String AD_ALERT_SKIP = "ad_alert_skip";
    public static final String REMOVE_AD = "remove_ad";
    public static final String FREE_USER = "free_user";

    public static SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(MY_SHARED_PREF, Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getEditor() {
        return getSharedPreferences().edit();
    }

    // 광고 정보
    public static boolean isRemoveAd = false;
    public static boolean isFreeUser = false;

    // 에러 리포팅
    public static StringBuilder logs;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");

    public static void printLog(int type, @Nullable String msg, @Nullable Exception e) {
        StringBuilder log = new StringBuilder();

        log.append("SMBus").append("(").append(sdf.format(new Date(System.currentTimeMillis()))).append(") : ");
        if(e != null) {
            log.append(e.getMessage());
        } else if (msg != null) {
            log.append(msg);
        }

        switch (type) {
            case 1:
                Log.d("SMBus", log.toString());
                logs.append(log.toString()).append("\n");
                break;

            case 2:
                Log.e("SMBus", log.toString());
                logs.append(log.toString()).append("\n");
                sendReport();
                break;

            case 3:
                Log.d("SMBus", log.toString());
                logs.append(log.toString()).append("\n");
                sendReport();
                break;
        }
    }

    private static void sendReport() {
        String sb =
                "Device Default Information" + "\n" +
                "DeviceID: " + Constants.deviceID + "\n" +
                "MODEL: " + Build.MODEL + "\n" +
                "MANUFACTURER: " + Build.MANUFACTURER + "\n" +
                "Android Version: " + Build.VERSION.RELEASE + "\n\n" +
                "Error Log" + "\n" +
                logs.toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.child("eLogs").push().setValue(sb);
    }
}
