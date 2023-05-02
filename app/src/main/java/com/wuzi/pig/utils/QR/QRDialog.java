package com.wuzi.pig.utils.QR;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.utils.widget.ImageFilterView;

import com.google.gson.Gson;
import com.wuzi.pig.R;
import com.wuzi.pig.base.BaseDialogFragment;
import com.wuzi.pig.constant.PigFarmConstant;
import com.wuzi.pig.entity.PigFarmEntity;
import com.wuzi.pig.utils.UIUtils;
import com.wuzi.pig.utils.tools.TimeUtils;

import java.util.Calendar;

import butterknife.BindView;

public class QRDialog extends BaseDialogFragment {

    @BindView(R.id.qr_title)
    AppCompatTextView mQRTitleView;
    @BindView(R.id.qr_view)
    ImageFilterView mQRView;
    @BindView(R.id.qr_time)
    AppCompatTextView mQRTimeView;
    @BindView(R.id.qr_prompt)
    AppCompatTextView mQRPromptView;

    private PigFarmEntity mPigFarmEntity;

    @Override
    protected int getLayoutID() {
        return R.layout.dialog_qr;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        setMessage(mPigFarmEntity);
    }

    public void setMessage(PigFarmEntity entity) {
        mPigFarmEntity = entity;
        if (entity != null) {
            if (mQRTitleView != null) {
                mQRTitleView.setText(entity.getPigfarmName());
            }
            long timeInMillis = Calendar.getInstance().getTimeInMillis();
            if (mQRTimeView != null) {
                String formatTime = TimeUtils.getFormatTime(timeInMillis);
                int minute = PigFarmConstant.PIG_FARM_QR_PAST_TIME / 1000 / 60;
                mQRTimeView.setText(formatTime + "（" + minute + "分钟后过期）");
            }
            if (mQRPromptView != null) {

                mQRPromptView.setText("扫描此二维码绑定猪场");
            }
            if (mQRView != null) {
                int screenWidth = UIUtils.getScreenWidth(mContext);
                Bitmap logoBitmap = getLogoBitmap();
                String[] texts = new String[]{
                        entity.getPigfarmId(),
                        entity.getPigfarmName(),
                        String.valueOf(timeInMillis)
                };
                String json = new Gson().toJson(texts);
                Bitmap qrBitmap = QRCodeUtil.createQRCodeWithLogo(json, (int) (screenWidth * 0.8f), logoBitmap);
                mQRView.setImageBitmap(qrBitmap);
            }
        }
    }

    private Bitmap getLogoBitmap() {
        Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        /*int padding = 0; //Math.max(UIUtils.dip2px(3), logoBitmap / 30);
        Bitmap logoBgBitmap = Bitmap.createBitmap(logoBitmap.getWidth() + padding, logoBitmap.getHeight() + padding, logoBitmap.getConfig());
        Canvas canvas = new Canvas(logoBgBitmap);
        Rect resRect = new Rect(0, 0, logoBitmap.getWidth(), logoBitmap.getHeight());
        Rect dstRect = new Rect(resRect);
        dstRect.offset(padding/2, padding/2);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawBitmap(logoBitmap, resRect, dstRect, paint);*/
        return logoBitmap;
    }
}
