package com.wuzi.pig.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.Browser;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;

public class SpannableUtils {

    public static SpannableStringBuilder getHighlightSpannable(final String text, final String keyword, int color) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        if (!StringUtils.isEmpty(keyword)) {
            int length = keyword.length();
            int start = 0;
            while (start >= 0) {
                start = text.indexOf(keyword, start);
                if (start < 0) break;
                ForegroundColorSpan highSpan = new ForegroundColorSpan(color);
                builder.setSpan(highSpan, start, start + length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                start += length;
            }
        }
        return builder;
    }

    public static SpannableStringBuilder getBoldSpannable(final String text, final String keyword) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        if (!StringUtils.isEmpty(keyword)) {
            int length = keyword.length();
            int start = 0;
            while (start >= 0) {
                start = text.indexOf(keyword, start);
                if (start < 0) break;
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                builder.setSpan(styleSpan, start, start + length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                start += length;
            }
        }
        return builder;
    }

    public static SpannableStringBuilder getBoldSpannable(SpannableStringBuilder builder, final String text, final String keyword) {
        if (builder == null) {
            return null;
        }
        if (builder == null) {
            builder = new SpannableStringBuilder(text);
        }
        if (!StringUtils.isEmpty(keyword)) {
            int length = keyword.length();
            int start = 0;
            while (start >= 0) {
                start = text.indexOf(keyword, start);
                if (start < 0) break;
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                builder.setSpan(styleSpan, start, start + length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                start += length;
            }
        }
        return builder;
    }

    public static SpannableStringBuilder getSizeSpannable(SpannableStringBuilder builder, final String text, final String keyword, final int size) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        if (builder == null) {
            builder = new SpannableStringBuilder(text);
        }
        if (!StringUtils.isEmpty(keyword)) {
            int length = keyword.length();
            int start = 0;
            while (start >= 0) {
                start = text.indexOf(keyword, start);
                if (start < 0) break;
                AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(size, true);
                builder.setSpan(sizeSpan, start, start + length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                start += length;
            }
        }
        return builder;
    }

    public static SpannableStringBuilder getPhoneSpannable(final String phone, final int color) {
        if (StringUtils.isEmpty(phone)) {
            return null;
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(phone);
        ClickableSpan clickSpan = SpannableUtils.getURLSpanNoUnderline("tel:" + phone, color);
        builder.setSpan(clickSpan, 0, phone.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return builder;
    }

    public static ClickableSpan getURLSpanNoUnderline(String url, int color) {
        return new URLSpanNoUnderline(url, color);
    }

    public static class URLSpanNoUnderline extends ClickableSpan {
        private final String mUrl;
        private final int mColor;

        public URLSpanNoUnderline(String url, int color) {
            mUrl = url;
            mColor = color;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(mColor);//文本颜色
            ds.setUnderlineText(false);//是否有下划线
            //ds.bgColor = Color.WHITE;//背景颜色
        }

        @Override
        public void onClick(View v) {
            try {
                Uri uri = Uri.parse(mUrl);
                Context context = v.getContext();
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
