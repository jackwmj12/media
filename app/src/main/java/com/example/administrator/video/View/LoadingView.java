package com.example.administrator.video.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.video.R;

import androidx.annotation.NonNull;


/**
 * Created by wood on 2018/3/6.
 */

public class LoadingView extends Dialog {

    View contentView;
    LVCircularRing circle;

    public LoadingView(@NonNull Context context) {
        super(context, R.style.loadingDialogTheme);
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
    }

    public static final LoadingView getNewInstance(Context context) {
        return new LoadingView(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        circle = findViewById(R.id.pg_loading);
    }

    private void setText(String text) {
        if (isNotEmpty(text)) {
            contentView.findViewById(R.id.tv_info).setVisibility(View.VISIBLE);
            ((TextView) contentView.findViewById(R.id.tv_info)).setText(text);
        } else {
            contentView.findViewById(R.id.tv_info).setVisibility(View.GONE);
        }
    }


    public void show(String text) {
        setText(text);
        super.show();
        if (circle != null) {
            circle.startAnim();
        }
    }

    @Override
    public void show() {
        show("");
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (circle != null) {
            circle.stopAnim();
        }
    }


    private final boolean isNotEmpty(String s) {
        return s != null && !s.trim().equals("");
    }
}
