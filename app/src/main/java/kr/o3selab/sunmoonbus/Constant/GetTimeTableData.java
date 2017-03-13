package kr.o3selab.sunmoonbus.Constant;

import java.util.Vector;

import kr.o3selab.sunmoonbus.LoadingActivity;
import kr.o3selab.sunmoonbus.Model.DBManager;
import kr.o3selab.sunmoonbus.Model.NetworkGroup;
import kr.o3selab.sunmoonbus.Model.TimeTableData;

public class GetTimeTableData implements Runnable {

    DBManager mDBManager;
    LoadingActivity activity;

    public GetTimeTableData(LoadingActivity activity) {
        mDBManager = Constants.mDBManager;
        this.activity = activity;
    }

    @Override
    public void run() {
        try {
            int flag = 0;

            for (NetworkGroup networkGroup : API.WEEKDAY_NOHOLIDAY) {
                TimeTableData data = API.getData(networkGroup);

                Vector<String[]> timeDatas = data.getDatas();

                String tableName = DBManager.WEEKDAY_NOHOLIDAY_GROUP[flag];
                int columnCount = DBManager.WEEKDAY_NOHOLIDAY_GROUP_NUM[flag];

                for (String[] timeData : timeDatas) {
                    if (columnCount == 5) {
                        mDBManager.insertDataNum5(tableName, timeData);
                    } else if (columnCount == 4) {
                        mDBManager.insertDataNum4(tableName, timeData);
                    } else {
                        mDBManager.insertDataNum3(tableName, timeData);
                    }
                }
                flag++;
                activity.upProgressValue();
            }

            flag = 0;
            for (NetworkGroup networkGroup : API.SATURDAY_NOHOLIDAY) {
                TimeTableData data = API.getData(networkGroup);

                Vector<String[]> timeDatas = data.getDatas();

                String tableName = DBManager.SATURDAY_NOHOLIDAY_GROUP[flag];
                int columnCount = DBManager.SATURDAY_NOHOLIDAY_GROUP_NUM[flag];

                for (String[] timeData : timeDatas) {
                    if (columnCount == 5) {
                        mDBManager.insertDataNum5(tableName, timeData);
                    } else if (columnCount == 4) {
                        mDBManager.insertDataNum4(tableName, timeData);
                    } else {
                        mDBManager.insertDataNum3(tableName, timeData);
                    }
                }
                flag++;
                activity.upProgressValue();
            }

            flag = 0;
            for (NetworkGroup networkGroup : API.SUNDAY_NOHOLIDAY) {
                TimeTableData data = API.getData(networkGroup);

                Vector<String[]> timeDatas = data.getDatas();

                String tableName = DBManager.SUNDAY_NOHOLIDAY_GROUP[flag];
                int columnCount = DBManager.SUNDAY_NOHOLIDAY_GROUP_NUM[flag];

                for (String[] timeData : timeDatas) {
                    if (columnCount == 5) {
                        mDBManager.insertDataNum5(tableName, timeData);
                    } else if (columnCount == 4) {
                        mDBManager.insertDataNum4(tableName, timeData);
                    } else {
                        mDBManager.insertDataNum3(tableName, timeData);
                    }
                }
                flag++;
                activity.upProgressValue();
            }

            flag = 0;
            for (NetworkGroup networkGroup : API.WEEKDAY_HOLIDAY) {
                TimeTableData data = API.getData(networkGroup);

                Vector<String[]> timeDatas = data.getDatas();

                String tableName = DBManager.WEEKDAY_HOLIDAY_GROUP[flag];
                int columnCount = DBManager.WEEKDAY_HOLIDAY_GROUP_NUM[flag];

                for (String[] timeData : timeDatas) {
                    if (columnCount == 5) {
                        mDBManager.insertDataNum5(tableName, timeData);
                    } else if (columnCount == 4) {
                        mDBManager.insertDataNum4(tableName, timeData);
                    } else {
                        mDBManager.insertDataNum3(tableName, timeData);
                    }
                }
                flag++;
                activity.upProgressValue();
            }

            flag = 0;
            for (NetworkGroup networkGroup : API.SATURDAY_HOLIDAY) {
                TimeTableData data = API.getData(networkGroup);

                Vector<String[]> timeDatas = data.getDatas();

                String tableName = DBManager.WEEKEND_HOLIDAY_GROUP[flag];
                int columnCount = DBManager.WEEKEND_HOLIDAY_GROUP_NUM[flag];

                for (String[] timeData : timeDatas) {
                    if (columnCount == 5) {
                        mDBManager.insertDataNum5(tableName, timeData);
                    } else if (columnCount == 4) {
                        mDBManager.insertDataNum4(tableName, timeData);
                    } else {
                        mDBManager.insertDataNum3(tableName, timeData);
                    }
                }
                flag++;
                activity.upProgressValue();
            }

            activity.timeTableStatus = API.SUCCESS;
        } catch (Exception e) {
            activity.timeTableStatus = API.HTTP_HANDLER_ERROR;
        }
    }
}
