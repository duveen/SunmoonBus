package kr.o3selab.sunmoonbus.constant;

import android.content.SharedPreferences;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;

import kr.o3selab.sunmoonbus.activity.LoadingActivity;
import kr.o3selab.sunmoonbus.R;

public class GetHolidayDate implements Runnable {

    private LoadingActivity activity;

    public GetHolidayDate(LoadingActivity activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
        Document document;
        try {
            document = Jsoup.connect(API.getHolidayUrl()).get();
        } catch (Exception e) {
            activity.holidayStatus = ConstantsOld.DONTKNOW;
            return;
        }

        if (document == null) {
            activity.holidayStatus = ConstantsOld.DONTKNOW;
            return;
        }

        Elements elements = document.select("h3");
        String content = elements.get(0).text(); // 운행기간 : yyyy.mm.dd. ~ yyyy.mm.dd

        content = content.replace("운행기간", "").replace(":", "").replace(".", "").replace(" ", "").replace("~", "");

        if(content.length() != 16) {
            Toast.makeText(ConstantsOld.activity, R.string.loading_error_get_holiday_date, Toast.LENGTH_SHORT).show();
            activity.holidayStatus = ConstantsOld.DONTKNOW;
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        long fromDate;
        long toDate;

        try {
            fromDate = sdf.parse(content.substring(0, 8)).getTime();
            toDate = sdf.parse(content.substring(8, 16)).getTime() + (1000 * 24 * 60 * 60 - 1000);

            ConstantsOld.vacationPeriodStart = fromDate;
            ConstantsOld.vacationPeriodEnd = toDate;

            SharedPreferences.Editor editor = ConstantsOld.getEditor();
            editor.putLong(ConstantsOld.HOLIDAY_PERIOD_START, fromDate);
            editor.putLong(ConstantsOld.HOLIDAY_PERIOD_END, toDate);
            editor.commit();
        } catch (Exception e) {
            activity.holidayStatus = ConstantsOld.DONTKNOW;
            return;
        }

        long currentDate = System.currentTimeMillis();

        if (currentDate >= fromDate && currentDate <= toDate) {
            activity.holidayStatus = ConstantsOld.HOLIDAY;
        } else {
            activity.holidayStatus = ConstantsOld.NOHOLIDAY;
        }
    }
}