package kr.o3selab.sunmoonbus.model;

import android.app.Activity;
import android.widget.Toast;

import kr.o3selab.sunmoonbus.R;

public class BackPressed {

    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;

    public BackPressed(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }

        if(System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();
            toast.cancel();
        }
    }

    private void showGuide() {
        toast = Toast.makeText(activity, R.string.back_button_toast, Toast.LENGTH_SHORT);
        toast.show();
    }

}
