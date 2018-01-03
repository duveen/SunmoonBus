package kr.o3selab.sunmoonbus.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import kr.o3selab.sunmoonbus.constant.ConstantsOld;
import kr.o3selab.sunmoonbus.model.DBManager;
import kr.o3selab.sunmoonbus.model.TableClickListener;
import kr.o3selab.sunmoonbus.R;
import me.grantland.widget.AutofitTextView;

public class Tab2 extends Fragment {

    public String TAG;
    public int mType;

    public Tab2() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mType = getArguments().getInt("type");

        if (mType == ConstantsOld.WEEKDAY_NOHOLIDAY)
            TAG = DBManager.WEEKDAY_NOHOLIDAY_CHEONAN_TERMINAL;
        else if (mType == ConstantsOld.SATURDAY_NOHOLIDAY)
            TAG = DBManager.SATURDAY_NOHOLIDAY_CHEONAN_TERMINAL;
        else if (mType == ConstantsOld.SUNDAY_NOHOLIDAY)
            TAG = DBManager.SUNDAY_NOHOLIDAY_CHEONAN_TERMINAL;
        else if (mType == ConstantsOld.WEEKDAY_HOLIDAY)
            TAG = DBManager.WEEKDAY_HOLIDAY_CHEONAN_TERMINAL;
        else if (mType == ConstantsOld.WEEKEND_HOLIDAY)
            TAG = DBManager.WEEKEND_HOLIDAY_CHEONAN_TERMINAL;

        View v = inflater.inflate(R.layout.tab_2, container, false);

        LinearLayout tabLayout = (LinearLayout) v.findViewById(R.id.data_table);

        String query = "SELECT * FROM " + TAG + ";";
        Cursor cursor = ConstantsOld.mDBManager.execReadSQL(query);

        while (cursor.moveToNext()) {

            String data1 = cursor.getString(1);
            String data2 = cursor.getString(2);
            String data3 = cursor.getString(3);

            String[] datas = {data1, data2, data3};

            LinearLayout itemLayout = (LinearLayout) inflater.inflate(R.layout.item_three, null);

            AutofitTextView textView2 = (AutofitTextView) itemLayout.findViewById(R.id.item_tab1);
            textView2.setText(data1);

            AutofitTextView textView3 = (AutofitTextView) itemLayout.findViewById(R.id.item_tab2);
            textView3.setText(data2);

            AutofitTextView textView4 = (AutofitTextView) itemLayout.findViewById(R.id.item_tab3);
            textView4.setText(data3);

            tabLayout.addView(itemLayout);
            itemLayout.setOnClickListener(new TableClickListener(getString(R.string.region_cheonan_terminal), TableClickListener.ITEM_THREE, datas));
        }

        return v;
    }

}