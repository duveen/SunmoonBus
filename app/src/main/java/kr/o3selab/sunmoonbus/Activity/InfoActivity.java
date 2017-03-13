package kr.o3selab.sunmoonbus.Activity;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import kr.o3selab.sunmoonbus.Constant.Constants;
import kr.o3selab.sunmoonbus.R;

public class InfoActivity extends AppCompatActivity {

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        mToolbar = (Toolbar) findViewById(R.id.info_toolbar);
        setSupportActionBar(mToolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.setting_menu_about_application);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

        TextView infoApplicationVersionView = (TextView) findViewById(R.id.info_application_version);
        infoApplicationVersionView.setText(infoApplicationVersionView.getText().toString() + " " + Constants.appVersion);

        TextView timeTableVersionView = (TextView) findViewById(R.id.info_timetable_version);
        timeTableVersionView.setText(timeTableVersionView.getText() + " " + sdf.format(new Date(Constants.timeTableVersion)));

        TextView vacationPeriodView = (TextView) findViewById(R.id.info_vacation_period);
        vacationPeriodView.setText(vacationPeriodView.getText() + " " + sdf.format(new Date(Constants.vacationPeriodStart)) + " ~ " + sdf.format(new Date(Constants.vacationPeriodEnd)));
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

        Constants.activity = this;
        Constants.context = this;
    }
}
