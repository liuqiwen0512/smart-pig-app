package com.wuzi.pig.utils.ui;

import android.app.Activity;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

public class ViewCompat {

    public static void passwordCursorToEnd(Activity activity, AppCompatEditText editView) {
        View currentFocus = activity.getCurrentFocus();
        if (currentFocus == editView) {
            editView.setSelection(editView.getText().toString().length());
        }
        editView.requestFocus();
    }

}
