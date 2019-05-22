package com.Danthop.bionet.Class;

import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceService extends FirebaseInstanceIdService {

    private static final String LOGTAG = "android-fcm";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Se obtiene el token actualizado

        Log.d(LOGTAG, "Token actualizado: " + refreshedToken);

    }
}
