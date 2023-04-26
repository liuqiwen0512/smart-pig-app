package com.wuzi.pig.utils;

import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.wuzi.pig.R;

public class PromptUtils {

    public static void hidePrompt(View view) {
        view.setVisibility(View.GONE);
    }

    public static void showEmptyPrompt(AppCompatTextView view) {
        String msg = view.getResources().getString(R.string.error_empty);
        showEmptyPrompt(view, msg);
    }

    public static void showEmptyPrompt(AppCompatTextView view, String message) {
        view.setVisibility(View.VISIBLE);
        view.setText(message);
    }

}
