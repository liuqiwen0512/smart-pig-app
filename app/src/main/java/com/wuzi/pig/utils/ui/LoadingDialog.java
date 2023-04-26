package com.wuzi.pig.utils.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.wuzi.pig.R;
import com.wuzi.pig.utils.StatusBarUtils;
import com.wuzi.pig.utils.StatusbarColorUtils;

/**
 * 加载进度条
 */
public class LoadingDialog extends Dialog {
    // 有背景色
    public LoadingDialog(Context context) {
        super(context, R.style.loadingDialogTheme);
        initView();
    }

    // 没有背景色
    public LoadingDialog(Context context, boolean noBg) {
        super(context, R.style.loadingDialogThemeNobg);
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_loading);
        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.alpha = 1f;
            window.setAttributes(attributes);
        }
        setCancelable(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (LoadingDialog.this.isShowing())
                    LoadingDialog.this.dismiss();
                break;
        }
        return true;
    }

    // 设置加载文字
    public void setText(String text) {
        TextView textView = findViewById(R.id.tv_content);
        if (textView != null) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }
    }
}