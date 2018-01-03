package kr.o3selab.sunmoonbus.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import kr.o3selab.sunmoonbus.constant.Constants;
import kr.o3selab.sunmoonbus.model.BackPressed;
import kr.o3selab.sunmoonbus.model.SlidingTabLayout;
import kr.o3selab.sunmoonbus.model.ViewPagerAdapter;
import kr.o3selab.sunmoonbus.R;

public class MainActivity extends AppCompatActivity {

    static final int MENU_WEEKDAY = Menu.FIRST;
    static final int MENU_SATURDAY = MENU_WEEKDAY + 1;
    static final int MENU_SUNDAY = MENU_WEEKDAY + 2;
    static final int MENU_WEEKEND = MENU_WEEKDAY + 3;
    static final int MENU_SETUP = MENU_WEEKDAY + 4;
    public static String APP_ID;
    boolean isHoliday;
    boolean isLoaded;
    Toolbar mToolbar;
    ViewPager mViewPager;
    ViewPagerAdapter mViewPagerAdapter;
    SlidingTabLayout mSlidingTabLayout;
    int numbOfTabs;

    BackPressed mBackPressed;

    InterstitialAd mInterstitialAd;
    ShowAdThread mShowAdThread;
    ValueEventListener noticeEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ArrayList<HashMap> noticeList = (ArrayList<HashMap>) dataSnapshot.getValue();
            if (noticeList != null) {
                for (HashMap map : noticeList) {
                    String title = (String) map.get("title");
                    String body = (String) map.get("body");
                    Boolean isLink = (Boolean) map.get("isLink");

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                            .setTitle(title)
                            .setMessage(body)
                            .setCancelable(true);

                    if (isLink) {
                        final String link = (String) map.get("link");
                        builder.setPositiveButton(R.string.main_notice_move, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
                            }
                        });
                        builder.setPositiveButton(R.string.main_notice_cancel, null);
                    } else {
                        builder.setPositiveButton(R.string.main_notice_ok, null);
                    }

                    builder.show();
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    View.OnClickListener snackBarListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting Default Data
        Constants.printLog(1, "Start MainActivity", null);

        Constants.activity = this;
        Constants.context = this;

        // Creating The Toolbar and setting it as the Toolbar for the activity
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        // Handling Holiday
        isHoliday = getIntent().getBooleanExtra("isHoliday", false);
        if (isHoliday) Toast.makeText(this, R.string.main_show_holiday, Toast.LENGTH_SHORT).show();

        isLoaded = true;

        // Handling BackpressedButton
        mBackPressed = new BackPressed(this);

        // Handling Admob AD
        APP_ID = getString(R.string.admob_ad_id);

        boolean isSkip = compareDate(Constants.getSharedPreferences().getLong(Constants.AD_ALERT_SKIP, 0L));
        if (!Constants.isRemoveAd && !Constants.isFreeUser && isSkip) {

            Constants.printLog(1, "Initialize Add", null);
            MobileAds.initialize(this, APP_ID);

            // Admob Ad
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getString(R.string.admob_ad_fullsize));
            requestNewInterstitial();
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    showRemoveAdAlert();
                }
            });

            mShowAdThread = new ShowAdThread();
            mShowAdThread.start();
        }

        // inital UI
        initalization();

        // Handling Notice
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("notice");

        query.addListenerForSingleValueEvent(noticeEventListener);
    }

    private void initalization() {
        String[] weekDay = {"일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"};

        Calendar cal = Calendar.getInstance();
        int num = cal.get(Calendar.DAY_OF_WEEK) - 1;
        String today = weekDay[num];

        if (!isHoliday) {
            switch (today) {
                case "토요일":
                    setSaturdayDataWithoutHoliday();
                    break;
                case "일요일":
                    setSundayDataWithoutHoliday();
                    break;
                default:
                    setWeekdayDataWithoutHoliday();
                    break;
            }
        } else {
            if (today.equals("토요일") || today.equals("일요일"))
                setWeekendDataWithHoliday();
            else
                setWeekdayDataWithHoliday();
        }
    }

    public void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("FD06408E72344099D81A33F2E6600554")
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    public void showRemoveAdAlert() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.main_ad_alert_title)
                .setMessage(R.string.main_ad_alert_message)
                .setPositiveButton(R.string.main_ad_alert_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainActivity.this, SetupActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.main_ad_alert_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences.Editor editor = Constants.getEditor();
                        editor.putLong(Constants.AD_ALERT_SKIP, System.currentTimeMillis());
                        editor.commit();
                    }
                })
                .show();

    }

    public boolean compareDate(long compareDate) {
        final long ONE_DAY = 1000 * 60 * 60 * 24;
        long currentDate = System.currentTimeMillis();

        return compareDate + ONE_DAY < currentDate;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0, MENU_WEEKDAY, Menu.NONE, R.string.main_menu_weekday);
        if (!isHoliday) {
            menu.add(0, MENU_SATURDAY, Menu.NONE, R.string.main_menu_saturday_noholiday);
            menu.add(0, MENU_SUNDAY, Menu.NONE, R.string.main_menu_sunday);
        } else {
            menu.add(0, MENU_WEEKEND, Menu.NONE, R.string.main_menu_saturday_holiday);
        }
        menu.add(0, MENU_SETUP, Menu.NONE, R.string.main_menu_setting);

        return super.onPrepareOptionsMenu(menu);
    }

    public void setWeekdayDataWithoutHoliday() {
        Constants.tabTitles = new CharSequence[]{getString(R.string.region_cheonan_asan_station), getString(R.string.region_cheonan_terminal), getString(R.string.region_onyang_station_terminal), getString(R.string.region_cheonan_campus)};
        numbOfTabs = Constants.tabTitles.length;
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), Constants.tabTitles, numbOfTabs, Constants.WEEKDAY_NOHOLIDAY);
        setUI();

        Snackbar.make(getWindow().getDecorView().getRootView(), getString(R.string.main_snackbar_weekday), 3000).setAction(R.string.main_snackbar_ok, snackBarListener).setActionTextColor(Color.rgb(56, 142, 60)).show();
    }

    public void setSaturdayDataWithoutHoliday() {
        Constants.tabTitles = new CharSequence[]{getString(R.string.region_cheonan_asan_station), getString(R.string.region_cheonan_terminal)};
        numbOfTabs = Constants.tabTitles.length;
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), Constants.tabTitles, numbOfTabs, Constants.SATURDAY_NOHOLIDAY);
        setUI();

        Snackbar.make(getWindow().getDecorView().getRootView(), getString(R.string.main_snackbar_saturday), 3000).setAction(R.string.main_snackbar_ok, snackBarListener).setActionTextColor(Color.rgb(56, 142, 60)).show();
    }

    public void setSundayDataWithoutHoliday() {
        Constants.tabTitles = new CharSequence[]{getString(R.string.region_cheonan_asan_station), getString(R.string.region_cheonan_terminal)};
        numbOfTabs = Constants.tabTitles.length;
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), Constants.tabTitles, numbOfTabs, Constants.SUNDAY_NOHOLIDAY);
        setUI();

        Snackbar.make(getWindow().getDecorView().getRootView(), getString(R.string.main_snackbar_sunday), 3000).setAction(R.string.main_snackbar_ok, snackBarListener).setActionTextColor(Color.rgb(56, 142, 60)).show();
    }

    public void setWeekdayDataWithHoliday() {
        Constants.tabTitles = new CharSequence[]{getString(R.string.region_cheonan_asan_station), getString(R.string.region_cheonan_terminal), getString(R.string.region_onyang_station_terminal), getString(R.string.region_cheonan_campus)};
        numbOfTabs = Constants.tabTitles.length;
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), Constants.tabTitles, numbOfTabs, Constants.WEEKDAY_HOLIDAY);
        setUI();

        Snackbar.make(getWindow().getDecorView().getRootView(), getString(R.string.main_snackbar_weekday), 3000).setAction(R.string.main_snackbar_ok, snackBarListener).setActionTextColor(Color.rgb(56, 142, 60)).show();
    }

    public void setWeekendDataWithHoliday() {
        Constants.tabTitles = new CharSequence[]{getString(R.string.region_cheonan_asan_station), getString(R.string.region_cheonan_terminal)};
        numbOfTabs = Constants.tabTitles.length;
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), Constants.tabTitles, numbOfTabs, Constants.WEEKEND_HOLIDAY);
        setUI();

        Snackbar.make(getWindow().getDecorView().getRootView(), getString(R.string.main_snackbar_weekend), 3000).setAction(R.string.main_snackbar_ok, snackBarListener).setActionTextColor(Color.rgb(56, 142, 60)).show();
    }

    public void setUI() {
        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.

        // Assigning ViewPager View and setting the adapter
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(numbOfTabs);

        // Assiging the Sliding Tab Layout View
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.tabs);
        mSlidingTabLayout.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case MENU_WEEKDAY:
                if (!isHoliday) setWeekdayDataWithoutHoliday();
                else setWeekdayDataWithHoliday();
                return true;

            case MENU_SATURDAY:
                setSaturdayDataWithoutHoliday();
                return true;

            case MENU_SUNDAY:
                setSundayDataWithoutHoliday();
                return true;

            case MENU_WEEKEND:
                setWeekendDataWithHoliday();
                return true;

            case MENU_SETUP:
                startActivity(new Intent(MainActivity.this, SetupActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Constants.activity = this;
        Constants.context = this;
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mShowAdThread != null) {
            if (mShowAdThread.getState().equals(Thread.State.TIMED_WAITING)) {
                mShowAdThread.interrupt();
            }
        }
    }

    @Override
    public void onBackPressed() {
        mBackPressed.onBackPressed();
    }

    private class ShowAdThread extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                return;
            }

            while (true) {
                if (isLoaded) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mInterstitialAd.isLoaded()) {
                                isLoaded = false;
                                mInterstitialAd.show();
                            }
                        }
                    });
                }

                if (!isLoaded) break;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
}
