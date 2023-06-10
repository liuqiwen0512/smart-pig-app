package com.wuzi.pig.module.monitor;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;

import androidx.appcompat.widget.AppCompatTextView;

import com.wuzi.pig.R;
import com.wuzi.pig.base.BaseDialogFragment;
import com.wuzi.pig.module.monitor.tools.MonTools;
import com.wuzi.pig.utils.StringUtils;
import com.wuzi.pig.utils.fun.Function;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

public class MonDateDialog extends BaseDialogFragment {

    @BindView(R.id.date_value)
    AppCompatTextView mDateValueView;
    @BindView(R.id.date_week_value)
    AppCompatTextView mDateWeekValueView;
    @BindView(R.id.date)
    DatePicker mDatePicker;

    private Function<Calendar> mSubmitListener;
    private Calendar mCurrentDate;

    @Override
    protected int getLayoutID() {
        return R.layout.dialog_monitor_chart_date;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mDatePicker.setMaxDate(Calendar.getInstance().getTimeInMillis());
        mDatePicker.init(mCurrentDate.get(Calendar.YEAR),
                mCurrentDate.get(Calendar.MONTH),
                mCurrentDate.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                        setDateValue(calendar);
                    }
                });
        setDateValue(mCurrentDate);
    }

    @OnClick({R.id.cancel, R.id.ok})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.cancel: {
                dismiss();
                break;
            }
            case R.id.ok: {
                dismiss();
                if (mSubmitListener != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth(), 0, 0, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    mSubmitListener.action(calendar);
                }
                break;
            }
        }
    }

    public void setCurrentDate(Calendar currentDate) {
        mCurrentDate = currentDate;
    }

    public void setSubmitListener(Function<Calendar> submitListener) {
        mSubmitListener = submitListener;
    }

    @Override
    protected void initWindowTheme(Dialog dialog, Window window) {
        super.initWindowTheme(dialog, window);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.windowAnimations = R.style.dialog_fade_style3;
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.BOTTOM;
        window.setAttributes(attributes);
    }

    private void setDateValue(Calendar calendar) {
        mDateValueView.setText(MonTools.formatTime(calendar));
        String nickname = MonTools.nicknameOfTime(calendar);
        if (!StringUtils.isEmpty(nickname)) {
            nickname = " (" + nickname + ")";
        }
        mDateWeekValueView.setText(MonTools.getFormatWeek(calendar) + nickname);
    }
}
