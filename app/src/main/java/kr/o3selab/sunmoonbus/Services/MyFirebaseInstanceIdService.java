package kr.o3selab.sunmoonbus.Services;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import kr.o3selab.sunmoonbus.Constant.Constants;

/**
 * Created by mingyupark on 2017. 3. 12..
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    public static final String TAG = "[MyFirebaseInstanceIdService]";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Constants.printLog(1, TAG + "New Token: " + refreshedToken, null);


    }
}
