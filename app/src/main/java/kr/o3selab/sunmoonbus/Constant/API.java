package kr.o3selab.sunmoonbus.Constant;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Vector;

import kr.o3selab.sunmoonbus.Model.NetworkGroup;
import kr.o3selab.sunmoonbus.Model.TimeTableData;

public class API {

    public static final int SUCCESS = 1;
    public static final int HTTP_HANDLER_ERROR = -1;

    private static final int NO_SKIP_NUMBER = -1;

    // ============================================================
    //     학기 중 시간표
    // ============================================================

    // 평일 - 천안역/아산역
    private static final String WEEKDAY_NOHOLIDAY_CHEONAN_ASAN_STATION_URL = "http://lily.sunmoon.ac.kr/Page/About/About08_04_02_01_01.aspx";
    private static final int WEEKDAY_NOHOLIDAY_CHEONAN_ASAN_STATION_TABS = 6;
    private static final int WEEKDAY_NOHOLIDAY_CHEONAN_ASAN_STATION_SKIP = 3;
    private static final NetworkGroup WEEKDAY_NOHOLIDAY_CHEONAN_ASAN_STATION
            = new NetworkGroup(WEEKDAY_NOHOLIDAY_CHEONAN_ASAN_STATION_URL, WEEKDAY_NOHOLIDAY_CHEONAN_ASAN_STATION_TABS, WEEKDAY_NOHOLIDAY_CHEONAN_ASAN_STATION_SKIP);

    // 평일 - 천안터미널
    private static final String WEEKDAY_NOHOLIDAY_CHEONAN_TERMINAL_URL = "http://lily.sunmoon.ac.kr/Page/About/About08_04_02_01_02.aspx";
    private static final int WEEKDAY_NOHOLIDAY_CHEONAN_TERMINAL_TABS = 4;
    private static final int WEEKDAY_NOHOLIDAY_CHEONAN_TERMINAL_SKIP = 2;
    private static final NetworkGroup WEEKDAY_NOHOLIDAY_CHEONAN_TERMINAL
            = new NetworkGroup(WEEKDAY_NOHOLIDAY_CHEONAN_TERMINAL_URL, WEEKDAY_NOHOLIDAY_CHEONAN_TERMINAL_TABS, WEEKDAY_NOHOLIDAY_CHEONAN_TERMINAL_SKIP);

    // 평일 - 온양역/터미널
    private static final String WEEKDAY_NOHOLIDAY_ONYANG_STATION_TERMINAL_URL = "http://lily.sunmoon.ac.kr/Page/About/About08_04_02_01_03.aspx";
    private static final int WEEKDAY_NOHOLIDAY_ONYANG_STATION_TERMINAL_TABS = 5;
    private static final int WEEKDAY_NOHOLIDAY_ONYANG_STATION_TERMINAL_SKIP = NO_SKIP_NUMBER;
    private static final NetworkGroup WEEKDAY_NOHOLIDAY_ONYANG_STATION_TERMINAL
            = new NetworkGroup(WEEKDAY_NOHOLIDAY_ONYANG_STATION_TERMINAL_URL, WEEKDAY_NOHOLIDAY_ONYANG_STATION_TERMINAL_TABS, WEEKDAY_NOHOLIDAY_ONYANG_STATION_TERMINAL_SKIP);

    // 평일 - 천안캠퍼스
    private static final String WEEKDAY_NOHOLIDAY_CHEONAN_CAMPUS_URL = "http://lily.sunmoon.ac.kr/Page/About/About08_04_02_01_04.aspx";
    private static final int WEEKDAY_NOHOLIDAY_CHEONAN_CAMPUS_TABS = 5;
    private static final int WEEKDAY_NOHOLIDAY_CHEONAN_CAMPUS_SKIP = NO_SKIP_NUMBER;
    private static final NetworkGroup WEEKDAY_NOHOLIDAY_CHEONAN_CAMPUS
            = new NetworkGroup(WEEKDAY_NOHOLIDAY_CHEONAN_CAMPUS_URL, WEEKDAY_NOHOLIDAY_CHEONAN_CAMPUS_TABS, WEEKDAY_NOHOLIDAY_CHEONAN_CAMPUS_SKIP);

    // 평일 그룹
    public static NetworkGroup[] WEEKDAY_NOHOLIDAY = {
            WEEKDAY_NOHOLIDAY_CHEONAN_ASAN_STATION,
            WEEKDAY_NOHOLIDAY_CHEONAN_TERMINAL,
            WEEKDAY_NOHOLIDAY_ONYANG_STATION_TERMINAL,
            WEEKDAY_NOHOLIDAY_CHEONAN_CAMPUS
    };


    // 토요일/공휴일 - 천안역/아산역
    private static final String SATURDAY_NOHOLIDAY_CHEONAN_ASAN_STATION_URL = "http://lily.sunmoon.ac.kr/Page/About/About08_04_02_02_01.aspx";
    private static final int SATURDAY_NOHOLIDAY_CHEONAN_ASAN_STATION_TABS = 5;
    private static final int SATURDAY_NOHOLIDAY_CHEONAN_ASAN_STATION_SKIP = NO_SKIP_NUMBER;
    private static final NetworkGroup SATURDAY_NOHOLIDAY_CHEONAN_ASAN_STATION
            = new NetworkGroup(SATURDAY_NOHOLIDAY_CHEONAN_ASAN_STATION_URL, SATURDAY_NOHOLIDAY_CHEONAN_ASAN_STATION_TABS, SATURDAY_NOHOLIDAY_CHEONAN_ASAN_STATION_SKIP);

    // 토요일/공휴일 - 천안터미널
    private static final String SATURDAY_NOHOLIDAY_CHEONAN_TERMINAL_URL = "http://lily.sunmoon.ac.kr/Page/About/About08_04_02_02_02.aspx";
    private static final int SATURDAY_NOHOLIDAY_CHEONAN_TERMINAL_TABS = 3;
    private static final int SATURDAY_NOHOLIDAY_CHEONAN_TERMINAL_SKIP = NO_SKIP_NUMBER;
    private static final NetworkGroup SATURDAY_NOHOLIDAY_CHEONAN_TERMINAL
            = new NetworkGroup(SATURDAY_NOHOLIDAY_CHEONAN_TERMINAL_URL, SATURDAY_NOHOLIDAY_CHEONAN_TERMINAL_TABS, SATURDAY_NOHOLIDAY_CHEONAN_TERMINAL_SKIP);

    // 토요일/공휴일 그룹
    public static NetworkGroup[] SATURDAY_NOHOLIDAY = {
            SATURDAY_NOHOLIDAY_CHEONAN_ASAN_STATION,
            SATURDAY_NOHOLIDAY_CHEONAN_TERMINAL
    };


    // 일요일 - 천안역/아산역
    private static final String SUNDAY_NOHOLIDAY_CHEONAN_ASAN_STATION_URL = "http://lily.sunmoon.ac.kr/Page/About/About08_04_02_03_01.aspx";
    private static final int SUNDAY_NOHOLIDAY_CHEONAN_ASAN_STATION_TABS = 5;
    private static final int SUNDAY_NOHOLIDAY_CHEONAN_ASAN_STATION_SKIP = NO_SKIP_NUMBER;
    private static final NetworkGroup SUNDAY_NOHOLIDAY_CHEONAN_ASAN_STATION
            = new NetworkGroup(SUNDAY_NOHOLIDAY_CHEONAN_ASAN_STATION_URL, SUNDAY_NOHOLIDAY_CHEONAN_ASAN_STATION_TABS, SUNDAY_NOHOLIDAY_CHEONAN_ASAN_STATION_SKIP);


    // 일요일 - 천안터미널
    private static final String SUNDAY_NOHOLIDAY_CHEONAN_TERMINAL_URL = "http://lily.sunmoon.ac.kr/Page/About/About08_04_02_03_02.aspx";
    private static final int SUNDAY_NOHOLIDAY_CHEONAN_TERMINAL_TABS = 3;
    private static final int SUNDAY_NOHOLIDAY_CHEONAN_TERMINAL_SKIP = NO_SKIP_NUMBER;
    private static final NetworkGroup SUNDAY_NOHOLIDAY_CHEONAN_TERMINAL
            = new NetworkGroup(SUNDAY_NOHOLIDAY_CHEONAN_TERMINAL_URL, SUNDAY_NOHOLIDAY_CHEONAN_TERMINAL_TABS, SUNDAY_NOHOLIDAY_CHEONAN_TERMINAL_SKIP);

    // 일요일 그룹
    public static NetworkGroup[] SUNDAY_NOHOLIDAY = {
            SUNDAY_NOHOLIDAY_CHEONAN_ASAN_STATION,
            SUNDAY_NOHOLIDAY_CHEONAN_TERMINAL
    };

    // ============================================================
    //     방학 중 시간표
    // ============================================================

    // 평일 - 천안역/아산역
    private static final String WEEKDAY_HOLIDAY_CHEONAN_ASAN_STATION_URL = "http://lily.sunmoon.ac.kr/Page/About/About08_04_03_01_01.aspx";
    private static final int WEEKDAY_HOLIDAY_CHEONAN_ASAN_STATION_TABS = 6;
    private static final int WEEKDAY_HOLIDAY_CHEONAN_ASAN_STATION_SKIP = 5;
    private static final NetworkGroup WEEKDAY_HOLIDAY_CHEONAN_ASAN_STATION
            = new NetworkGroup(WEEKDAY_HOLIDAY_CHEONAN_ASAN_STATION_URL, WEEKDAY_HOLIDAY_CHEONAN_ASAN_STATION_TABS, WEEKDAY_HOLIDAY_CHEONAN_ASAN_STATION_SKIP);

    // 평일 - 천안터미널
    private static final String WEEKDAY_HOLIDAY_CHEONAN_TERMINAL_URL = "http://lily.sunmoon.ac.kr/Page/About/About08_04_03_01_02.aspx";
    private static final int WEEKDAY_HOLIDAY_CHEONAN_TERMINAL_TABS = 4;
    private static final int WEEKDAY_HOLIDAY_CHEONAN_TERMINAL_SKIP = 3;
    private static final NetworkGroup WEEKDAY_HOLIDAY_CHEONAN_TERMINAL
            = new NetworkGroup(WEEKDAY_HOLIDAY_CHEONAN_TERMINAL_URL, WEEKDAY_HOLIDAY_CHEONAN_TERMINAL_TABS, WEEKDAY_HOLIDAY_CHEONAN_TERMINAL_SKIP);

    // 평일 - 온양방면
    private static final String WEEKDAY_HOLIDAY_ONYANG_STATION_TERMINAL_URL = "http://lily.sunmoon.ac.kr/Page/About/About08_04_03_01_03.aspx";
    private static final int WEEKDAY_HOLIDAY_ONYANG_STATION_TERMINAL_TABS = 5;
    private static final int WEEKDAY_HOLIDAY_ONYANG_STATION_TERMINAL_SKIP = 4;
    private static final NetworkGroup WEEKDAY_HOLIDAY_ONYANG_STATION_TERMINAL
            = new NetworkGroup(WEEKDAY_HOLIDAY_ONYANG_STATION_TERMINAL_URL, WEEKDAY_HOLIDAY_ONYANG_STATION_TERMINAL_TABS, WEEKDAY_HOLIDAY_ONYANG_STATION_TERMINAL_SKIP);

    // 평일 - 천안캠퍼스
    private static final String WEEKDAY_HOLIDAY_CHEONAN_CAMPUS_URL = "http://lily.sunmoon.ac.kr/Page/About/About08_04_03_01_04.aspx";
    private static final int WEEKDAY_HOLIDAY_CHEONAN_CAMPUS_TABS = 5;
    private static final int WEEKDAY_HOLIDAY_CHEONAN_CAMPUS_SKIP = NO_SKIP_NUMBER;
    private static final NetworkGroup WEEKDAY_HOLIDAY_CHEONAN_CAMPUS
            = new NetworkGroup(WEEKDAY_HOLIDAY_CHEONAN_CAMPUS_URL, WEEKDAY_HOLIDAY_CHEONAN_CAMPUS_TABS, WEEKDAY_HOLIDAY_CHEONAN_CAMPUS_SKIP);

    // 평일 그룹
    public static final NetworkGroup[] WEEKDAY_HOLIDAY = {
            WEEKDAY_HOLIDAY_CHEONAN_ASAN_STATION,
            WEEKDAY_HOLIDAY_CHEONAN_TERMINAL,
            WEEKDAY_HOLIDAY_ONYANG_STATION_TERMINAL,
            WEEKDAY_HOLIDAY_CHEONAN_CAMPUS
    };

    // 휴일 - 천안역/아산역
    private static final String WEEKEND_HOLIDAY_CHEONAN_ASAN_STATION_URL = "http://lily.sunmoon.ac.kr/Page/About/About08_04_03_02_01.aspx";
    private static final int WEEKEND_HOLIDAY_CHEONAN_ASAN_STATION_TABS = 6;
    private static final int WEEKEND_HOLIDAY_CHEONAN_ASAN_STATION_SKIP = 5;
    private static final NetworkGroup WEEKEND_HOLIDAY_CHEONAN_ASAN_STATION
            = new NetworkGroup(WEEKEND_HOLIDAY_CHEONAN_ASAN_STATION_URL, WEEKEND_HOLIDAY_CHEONAN_ASAN_STATION_TABS, WEEKEND_HOLIDAY_CHEONAN_ASAN_STATION_SKIP);

    // 휴일 - 천안터미널
    private static final String WEEKEND_HOLIDAY_CHEONAN_TERMINAL_URL = "http://lily.sunmoon.ac.kr/Page/About/About08_04_03_02_02.aspx";
    private static final int WEEKEND_HOLIDAY_CHEONAN_TERMINAL_TABS = 4;
    private static final int WEEKEND_HOLIDAY_CHEONAN_TERMINAL_SKIP = 3;
    private static final NetworkGroup WEEKEND_HOLIDAY_CHEONAN_TERMINAL
            = new NetworkGroup(WEEKEND_HOLIDAY_CHEONAN_TERMINAL_URL, WEEKEND_HOLIDAY_CHEONAN_TERMINAL_TABS, WEEKEND_HOLIDAY_CHEONAN_TERMINAL_SKIP);

    // 휴일 그룹
    public static final NetworkGroup[] SATURDAY_HOLIDAY = {
            WEEKEND_HOLIDAY_CHEONAN_ASAN_STATION,
            WEEKEND_HOLIDAY_CHEONAN_TERMINAL
    };

    public static String getHolidayUrl() {
        return WEEKDAY_HOLIDAY_CHEONAN_TERMINAL_URL;
    }

    public static TimeTableData getData(NetworkGroup networkGroup) {

        String url = networkGroup.getUrl();
        int numbOfTabs = networkGroup.getTabs();
        int skipNumber = networkGroup.getSkip();

        Vector<String[]> datas = new Vector<>();
        Document doc;

        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            return new TimeTableData(HTTP_HANDLER_ERROR, null);
        }

        Elements contents = doc.select("div.table01 table tr td");

        int flagOfTabs = 0;
        int arrayNumber = 0;

        int compareTabs;

        if (skipNumber != 0) compareTabs = numbOfTabs - 1;
        else compareTabs = numbOfTabs;


        String[] data = null;
        for (Element content : contents) {

            if (flagOfTabs % numbOfTabs == 0) {
                flagOfTabs = 0;
                arrayNumber = 0;
                if (skipNumber != -1) {
                    data = new String[numbOfTabs - 1];
                } else {
                    data = new String[numbOfTabs];
                }
            }

            if (flagOfTabs == skipNumber) {
                flagOfTabs++;
                continue;
            }

            String temp = content.text();

            assert data != null;
            data[arrayNumber++] = temp;

            flagOfTabs++;

            if (arrayNumber == compareTabs) datas.add(data);

        }

        /*int printLog = 1;

        for (String[] times : datas) {
            StringBuffer buffer = new StringBuffer();

            buffer.append(printLog++).append(", ");

            for (String time : times) {
                buffer.append(time).append(", ");
            }

            Log.d("SMBus", buffer.toString());
        }*/

        return new TimeTableData(SUCCESS, datas);
    }
}
