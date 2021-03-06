package com.example.xpvehicles.miscellaneous;

import android.text.TextUtils;
import android.util.Patterns;

public class EmailChecker {
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
