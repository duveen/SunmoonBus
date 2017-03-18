package kr.o3selab.sunmoonbus;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import kr.o3selab.sunmoonbus.Activity.MainActivity;
import kr.o3selab.sunmoonbus.Constant.API;
import kr.o3selab.sunmoonbus.Constant.Constants;
import kr.o3selab.sunmoonbus.Constant.GetHolidayDate;
import kr.o3selab.sunmoonbus.Constant.GetTimeTableData;
import kr.o3selab.sunmoonbus.Model.DBManager;

public class LoadingActivity extends AppCompatActivity {

    private DBManager mDBManager;
    public int timeTableStatus;
    public int holidayStatus;
    public boolean isHoliday;
    private RoundCornerProgressBar pBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Constants.activity = this;
        Constants.context = this;
        Constants.logs = new StringBuilder();

        try {
            Constants.deviceID = AdvertisingIdClient.getAdvertisingIdInfo(this).getId();
        } catch (Exception e) {
            Constants.deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        pBar = (RoundCornerProgressBar) findViewById(R.id.progressBar);
        pBar.setMax(14);

        mDBManager = new DBManager(this, "SMBus.db", null, 1);
        Constants.mDBManager = mDBManager;

        timeTableStatus = 0;
        holidayStatus = 0;
        isHoliday = false;

        findRemoveAdPackageAndVersionOnFirebase();
    }

    public void findRemoveAdPackageAndVersionOnFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        try {
            Query removeAD = databaseReference.child("freeUsers");
            removeAD.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        boolean isFreeUser = dataSnapshot.getValue().toString().contains(Constants.deviceID);
                        SharedPreferences.Editor editor = Constants.getEditor();
                        if (isFreeUser) {
                            Constants.isFreeUser = true;
                            editor.putBoolean(Constants.FREE_USER, true);
                        } else {
                            Constants.isFreeUser = false;
                            editor.putBoolean(Constants.FREE_USER, false);
                        }
                        editor.commit();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Query versionInfo = databaseReference.child("version");
            versionInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    HashMap map = (HashMap) dataSnapshot.getValue();

                    Double releasedVersion = Double.parseDouble(map.get("version_info").toString());
                    Boolean isImportant = (Boolean) map.get("isImportant");
                    String updateContent = (String) map.get("update_content");

                    Constants.printLog(1, "AppVersion : " + Constants.appVersion + ", releasedVersion : " + releasedVersion, null);
                    Constants.printLog(1, "isImportantUpdate : " + isImportant, null);

                    if (Constants.appVersion == releasedVersion) {
                        initalization();
                    } else {
                        if (isImportant) {
                            new AlertDialog.Builder(LoadingActivity.this)
                                    .setTitle(R.string.loading_update_alert)
                                    .setMessage(getString(R.string.loading_update_message_important) + updateContent)
                                    .setPositiveButton(R.string.loading_update_positive, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setData(Uri.parse("market://details?id=" + getPackageName()));
                                            startActivity(intent);

                                            LoadingActivity.this.finish();
                                        }
                                    })
                                    .setNegativeButton(R.string.loading_update_negative_exit, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            LoadingActivity.this.finish();
                                        }
                                    })
                                    .setCancelable(false)
                                    .show();
                        } else {
                            new AlertDialog.Builder(LoadingActivity.this)
                                    .setTitle(R.string.loading_update_alert)
                                    .setMessage(getString(R.string.loading_update_message) + updateContent)
                                    .setPositiveButton(R.string.loading_update_positive, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setData(Uri.parse("market://details?id=" + getPackageName()));
                                            startActivity(intent);

                                            LoadingActivity.this.finish();
                                        }
                                    })
                                    .setNegativeButton(R.string.loading_update_negative_cancel, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            initalization();
                                        }
                                    })
                                    .setCancelable(true)
                                    .show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(LoadingActivity.this, R.string.loading_receive_error_toast_message, Toast.LENGTH_SHORT).show();
                    LoadingActivity.this.finish();
                }
            });


        } catch (Exception e) {
            Constants.printLog(2, null, e);
        }
    }

    public void initalization() {
        String query = "select * from info;";
        Cursor versionCursor = mDBManager.execReadSQL(query);

        if (versionCursor.moveToNext()) {
            // 셔틀 버전 불러오기
            Constants.printLog(1, "Start Loading.. Version : " + versionCursor.getString(0), null);
            Constants.timeTableVersion = Long.parseLong(versionCursor.getString(0));
            String time = new SimpleDateFormat("yyyy.MM.dd").format(new Date(Constants.timeTableVersion));
            Toast.makeText(this, getString(R.string.loading_show_dataversion_prefix) + time + getString(R.string.loading_show_dataversion_suffix), Toast.LENGTH_SHORT).show();

            pBar.setMax(1);

            goNextActivity();
        } else {
            Constants.printLog(1, "Start First Loading..", null);
            pBar.setMax(pBar.getMax() + 1);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 날짜 정보 불러오기
                        insertDate();
                        Constants.printLog(1, "Insert Date : " + System.currentTimeMillis(), null);

                        // 셔틀 시간표 불러오기
                        new Thread(new GetTimeTableData(LoadingActivity.this)).start();

                        while (true) {
                            if (timeTableStatus == API.SUCCESS) {
                                Constants.printLog(1, "Success Get Time Table", null);
                                break;
                            } else if (timeTableStatus == API.HTTP_HANDLER_ERROR) {
                                showDontReceiveData(new Exception("HTTP handling error"));
                                return;
                            }
                        }

                        // 방학 상태 체크하기
                        new Thread(new GetHolidayDate(LoadingActivity.this)).start();

                        while (true) {
                            if (holidayStatus == Constants.HOLIDAY) {
                                Constants.printLog(1, "Success Get Holiday Date : true", null);
                                isHoliday = true;
                                break;
                            } else if (holidayStatus == Constants.NOHOLIDAY) {
                                Constants.printLog(1, "Success Get Holiday Date : false", null);
                                isHoliday = false;
                                break;
                            } else if (holidayStatus == Constants.DONTKNOW) {
                                showDontReceiveData(new Exception("Don't know holiday status"));
                                return;
                            }
                        }

                        // 다음 액티비티로
                        goNextActivity();

                    } catch (Exception e) {
                        showDontReceiveData(e);
                    }

                }
            }).start();
        }
    }

    public void upProgressValue() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pBar.setProgress(pBar.getProgress() + 1);
            }
        });
    }

    public void insertDate() {
        Long time = System.currentTimeMillis();
        String query = "insert into info (date) values ('" + time + "');";
        Constants.timeTableVersion = time;
        Constants.mDBManager.execWriteSQL(query);
    }

    public void showDontReceiveData(Exception e) {
        Constants.printLog(2, null, e);
        mDBManager.deleteAllData();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoadingActivity.this, R.string.loading_receive_error_toast_message, Toast.LENGTH_SHORT).show();
                LoadingActivity.this.finish();
            }
        });
    }

    public void goNextActivity() {

        upProgressValue();

        SharedPreferences sharedPreferences = Constants.getSharedPreferences();
        long fromDate = sharedPreferences.getLong(Constants.HOLIDAY_PERIOD_START, 0L);
        long toDate = sharedPreferences.getLong(Constants.HOLIDAY_PERIOD_END, 0L);

        Constants.printLog(1, "Vacation Date Start : " + fromDate, null);
        Constants.printLog(1, "Vacation Date End : " + toDate, null);

        if (fromDate != 0L && toDate != 0L) {
            Constants.vacationPeriodStart = fromDate;
            Constants.vacationPeriodEnd = toDate;

            long currentDate = System.currentTimeMillis();
            isHoliday = currentDate >= fromDate && currentDate <= toDate;
        }

        Constants.isRemoveAd = sharedPreferences.getBoolean(Constants.REMOVE_AD, false);
        Constants.isFreeUser = sharedPreferences.getBoolean(Constants.FREE_USER, false);

        Constants.printLog(1, "MyDeviceID : " + Constants.deviceID, null);
        Constants.printLog(1, "isHoliday : " + isHoliday, null);
        Constants.printLog(1, "isRemoveAd : " + Constants.isRemoveAd, null);
        Constants.printLog(1, "isFreeUser : " + Constants.isFreeUser, null);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                intent.putExtra("isHoliday", isHoliday);
                startActivity(intent);
                LoadingActivity.this.finish();
            }
        });
    }


    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        Constants.activity = this;
        Constants.context = this;
    }
}
