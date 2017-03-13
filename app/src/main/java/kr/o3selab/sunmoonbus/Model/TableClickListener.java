package kr.o3selab.sunmoonbus.Model;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import kr.o3selab.sunmoonbus.Constant.Constants;
import kr.o3selab.sunmoonbus.R;

public class TableClickListener implements View.OnClickListener {

    public static final Integer ITEM_THREE = 3;
    public static final Integer ITEM_FOUR = 4;
    public static final Integer ITEM_FIVE = 5;

    private Context mContext;
    private String mLocation;
    private Integer mItemCount;
    private String[] mDatas;

    public TableClickListener(String location, int itemCount, String[] datas) {
        mContext = Constants.context;
        mDatas = datas;
        mLocation = location;
        mItemCount = itemCount;
    }

    @Override
    public void onClick(View view) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_time_detail);
        dialog.setCancelable(false);

        ImageView imageView = (ImageView) dialog.findViewById(R.id.time_detail_line);

        switch (mItemCount) {
            case 3:
                imageView.setImageResource(R.drawable.time_dialog_02);
                break;
            case 4:
                imageView.setImageResource(R.drawable.time_dialog_03);
                break;
            case 5:
                imageView.setImageResource(R.drawable.time_dialog_04);
                break;
        }

        final String[] items = getItemsUsingTitle(mLocation);
        final String[] datas = new String[mItemCount];

        for(int i = 0; i < mItemCount; i++) {
            datas[i] = items[i] + " : " + mDatas[i];
        }

        LinearLayout itemsLayout = (LinearLayout) dialog.findViewById(R.id.time_detail_items);

        for(String data : datas) {
            TextView textView = new TextView(mContext);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            layoutParams.weight = 1;
            layoutParams.gravity = Gravity.CENTER_VERTICAL;

            textView.setLayoutParams(layoutParams);
            textView.setText(data);
            textView.setTextSize(16);
            textView.setTextColor(Color.BLACK);

            itemsLayout.addView(textView);
        }

        ImageView sendKakaoButton = (ImageView) dialog.findViewById(R.id.time_detail_kakao);
        sendKakaoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyKakaoLink.sendKakaoMessage(mLocation, datas);
            }
        });

        ImageView okButton = (ImageView) dialog.findViewById(R.id.time_detail_cancel);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    String[] getItemsUsingTitle(String location) {

        String[] items = new String[mItemCount];

        if(location.equals(mContext.getString(R.string.region_cheonan_asan_station))) {

            items[0] = mContext.getString(R.string.region_depart_asancampus_without_n);
            items[1] = mContext.getString(R.string.region_asan_station);
            items[2] = mContext.getString(R.string.region_cheonan_station);
            items[3] = mContext.getString(R.string.region_asan_station);
            items[4] = mContext.getString(R.string.region_arrive_asancampus_without_n);

        } else if(location.equals(mContext.getString(R.string.region_cheonan_terminal))) {

            items[0] = mContext.getString(R.string.region_depart_asancampus_without_n);
            items[1] = mContext.getString(R.string.region_cheonan_terminal);
            items[2] = mContext.getString(R.string.region_arrive_asancampus_without_n);

        } else if(location.equals(mContext.getString(R.string.region_onyang_station_terminal))) {

            items[0] = mContext.getString(R.string.region_depart_asancampus_without_n);
            items[1] = mContext.getString(R.string.region_baebang);
            items[2] = mContext.getString(R.string.region_onyang_terminal);
            items[3] = mContext.getString(R.string.region_onyang_station);
            items[4] = mContext.getString(R.string.region_arrive_asancampus_without_n);

        } else if(location.equals(mContext.getString(R.string.region_cheonan_to_asan))) {

            items[0] = mContext.getString(R.string.region_depart_cheonancampus_without_n);
            items[1] = mContext.getString(R.string.region_cheongsu);
            items[2] = mContext.getString(R.string.region_sinbang);
            items[3] = mContext.getString(R.string.region_asan_station);
            items[4] = mContext.getString(R.string.region_arrive_asancampus_without_n);

        } else if(location.equals(mContext.getString(R.string.region_asan_to_cheonan))) {

            items[0] = mContext.getString(R.string.region_depart_asancampus_without_n);
            items[1] = mContext.getString(R.string.region_asan_station);
            items[2] = mContext.getString(R.string.region_sinbang);
            items[3] = mContext.getString(R.string.region_cheongsu);
            items[4] = mContext.getString(R.string.region_arrive_cheonancampus_without_n);

        }

        return items;
    }
}
