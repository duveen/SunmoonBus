package kr.o3selab.sunmoonbus.service;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import kr.o3selab.sunmoonbus.constant.ConstantsOld;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    public static final String TAG = "[MyFirebaseInstanceIdService]";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        ConstantsOld.printLog(1, TAG + "New Token: " + refreshedToken, null);


    }
}
