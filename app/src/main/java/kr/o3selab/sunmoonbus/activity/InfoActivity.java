package kr.o3selab.sunmoonbus.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import kr.o3selab.sunmoonbus.R;
import kr.o3selab.sunmoonbus.constant.ConstantsOld;

public class InfoActivity extends BaseActivity {

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        mToolbar = (Toolbar) findViewById(R.id.info_toolbar);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.setting_menu_about_application);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

        TextView infoApplicationVersionView = (TextView) findViewById(R.id.info_application_version);
        infoApplicationVersionView.setText(infoApplicationVersionView.getText().toString() + " " + ConstantsOld.appVersion);

        TextView timeTableVersionView = (TextView) findViewById(R.id.info_timetable_version);
        timeTableVersionView.setText(timeTableVersionView.getText() + " " + sdf.format(new Date(ConstantsOld.timeTableVersion)));

        TextView vacationPeriodView = (TextView) findViewById(R.id.info_vacation_period);
        vacationPeriodView.setText(vacationPeriodView.getText() + " " + sdf.format(new Date(ConstantsOld.vacationPeriodStart)) + " ~ " + sdf.format(new Date(ConstantsOld.vacationPeriodEnd)));
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

    @Override
    protected void onResume() {
        super.onResume();

        ConstantsOld.activity = this;
        ConstantsOld.context = this;
    }
}