package kr.o3selab.sunmoonbus.Constant;

import android.content.SharedPreferences;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

import kr.o3selab.sunmoonbus.LoadingActivity;
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
            activity.holidayStatus = Constants.DONTKNOW;
            return;
        }

        if (document == null) {
            activity.holidayStatus = Constants.DONTKNOW;
            return;
        }

        Elements elements = document.select("h3");
        String content = elements.get(0).text(); // 운행기간 : yyyy.mm.dd. ~ yyyy.mm.dd

        content = content.replace("운행기간", "");
        content = content.replace(":", "");

        StringTokenizer str = new StringTokenizer(content, ".~ ");
        if (str.countTokens() != 6) {
            Toast.makeText(Constants.activity, R.string.loading_error_get_holiday_date, Toast.LENGTH_SHORT).show();
            activity.holidayStatus = Constants.DONTKNOW;
            return;
        }

        String fromYear = str.nextToken();
        String fromMonth = str.nextToken();
        String fromDateOfMonth = str.nextToken();
        String toYear = str.nextToken();
        String toMonth = str.nextToken();
        String toDateOfMonth = str.nextToken();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        long fromDate;
        long toDate;

        try {
            fromDate = sdf.parse(fromYear + fromMonth + fromDateOfMonth).getTime();
            toDate = sdf.parse(toYear + toMonth + toDateOfMonth).getTime() + (1000 * 24 * 60 * 60 - 1000);

            Constants.vacationPeriodStart = fromDate;
            Constants.vacationPeriodEnd = toDate;

            SharedPreferences.Editor editor = Constants.getEditor();
            editor.putLong(Constants.HOLIDAY_PERIOD_START, fromDate);
            editor.putLong(Constants.HOLIDAY_PERIOD_END, toDate);
            editor.commit();

        } catch (Exception e) {
            activity.holidayStatus = Constants.DONTKNOW;
            return;
        }

        long currentDate = System.currentTimeMillis();

        if (currentDate >= fromDate && currentDate <= toDate) {
            activity.holidayStatus = Constants.HOLIDAY;
        } else {
            activity.holidayStatus = Constants.NOHOLIDAY;
        }
    }
}