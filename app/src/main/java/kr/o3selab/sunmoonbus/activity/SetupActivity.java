package kr.o3selab.sunmoonbus.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import kr.o3selab.sunmoonbus.R;
import kr.o3selab.sunmoonbus.constant.ConstantsOld;
import kr.o3selab.sunmoonbus.util.IabBroadcastReceiver;
import kr.o3selab.sunmoonbus.util.IabHelper;
import kr.o3selab.sunmoonbus.util.IabResult;
import kr.o3selab.sunmoonbus.util.Inventory;
import kr.o3selab.sunmoonbus.util.Purchase;

public class SetupActivity extends BaseActivity implements IabBroadcastReceiver.IabBroadcastListener {

    public static final String TAG = "SMBus";

    Toolbar mToolbar;

    int setVFlag = 0;

    boolean mIsRemoveAd;
    View removeADView;
    IabHelper mHelper;
    IabBroadcastReceiver mBroadcastReceiver;
    static final String REMOVE_AD = "remove_ad";
    static final String REMOVE_AD_PAYLOAD = "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ";
    static final int RC_REQUEST = 10001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        if (!ConstantsOld.isRemoveAd && !ConstantsOld.isFreeUser) {
            String base64EncodedPublicKey = getString(R.string.base_64_key);

            // Create the helper, passing it our context and the public key to verify signatures with
            ConstantsOld.printLog(1, "Creating IAB helper.", null);
            mHelper = new IabHelper(this, base64EncodedPublicKey);

            // enable debug logging (for a production application, you should set this to false).
            mHelper.enableDebugLogging(true);

            // Start setup. This is asynchronous and the specified listener
            // will be called once setup completes.
            ConstantsOld.printLog(1, "Starting setup.", null);
            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                @Override
                public void onIabSetupFinished(IabResult result) {
                    ConstantsOld.printLog(1, "Setup finished.", null);

                    if (!result.isSuccess()) {
                        ConstantsOld.printLog(2, "Problem setting up in-app billing: " + result, null);
                        return;
                    }

                    if (mHelper == null) return;

                    mBroadcastReceiver = new IabBroadcastReceiver(SetupActivity.this);
                    IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                    registerReceiver(mBroadcastReceiver, broadcastFilter);

                    ConstantsOld.printLog(1, "Setup successful. Querying inventory.", null);
                    try {
                        mHelper.queryInventoryAsync(mGotInventoryListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        ConstantsOld.printLog(2, "Error querying inventory. Another async operation in progress.", null);
                    }
                }
            });
        }

        mToolbar = (Toolbar) findViewById(R.id.setup_toolbar);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.main_menu_setting);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }

        // 시간표 다시 불러오기
        View reloadTimeTableView = findViewById(R.id.setting_load_data);
        reloadTimeTableView.setOnClickListener(reloadOnClickListener);

        // 방학기간 수동 설정하기
        View settingVacationPeriodView = findViewById(R.id.setting_load_holiday_period);
        settingVacationPeriodView.setOnClickListener(setVacationPeriodListener);

        // 광고 제거하기
        removeADView = findViewById(R.id.setting_remove_ad);
        removeADView.setOnClickListener(removeAdListener);

        View reportWithKakaoView = findViewById(R.id.setting_report_kakao);
        reportWithKakaoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://open.kakao.com/o/sE1ziSs"));
                startActivity(intent);
            }
        });


        View aboutApplicationView = findViewById(R.id.setting_version);
        aboutApplicationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SetupActivity.this, InfoActivity.class));
            }
        });
    }

    View.OnClickListener reloadOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new AlertDialog.Builder(SetupActivity.this)
                    .setTitle(R.string.setup_reloadtime_alert_title)
                    .setMessage(R.string.setup_reloadtime_alert_message)
                    .setPositiveButton(R.string.setup_reloadtime_alert_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ConstantsOld.mDBManager.deleteAllData();
                            Toast.makeText(SetupActivity.this, R.string.setup_reloadtime_toast_message, Toast.LENGTH_SHORT).show();
                            finishAffinity();
                        }
                    })
                    .setNegativeButton(R.string.setup_reloadtime_alert_no, null)
                    .show();
        }
    };

    View.OnClickListener setVacationPeriodListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String[] items = {getString(R.string.setting_vacation_start_date), getString(R.string.setting_vacation_end_date)};

            new AlertDialog.Builder(SetupActivity.this)
                    .setTitle(R.string.setting_vacation_title)
                    .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setVFlag = i;
                        }
                    })
                    .setNegativeButton(R.string.setting_vacation_positive, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                            switch (setVFlag) {
                                case 0:
                                    StringTokenizer str = new StringTokenizer(sdf.format(new Date(ConstantsOld.vacationPeriodStart)), ".");

                                    int year = Integer.parseInt(str.nextToken());
                                    int month = Integer.parseInt(str.nextToken()) - 1;
                                    int date = Integer.parseInt(str.nextToken());

                                    DatePickerDialog datePickerDialog = new DatePickerDialog(SetupActivity.this, new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                                            try {
                                                long time = sdf.parse(year + "." + (month + 1) + "." + date).getTime();

                                                if (time > ConstantsOld.vacationPeriodEnd) {
                                                    Toast.makeText(SetupActivity.this, R.string.setting_vacation_error_toast_start, Toast.LENGTH_SHORT).show();
                                                    return;
                                                }

                                                ConstantsOld.vacationPeriodStart = time;
                                                SharedPreferences.Editor editor = ConstantsOld.getEditor();
                                                editor.putLong(ConstantsOld.HOLIDAY_PERIOD_START, time);
                                                editor.commit();

                                                Toast.makeText(SetupActivity.this, R.string.setting_vacation_ok_toast, Toast.LENGTH_SHORT).show();
                                            } catch (ParseException ignored) {

                                            }

                                        }
                                    }, year, month, date);
                                    datePickerDialog.show();
                                    break;

                                case 1:
                                    StringTokenizer str2 = new StringTokenizer(sdf.format(new Date(ConstantsOld.vacationPeriodEnd)), ".");

                                    int year2 = Integer.parseInt(str2.nextToken());
                                    int month2 = Integer.parseInt(str2.nextToken()) - 1;
                                    int date2 = Integer.parseInt(str2.nextToken());

                                    DatePickerDialog datePickerDialog2 = new DatePickerDialog(SetupActivity.this, new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                                            try {
                                                long time = sdf.parse(year + "." + (month + 1) + "." + date).getTime();

                                                if (time < ConstantsOld.vacationPeriodStart) {
                                                    Toast.makeText(SetupActivity.this, R.string.setting_vacation_error_toast_end, Toast.LENGTH_SHORT).show();
                                                    return;
                                                }

                                                ConstantsOld.vacationPeriodEnd = time + (1000 * 24 * 60 * 60 - 1000);
                                                SharedPreferences.Editor editor = ConstantsOld.getEditor();
                                                editor.putLong(ConstantsOld.HOLIDAY_PERIOD_END, time);
                                                editor.commit();

                                                Toast.makeText(SetupActivity.this, R.string.setting_vacation_ok_toast, Toast.LENGTH_SHORT).show();
                                            } catch (ParseException ignored) {

                                            }

                                        }
                                    }, year2, month2, date2);
                                    datePickerDialog2.show();
                                    break;
                            }
                        }
                    })
                    .show();
        }
    };

    View.OnClickListener removeAdListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!ConstantsOld.isRemoveAd && !ConstantsOld.isFreeUser) {
                startPurchaseAdProcess();
            } else {
                new AlertDialog.Builder(SetupActivity.this)
                        .setTitle(R.string.setup_removead_alert_title)
                        .setMessage(R.string.setup_removead_alert_message)
                        .setPositiveButton(R.string.setup_removead_alert_button, null)
                        .show();
            }
        }
    };


    public void startPurchaseAdProcess() {
        try {
            ConstantsOld.printLog(1, "Launching purchase flow for remove ad.", null);
            String payload = REMOVE_AD_PAYLOAD;

            try {
                mHelper.launchPurchaseFlow(SetupActivity.this, REMOVE_AD, RC_REQUEST, mPurchaseFinishedListener, payload);
            } catch (IabHelper.IabAsyncInProgressException e) {
                ConstantsOld.printLog(2, "Error launching purchase flow. Another async operation in progress.", null);
                Toast.makeText(this, R.string.setup_removead_error_toast_message, Toast.LENGTH_SHORT).show();
                this.finish();
            }

        } catch (Exception e) {
            ConstantsOld.printLog(2, null, e);
        }
    }

    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                ConstantsOld.printLog(2, "Failed to query inventory: " + result, null);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            // Do we have the premium upgrade?
            Purchase removeAd = inventory.getPurchase(REMOVE_AD);
            mIsRemoveAd = (removeAd != null && verifyDeveloperPayload(removeAd, REMOVE_AD_PAYLOAD));
            Log.d(TAG, "User is " + (mIsRemoveAd ? "REMOVE AD" : "NOT REMOVE AD"));

            if (mIsRemoveAd) {
                SharedPreferences.Editor editor = ConstantsOld.getEditor();
                editor.putBoolean(ConstantsOld.REMOVE_AD, true);
                editor.commit();

                removeADView.setClickable(false);
                removeADView.setEnabled(false);
            }
        }
    };

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            ConstantsOld.printLog(1, "Purchase finished: " + result + ", purchase: " + purchase.getOrderId(), null);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                ConstantsOld.printLog(2, "Error purchasing: " + result, null);
                return;
            }
            if (!verifyDeveloperPayload(purchase, REMOVE_AD_PAYLOAD)) {
                ConstantsOld.printLog(2, "Error purchasing. Authenticity verification failed.", null);
                return;
            }

            ConstantsOld.printLog(3, "Purchase successful.", null);
            if (purchase.getSku().equals(REMOVE_AD)) {
                ConstantsOld.printLog(3, "Purchase is remove ad package.", null);

                SharedPreferences.Editor editor = ConstantsOld.getEditor();
                editor.putBoolean(ConstantsOld.REMOVE_AD, true);
                editor.commit();

                ConstantsOld.isRemoveAd = true;
                removeADView.setEnabled(false);
                removeADView.setClickable(false);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        ConstantsOld.activity = this;
        ConstantsOld.context = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // very important:
        if (mBroadcastReceiver != null) {
            ConstantsOld.printLog(1, "unregisterReciver", null);
            unregisterReceiver(mBroadcastReceiver);
        }

        // very important:
        if (mHelper != null) {
            ConstantsOld.printLog(1, "Destroing Helper", null);
            mHelper.disposeWhenFinished();
            mHelper = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    boolean verifyDeveloperPayload(Purchase p, String userPayload) {
        String payload = p.getDeveloperPayload();
        return userPayload.equals(payload);
    }

    @Override
    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            Log.e(TAG, "Error querying inventory. Another async operation in progress.");
        }
    }
}
