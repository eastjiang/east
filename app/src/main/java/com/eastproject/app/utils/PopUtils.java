package com.eastproject.app.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by TMD on 2016/8/8.
 */
public class PopUtils {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
