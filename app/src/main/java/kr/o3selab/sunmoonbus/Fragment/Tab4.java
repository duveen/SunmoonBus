package kr.o3selab.sunmoonbus.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import kr.o3selab.sunmoonbus.Constant.Constants;
import kr.o3selab.sunmoonbus.Model.DBManager;
import kr.o3selab.sunmoonbus.Model.TableClickListener;
import kr.o3selab.sunmoonbus.R;
import me.grantland.widget.AutofitTextView;

public class Tab4 extends Fragment {

    public String TAG;
    public int mType;

    public Tab4() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mType = getArguments().getInt("type");

        if (mType == Constants.WEEKDAY_NOHOLIDAY)
            TAG = DBManager.WEEKDAY_NOHOLIDAY_CHEONAN_CAMPUS;
        else if (mType == Constants.WEEKDAY_HOLIDAY)
            TAG = DBManager.WEEKDAY_HOLIDAY_CHEONAN_CAMPUS;

        View v = inflater.inflate(R.layout.tab_4, container, false);

        LinearLayout tabLayout = (LinearLayout) v.findViewById(R.id.data_table);

        String query = "SELECT * FROM " + TAG + ";";
        Cursor cursor = Constants.mDBManager.execReadSQL(query);
        int flag = 1;
        boolean nextFlag = false;
        while (cursor.moveToNext()) {

            if (cursor.getCount() / 2 == flag - 1) {
                nextFlag = true;
                tabLayout = (LinearLayout) v.findViewById(R.id.data_table2);
            }

            String data1 = cursor.getString(1);
            String data2 = cursor.getString(2);
            String data3 = cursor.getString(3);
            String data4 = cursor.getString(4);
            String data5 = cursor.getString(5);

            String[] datas = {data1, data2, data3, data4, data5};

            LinearLayout itemLayout = (LinearLayout) inflater.inflate(R.layout.item_five, null);

            AutofitTextView textView2 = (AutofitTextView) itemLayout.findViewById(R.id.item_tab1);
            textView2.setText(data1);

            AutofitTextView textView3 = (AutofitTextView) itemLayout.findViewById(R.id.item_tab2);
            textView3.setText(data2);

            AutofitTextView textView4 = (AutofitTextView) itemLayout.findViewById(R.id.item_tab3);
            textView4.setText(data3);

            AutofitTextView textView5 = (AutofitTextView) itemLayout.findViewById(R.id.item_tab4);
            textView5.setText(data4);

            AutofitTextView textView6 = (AutofitTextView) itemLayout.findViewById(R.id.item_tab5);
            textView6.setText(data5);

            tabLayout.addView(itemLayout);
            flag++;
            if(!nextFlag)
                itemLayout.setOnClickListener(new TableClickListener(getString(R.string.region_cheonan_to_asan), TableClickListener.ITEM_FIVE, datas));
            else
                itemLayout.setOnClickListener(new TableClickListener(getString(R.string.region_asan_to_cheonan), TableClickListener.ITEM_FIVE, datas));

        }

        return v;
    }

}
