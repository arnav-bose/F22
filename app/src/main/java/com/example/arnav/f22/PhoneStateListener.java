package com.example.arnav.f22;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by Arnav on 19/11/2016.
 */
public class PhoneStateListener extends android.telephony.PhoneStateListener {

    private Context mContext;
    public static boolean pickedUp = false;

    public PhoneStateListener(Context context){
        this.mContext = context;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        switch (state){
            case TelephonyManager.CALL_STATE_IDLE:
                if (!pickedUp)
                    pickedUp = false;
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                pickedUp = false;
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                pickedUp = true;
        }
    }

    public static boolean callPicked(){
        return pickedUp;
    }
}
