package com.wuzi.pig.utils.ui.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;

public class AppCompatCheckBoxWrap extends AppCompatCheckBox {
    public AppCompatCheckBoxWrap(@NonNull Context context) {
        this(context, null);
    }

    public AppCompatCheckBoxWrap(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.R.attr.radioButtonStyle);
    }

    public AppCompatCheckBoxWrap(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
