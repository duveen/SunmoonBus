package kr.o3selab.sunmoonbus.model;

import java.util.Vector;

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
