package com.wuzi.pig.utils.ui.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;

public class GroupWrap extends Group {

    public GroupWrap(Context context) {
        super(context);
    }

    public GroupWrap(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GroupWrap(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setVisibility(int visibility) {
        ViewParent parent = this.getParent();
        if (parent instanceof ConstraintLayout) {
            ConstraintLayout container = (ConstraintLayout) parent;
            for(int i = 0; i < this.mCount; ++i) {
                int id = this.mIds[i];
                View view = container.getViewById(id);
                if (view != null) {
                    view.setVisibility(visibility);
                }
            }
        }
    }

    @Override
    protected void applyLayoutFeatures(ConstraintLayout container) {
        //int visibility = this.getVisibility();
        float elevation = 0.0F;
        if (Build.VERSION.SDK_INT >= 21) {
            elevation = this.getElevation();
        }

        for(int i = 0; i < this.mCount; ++i) {
            int id = this.mIds[i];
            View view = container.getViewById(id);
            if (view != null) {
                //view.setVisibility(visibility);
                if (elevation > 0.0F && Build.VERSION.SDK_INT >= 21) {
                    view.setTranslationZ(view.getTranslationZ() + elevation);
                }
            }
        }

    }
}
