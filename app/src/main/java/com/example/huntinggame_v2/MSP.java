package com.example.huntinggame_v2;

import android.content.Context;
import android.content.SharedPreferences;

public class MSP {
    private final String SP_FILE = "SP_FILE";

    private static MSP me;
    private SharedPreferences sharedPreferences;

    public static MSP getMe() {
        return me;
    }

    private MSP(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
    }

    public static MSP initHelper(Context context) {
        if (me == null) {
            me = new MSP(context);
        }
        return me;
    }

    public void putDouble(String KEY, double defValue) {
        putString(KEY, String.valueOf(defValue));
    }

    public double getDouble(String KEY, double defValue) {
        return Double.parseDouble(getString(KEY, String.valueOf(defValue)));
    }

    public int getInt(String KEY, int defValue) {
        return sharedPreferences.getInt(KEY, defValue);
    }

    public void putInt(String KEY, int value) {
        sharedPreferences.edit().putInt(KEY, value).apply();
    }

    public String getString(String KEY, String defValue) {
        return sharedPreferences.getString(KEY, defValue);
    }

    public void putString(String KEY, String value) {
        sharedPreferences.edit().putString(KEY, value).apply();
    }

    public void saveStringArray(String[] array, String arrayName) {
        sharedPreferences.edit().putInt(arrayName.toUpperCase() +"_SIZE", array.length);
        for(int i=1;i<=array.length;i++)
            sharedPreferences.edit().putString(arrayName.toUpperCase() + "_" + i, array[i]);
    }

    public String[] loadStringArray(String arrayName) {
        int size = sharedPreferences.getInt(arrayName.toUpperCase() + "_SIZE", 0);
        String array[] = new String[size];
        for(int i=0;i<size;i++)
            array[i] = sharedPreferences.getString(arrayName.toUpperCase() + "_" + i, "");
        return array;
    }

    public void saveIntArray(Integer[] array, String arrayName) {
        sharedPreferences.edit().putInt(arrayName.toUpperCase() +"_SIZE", array.length);
        for(int i=1;i<=array.length;i++)
            sharedPreferences.edit().putInt(arrayName.toUpperCase() + "_" + i, array[i-1]).apply();
    }

    public Integer[] loadIntArray(String arrayName) {
        int size = sharedPreferences.getInt(arrayName.toUpperCase() + "_SIZE", 10);
        Integer array[] = new Integer[size];
        for(int i=0;i<size;i++)
            array[i] = sharedPreferences.getInt(arrayName.toUpperCase() + "_" + i,0);
        return array;
    }

}
