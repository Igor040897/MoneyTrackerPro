package com.igor040897.moneytrackerpro.API;

import android.text.TextUtils;

/**
 * Created by fanre on 7/1/2017.
 */

public class Result {
    String status;

    public boolean isSuccess() {
        return TextUtils.equals(status, "success");
    }
}
