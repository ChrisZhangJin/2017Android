package com.example.chris.dualsimdemo;

import android.telephony.TelephonyManager;

/**
 * Created by Administrator on 2017/12/13 0013.
 */

public class TelephonyUtils {

    public static boolean isNullorEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String convertState(int code) {
        String state = "";
        switch (code) {
            case TelephonyManager.SIM_STATE_UNKNOWN:
                state = "unknown";
                break;
            case TelephonyManager.SIM_STATE_ABSENT:
                state = "absent";
                break;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                state = "pin required";
                break;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                state = "puk required";
                break;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                state = "network locked";
                break;
            case TelephonyManager.SIM_STATE_READY:
                state = "ready";
                break;
            case TelephonyManager.SIM_STATE_NOT_READY:
                state = "not ready";
                break;
            default:
                state = "other state";
                break;
        }
        return state;
    }

    public static String convertOperatorName(String code) {
        if (isNullorEmpty(code)) {
            return "";
        }

        if (code.equals("46000") || code.equals("46002") || code.equals("46007")) {
            return "中国移动";
        }

        if (code.equals("46001")) {
            return "中国联通";
        }

        if (code.equals("46003")) {
            return "中国电信";
        }

        return "海外运营商";
    }
}
