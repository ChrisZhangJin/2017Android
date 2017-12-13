package com.example.chris.dualsimdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/12/12 0012.
 */

public class TelephonyInfoCompat {

    private static final String METHOD_GET_SIM_STATE = "getSimState";
    private static final String METHOD_GET_SIM_OPERATOR = "getSimOperator";
    private static final String METHOD_GET_LINE_NUMBER = "getLine1Number";

    private static TelephonyInfoCompat instance;
    private String imei1;
    private String imei2;
    private String sim1State;
    private String sim2State;
    private String sim1Oper;
    private String sim2Oper;
    private String sim1Number;
    private String sim2Number;
    private int slotCount;


    public static synchronized TelephonyInfoCompat getInstace(Context context) {
        if (null == instance) {
            instance = new TelephonyInfoCompat();
        }
        instance.init(context);
        return instance;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initImeiBasedVersionM(Context context, TelephonyManager manager) {
        // get the device id if permission is granted
        if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            imei1 = manager.getDeviceId(0);
            imei2 = manager.getDeviceId(1);
        }
    }

    private static String getSimStateBySlot(Context context, int slot) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String state = "";
        try {
            Class<?> telephonyClass = Class.forName(manager.getClass().getName());
            Method getSimState = telephonyClass.getDeclaredMethod(METHOD_GET_SIM_STATE, int.class);
            Object ret = getSimState.invoke(manager, slot);

            if (ret != null) {
                final int simState = Integer.parseInt(ret.toString());
                state = TelephonyUtils.convertState(simState);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return state;
    }

    private static String getSimOperatorBySlot(Context context, int slot) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = "";
        try {
            Class<?> telephonyClass = Class.forName(manager.getClass().getName());
            Method getSimOperator = telephonyClass.getDeclaredMethod(METHOD_GET_SIM_OPERATOR, int.class);
            Object ret = getSimOperator.invoke(manager, slot);

            if (ret != null) {
                operator = TelephonyUtils.convertOperatorName(ret.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return operator;
    }

    private static String getLine1NumberBySlot(Context context, int slot) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String number = "";
        try {
            Class<?> telephonyClass = Class.forName(manager.getClass().getName());
            Method getLineNumber = telephonyClass.getDeclaredMethod(METHOD_GET_LINE_NUMBER, int.class);
            Object ret = getLineNumber.invoke(manager, slot);

            if (ret != null) {
                number = ret.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return number;
    }

    private void init(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initImeiBasedVersionM(context, manager);
            sim1State = getSimStateBySlot(context, 0);
            sim2State = getSimStateBySlot(context, 1);
            sim1Oper = getSimOperatorBySlot(context, 0);
            sim2Oper = getSimOperatorBySlot(context, 1);
            sim1Number = getLine1NumberBySlot(context, 0);
            sim2Number = getLine1NumberBySlot(context, 1);
            slotCount = manager.getPhoneCount();
        } else {
            DualSimAbility dualSimAbility = getDualSimAbilityProvider(context);
            imei1 = dualSimAbility.getImei1();
            imei2 = dualSimAbility.getImei2();
            sim1State = dualSimAbility.getSim1State();
            sim2State = dualSimAbility.getSim2State();
            sim1Oper = dualSimAbility.getSim1OperName();
            sim2Oper = dualSimAbility.getSim2OperName();
            sim1Number = dualSimAbility.getSim1Number();
            sim2Number = dualSimAbility.getSim2Number();
            slotCount = dualSimAbility.getSlotCount();
        }
    }

    private DualSimAbility getDualSimAbilityProvider(Context context) {
        DualSimProviderQualcomm qualcomm = new DualSimProviderQualcomm(context);
        if (qualcomm.isMatched()) {
            return qualcomm;
        }

        DualSimProviderMTK mtk = new DualSimProviderMTK(context);
        if (mtk.isMatched()) {
            return mtk;
        }

        return null;
    }


    public String getImei1() {
        return imei1;
    }

    public String getImei2() {
        return imei2;
    }

    public String getSIM1State() {
        return sim1State;
    }

    public String getSIM2State() {
        return sim2State;
    }

    public String getSim1Number() {
        return sim1Number;
    }

    public String getSim2Number() {
        return sim2Number;
    }

    public int getSlotCount() {
        return slotCount;
    }

    /**
     * based on Kitkat 4.4 api,
     * the 3rd part provider interface
     */
    private interface DualSimAbility {
        boolean isMatched();

        String getImei1();

        String getImei2();

        String getSim1State();

        String getSim2State();

        String getSim1OperName();

        String getSim2OperName();

        String getSim1Number();

        String getSim2Number();

        int getSlotCount();
    }

    private static abstract class DualSimProvider implements DualSimAbility {
        protected boolean isMatched = false;
        protected String imei1;
        protected String imei2;
        protected String imsi1;
        protected String imsi2;
        protected String sim1State;
        protected String sim2State;
        protected String sim1OperName;
        protected String sim2OperName;
        protected String sim1Number;
        protected String sim2Number;
        protected int slotCount;

        @Override
        public boolean isMatched() {
            return isMatched;
        }

        @Override
        public String getImei1() {
            return imei1;
        }

        @Override
        public String getImei2() {
            return imei2;
        }

        @Override
        public String getSim1State() {
            return sim1State;
        }

        @Override
        public String getSim2State() {
            return sim2State;
        }

        @Override
        public String getSim1OperName() {
            return sim1OperName;
        }

        @Override
        public String getSim2OperName() {
            return sim2OperName;
        }

        @Override
        public String getSim1Number() {
            return sim1Number;
        }

        @Override
        public String getSim2Number() {
            return sim2Number;
        }

        @Override
        public int getSlotCount() {
            return slotCount;
        }
    }

    /**
     * MTK平台
     * TODO: need to be tested
     */
    private static class DualSimProviderMTK extends DualSimProvider {

        DualSimProviderMTK(Context context) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            try {
                Class<?> c = Class.forName("com.android.internal.telephony.Phone");
                Field fields1 = c.getField("GEMINI_SIM_1");
                fields1.setAccessible(true);
                final int simId1 = (Integer) fields1.get(null);
                Field fields2 = c.getField("GEMINI_SIM_2");
                fields2.setAccessible(true);
                final int simId2 = (Integer) fields2.get(null);

                Method mx = TelephonyManager.class.getMethod("getDefault", int.class);
                TelephonyManager tm1 = (TelephonyManager) mx.invoke(telephonyManager, simId1);
                TelephonyManager tm2 = (TelephonyManager) mx.invoke(telephonyManager, simId2);

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    imsi1 = tm1.getSubscriberId();
                    imsi2 = tm2.getSubscriberId();
                    imei1 = tm1.getDeviceId();
                    imei2 = tm2.getDeviceId();
                }
            } catch (Exception e) {
                // nothing
                isMatched = false;
            }

            isMatched = true;
        }
    } // END OF DualSimProviderMTK


    /**
     * 高通平台
     */
    private static class DualSimProviderQualcomm extends DualSimProvider {

        @SuppressLint("WrongConstant")
        DualSimProviderQualcomm(Context context) {
            try {
                Class<?> c = Class.forName("android.telephony.MSimTelephonyManager");
                Object serviceObj = context.getSystemService("phone_msim");
                final int simId1 = 0;
                final int simId2 = 1;

                Method getDeviceId = c.getMethod("getDeviceId", int.class);
                Method getSubscriberId = c.getMethod("getSubscriberId", int.class);
                Method getPhoneType = c.getMethod("getPhoneType");
                Method getSimState = c.getMethod("getSimState", int.class);
                Method getPhoneCount = c.getMethod("getPhoneCount");
                Method getSimOperator = c.getMethod("getSimOperator", int.class);
                Method getLine1Number = c.getMethod("getLine1Number", int.class);

                imei1 = (String) getDeviceId.invoke(serviceObj, simId1);
                imei2 = (String) getDeviceId.invoke(serviceObj, simId2);

                imsi1 = (String) getSubscriberId.invoke(serviceObj, simId1);
                imsi2 = (String) getSubscriberId.invoke(serviceObj, simId2);

                sim1State = TelephonyUtils.convertState((int) getSimState.invoke(serviceObj, simId1));
                sim2State = TelephonyUtils.convertState((int) getSimState.invoke(serviceObj, simId2));

                sim1OperName = TelephonyUtils.convertOperatorName((String) getSimOperator.invoke(serviceObj, simId1));
                sim2OperName = TelephonyUtils.convertOperatorName((String) getSimOperator.invoke(serviceObj, simId2));

                sim1Number = (String) getLine1Number.invoke(serviceObj, simId1);
                sim2Number = (String) getLine1Number.invoke(serviceObj, simId2);

                slotCount = (int) getPhoneCount.invoke(serviceObj);
            } catch (Exception e) {
                isMatched = false;
            }
            isMatched = true;
        }
    }
}
