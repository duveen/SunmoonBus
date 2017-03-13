package kr.o3selab.sunmoonbus.Model;

import java.util.Vector;

/**
 * Created by duveen on 2017-02-09.
 */

public class TimeTableData {

    private int status;
    private Vector<String[]> datas;

    public TimeTableData(int status, Vector<String[]> datas) {
        this.status = status;
        this.datas = datas;
    }

    public int getStatus() {
        return status;
    }

    public Vector<String[]> getDatas() {
        return datas;
    }
}
