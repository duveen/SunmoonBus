package kr.o3selab.sunmoonbus.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {

    public final static String WEEKDAY_NOHOLIDAY_CHEONAN_ASAN_STATION = "wncas";
    public final static String WEEKDAY_NOHOLIDAY_CHEONAN_TERMINAL = "wnct";
    public final static String WEEKDAY_NOHOLIDAY_ONYANG_STATION_TERMINAL = "wnost";
    public final static String WEEKDAY_NOHOLIDAY_CHEONAN_CAMPUS = "wncc";

    public final static int[] WEEKDAY_NOHOLIDAY_GROUP_NUM = {5, 3, 5, 5};
    public final static String[] WEEKDAY_NOHOLIDAY_GROUP = {
            WEEKDAY_NOHOLIDAY_CHEONAN_ASAN_STATION,
            WEEKDAY_NOHOLIDAY_CHEONAN_TERMINAL,
            WEEKDAY_NOHOLIDAY_ONYANG_STATION_TERMINAL,
            WEEKDAY_NOHOLIDAY_CHEONAN_CAMPUS
    };

    public final static String SATURDAY_NOHOLIDAY_CHEONAN_ASAN_STATION = "sncas";
    public final static String SATURDAY_NOHOLIDAY_CHEONAN_TERMINAL = "snct";

    public final static int[] SATURDAY_NOHOLIDAY_GROUP_NUM = {5, 3};
    public final static String[] SATURDAY_NOHOLIDAY_GROUP = {
            SATURDAY_NOHOLIDAY_CHEONAN_ASAN_STATION,
            SATURDAY_NOHOLIDAY_CHEONAN_TERMINAL
    };

    public final static String SUNDAY_NOHOLIDAY_CHEONAN_ASAN_STATION = "uncas";
    public final static String SUNDAY_NOHOLIDAY_CHEONAN_TERMINAL = "uncc";

    public final static int[] SUNDAY_NOHOLIDAY_GROUP_NUM = {5, 3};
    public final static String[] SUNDAY_NOHOLIDAY_GROUP = {
            SUNDAY_NOHOLIDAY_CHEONAN_ASAN_STATION,
            SUNDAY_NOHOLIDAY_CHEONAN_TERMINAL
    };

    public final static String WEEKDAY_HOLIDAY_CHEONAN_ASAN_STATION = "whcas";
    public final static String WEEKDAY_HOLIDAY_CHEONAN_TERMINAL = "whct";
    public final static String WEEKDAY_HOLIDAY_ONYANG_STATION_TERMINAL = "whost";
    public final static String WEEKDAY_HOLIDAY_CHEONAN_CAMPUS = "whcc";

    public final static int[] WEEKDAY_HOLIDAY_GROUP_NUM = {5, 3, 4, 5};
    public final static String[] WEEKDAY_HOLIDAY_GROUP = {
            WEEKDAY_HOLIDAY_CHEONAN_ASAN_STATION,
            WEEKDAY_HOLIDAY_CHEONAN_TERMINAL,
            WEEKDAY_HOLIDAY_ONYANG_STATION_TERMINAL,
            WEEKDAY_HOLIDAY_CHEONAN_CAMPUS
    };

    public final static String WEEKEND_HOLIDAY_CHEONAN_ASAN_STATION = "shcas";
    public final static String WEEKEND_HOLIDAY_CHEONAN_TERMINAL = "shct";

    public final static int[] WEEKEND_HOLIDAY_GROUP_NUM = { 5, 3 };
    public final static String[] WEEKEND_HOLIDAY_GROUP = {
            WEEKEND_HOLIDAY_CHEONAN_ASAN_STATION,
            WEEKEND_HOLIDAY_CHEONAN_TERMINAL
    };

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE info(date TEXT)");

        db.execSQL(createTable(WEEKDAY_NOHOLIDAY_CHEONAN_ASAN_STATION, 5));
        db.execSQL(createTable(WEEKDAY_NOHOLIDAY_CHEONAN_TERMINAL, 3));
        db.execSQL(createTable(WEEKDAY_NOHOLIDAY_ONYANG_STATION_TERMINAL, 5));
        db.execSQL(createTable(WEEKDAY_NOHOLIDAY_CHEONAN_CAMPUS, 5));

        db.execSQL(createTable(SATURDAY_NOHOLIDAY_CHEONAN_ASAN_STATION, 5));
        db.execSQL(createTable(SATURDAY_NOHOLIDAY_CHEONAN_TERMINAL, 3));

        db.execSQL(createTable(SUNDAY_NOHOLIDAY_CHEONAN_ASAN_STATION, 5));
        db.execSQL(createTable(SUNDAY_NOHOLIDAY_CHEONAN_TERMINAL, 3));

        db.execSQL(createTable(WEEKDAY_HOLIDAY_CHEONAN_ASAN_STATION, 5));
        db.execSQL(createTable(WEEKDAY_HOLIDAY_CHEONAN_TERMINAL, 3));
        db.execSQL(createTable(WEEKDAY_HOLIDAY_ONYANG_STATION_TERMINAL, 4));
        db.execSQL(createTable(WEEKDAY_HOLIDAY_CHEONAN_CAMPUS, 5));

        db.execSQL(createTable(WEEKEND_HOLIDAY_CHEONAN_ASAN_STATION, 5));
        db.execSQL(createTable(WEEKEND_HOLIDAY_CHEONAN_TERMINAL, 3));
    }

    private String createTable(String tableName, int dataCount) {
        switch (dataCount) {
            case 3:
                return "CREATE TABLE " + tableName + "(num INTEGER PRIMARY KEY AUTOINCREMENT, data1 TEXT, data2 TEXT, data3 TEXT);";
            case 4:
                return "CREATE TABLE " + tableName + "(num INTEGER PRIMARY KEY AUTOINCREMENT, data1 TEXT, data2 TEXT, data3 TEXT, data4 TEXT);";
            case 5:
                return "CREATE TABLE " + tableName + "(num INTEGER PRIMARY KEY AUTOINCREMENT, data1 TEXT, data2 TEXT, data3 TEXT, data4 TEXT, data5 TEXT);";
            default:
                return null;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor execReadSQL(String query) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(query, null);
    }

    public void execWriteSQL(String query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    public void insertDataNum3(String tableName, String[] datas) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "INSERT INTO " + tableName + " (data1, data2, data3) VALUES ('" + datas[0] + "', '" + datas[1] + "', '" + datas[2] + "');";
        db.execSQL(query);
        db.close();
    }

    public void insertDataNum4(String tableName, String[] datas) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "INSERT INTO " + tableName + " (data1, data2, data3, data4) VALUES ('" + datas[0] + "', '" + datas[1] + "', '" + datas[2] + "', '" + datas[3] + "');";
        db.execSQL(query);
        db.close();
    }

    public void insertDataNum5(String tableName, String[] datas) {
        SQLiteDatabase db = getWritableDatabase();
        String query ="INSERT INTO " + tableName + " (data1, data2, data3, data4, data5) VALUES ('" + datas[0] + "', '" + datas[1] + "', '" + datas[2] + "', '" + datas[3] + "', '" + datas[4] + "');";
        db.execSQL(query);
        db.close();
    }

    public void deleteAllData() {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM info");
        db.execSQL("DELETE FROM " + WEEKDAY_NOHOLIDAY_CHEONAN_ASAN_STATION);
        db.execSQL("DELETE FROM " + WEEKDAY_NOHOLIDAY_CHEONAN_TERMINAL);
        db.execSQL("DELETE FROM " + WEEKDAY_NOHOLIDAY_ONYANG_STATION_TERMINAL);
        db.execSQL("DELETE FROM " + WEEKDAY_NOHOLIDAY_CHEONAN_CAMPUS);
        db.execSQL("DELETE FROM " + SATURDAY_NOHOLIDAY_CHEONAN_ASAN_STATION);
        db.execSQL("DELETE FROM " + SATURDAY_NOHOLIDAY_CHEONAN_TERMINAL);
        db.execSQL("DELETE FROM " + SUNDAY_NOHOLIDAY_CHEONAN_ASAN_STATION);
        db.execSQL("DELETE FROM " + SUNDAY_NOHOLIDAY_CHEONAN_TERMINAL);
        db.execSQL("DELETE FROM " + WEEKDAY_HOLIDAY_CHEONAN_ASAN_STATION);
        db.execSQL("DELETE FROM " + WEEKDAY_HOLIDAY_CHEONAN_TERMINAL);
        db.execSQL("DELETE FROM " + WEEKDAY_HOLIDAY_ONYANG_STATION_TERMINAL);
        db.execSQL("DELETE FROM " + WEEKDAY_HOLIDAY_CHEONAN_CAMPUS);
        db.execSQL("DELETE FROM " + WEEKEND_HOLIDAY_CHEONAN_ASAN_STATION);
        db.execSQL("DELETE FROM " + WEEKEND_HOLIDAY_CHEONAN_TERMINAL);

        db.close();
    }
}
