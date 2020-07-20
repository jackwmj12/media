package com.example.administrator.video.DateSelect;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.NumberPicker;


import com.example.administrator.video.R;

import java.util.Calendar;


/**
 * Created by Administrator on 2019/5/7 0007.
 */

public class DateTimepicker extends FrameLayout {
//    private final NumberPicker mYearSpinner;
//    private final NumberPicker mMonthSpinner;
//    private final NumberPicker mDaySpinner;
//    private final NumberPicker mHourSpinner;
    private final NumberPicker mMinuteSpinner;
    private final NumberPicker mSecondSpinner;
    private Calendar mDate;
    private int mYear, mMonth, mDay, mHour, mMinute, mSecond;
    private int mMinute2,mSecond2;
    private OnDateTimeChangedListener mOnDateTimeChangedListener;

    public void setmMinute(int mMinute){
        this.mMinute2=mMinute;
//        CommToast.show(String.valueOf(mMinute2));
    }
    public void setmSecond(int mSecond){
        this.mSecond2=mSecond;
    }
    public int getmMinute(){
//        CommToast.show(String.valueOf(mMinute));
        return mMinute;
    }
    public int getmSecond(){
        return mSecond;
    }

    public NumberPicker getmMinuteSpinner(){
        return mMinuteSpinner;
    }
    public DateTimepicker(Context context, int minute, int second) {
        super(context);
        /*
         *
         */
        mDate = Calendar.getInstance();
        mYear = mDate.get(Calendar.YEAR);
        mMonth = mDate.get(Calendar.MONTH) + 1;
        mHour = mDate.get(Calendar.HOUR_OF_DAY);
        mMinute = minute;
//        CommToast.show(String.valueOf(mMinute2));
        mSecond = second;
        /**
         *
         */
        inflate(context, R.layout.datetimepicker, this);
        /**
         *
         */
//        mYearSpinner = (NumberPicker) this.findViewById(R.id.np_datetime_year);
//        mYearSpinner.setMaxValue(2100);
//        mYearSpinner.setMinValue(1900);
//        mYearSpinner.setValue(mYear);
//        mYearSpinner.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);//设置NumberPicker不可编辑
//        mYearSpinner.setOnValueChangedListener(mOnYearChangedListener);//注册NumberPicker值变化时的监听事件
//
//        mMonthSpinner = (NumberPicker) this.findViewById(R.id.np_datetime_month);
//        mMonthSpinner.setMaxValue(12);
//        mMonthSpinner.setMinValue(1);
//        mMonthSpinner.setValue(mMonth);
//        mMonthSpinner.setFormatter(formatter);//格式化显示数字，个位数前添加0
//        mMonthSpinner.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
//        mMonthSpinner.setOnValueChangedListener(mOnMonthChangedListener);
//
//        mDaySpinner = (NumberPicker) this.findViewById(R.id.np_datetime_day);
//        judgeMonth();//判断是否闰年，从而设置2月份的天数
//        mDay = mDate.get(Calendar.DAY_OF_MONTH);
//        mDaySpinner.setValue(mDay);
//        mDaySpinner.setFormatter(formatter);
//        mDaySpinner.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
//        mDaySpinner.setOnValueChangedListener(mOnDayChangedListener);
//
//        mHourSpinner = (NumberPicker) this.findViewById(R.id.np_datetime_hour);
//        mHourSpinner.setMaxValue(23);
//        mHourSpinner.setMinValue(0);
//        mHourSpinner.setValue(mHour);
//        mHourSpinner.setFormatter(formatter);
//        mHourSpinner.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
//        mHourSpinner.setOnValueChangedListener(mOnHourChangedListener);

        mMinuteSpinner = (NumberPicker) this.findViewById(R.id.np_datetime_minute);
        mMinuteSpinner.setMaxValue(60);
        mMinuteSpinner.setMinValue(0);
        mMinuteSpinner.setValue(mMinute);
        mMinuteSpinner.setFormatter(formatter);
        mMinuteSpinner.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mMinuteSpinner.setOnValueChangedListener(mOnMinuteChangedListener);

        mSecondSpinner = (NumberPicker) this.findViewById(R.id.np_datetime_second);
        mSecondSpinner.setMaxValue(59);
        mSecondSpinner.setMinValue(0);
        mSecondSpinner.setValue(mSecond);
        mSecondSpinner.setFormatter(formatter);
        mSecondSpinner.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mSecondSpinner.setOnValueChangedListener(mOnSecondChangedListener);
    }

    /**
     *
     *
     */
//    private NumberPicker.OnValueChangeListener mOnYearChangedListener = new OnValueChangeListener() {
//        @Override
//        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//            mYear = mYearSpinner.getValue();
//            judgeYear();
//            onDateTimeChanged();
//        }
//    };
//
//    private NumberPicker.OnValueChangeListener mOnMonthChangedListener = new OnValueChangeListener() {
//        @Override
//        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//            mMonth = mMonthSpinner.getValue();
//            judgeMonth();
//            onDateTimeChanged();
//        }
//    };
//
//    private NumberPicker.OnValueChangeListener mOnDayChangedListener = new OnValueChangeListener() {
//        @Override
//        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//            mDay = mDaySpinner.getValue();
//            onDateTimeChanged();
//        }
//    };
//
//    private NumberPicker.OnValueChangeListener mOnHourChangedListener = new OnValueChangeListener() {
//        @Override
//        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//            mHour = mHourSpinner.getValue();
//            onDateTimeChanged();
//        }
//    };

    private NumberPicker.OnValueChangeListener mOnMinuteChangedListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            mMinute = mMinuteSpinner.getValue();
            mMinute2=mMinute;
//            CommToast.show(String.valueOf(mMinute));
//            setmMinute(mMinute);
            onDateTimeChanged();
        }
    };

    private NumberPicker.OnValueChangeListener mOnSecondChangedListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            mSecond = mSecondSpinner.getValue();
            mSecond2=mSecond;
            onDateTimeChanged();
        }
    };
    //数字格式化，<10的数字前自动加0
    private NumberPicker.Formatter formatter = new NumberPicker.Formatter(){
        @Override
        public String format(int value) {
            String Str = String.valueOf(value);
            if (value < 10) {
                Str = "0" + Str;
            }
            return Str;
        }
    };

    /*
     *接口回调 参数是当前的View 年月日时分秒
     */
    public interface OnDateTimeChangedListener {
        void onDateTimeChanged(DateTimepicker view, int year, int month, int day, int hour, int minute, int second);
    }
    /*
     *对外的公开方法
     */
    public void setOnDateTimeChangedListener(OnDateTimeChangedListener callback) {
        mOnDateTimeChangedListener = callback;
    }

    private void onDateTimeChanged() {
        if (mOnDateTimeChangedListener != null) {
            mOnDateTimeChangedListener.onDateTimeChanged(this, mYear, mMonth, mDay, mHour, mMinute, mSecond);
        }
    }

//    private void judgeYear(){
//        if(mMonth == 2)
//        {
//            if(mYear%4 == 0)
//            {
//                if(mDaySpinner.getMaxValue() != 29)
//                {
//                    mDaySpinner.setDisplayedValues(null);
//                    mDaySpinner.setMinValue(1);
//                    mDaySpinner.setMaxValue(29);
//                }
//            }
//            else
//            {
//                if(mDaySpinner.getMaxValue() != 28)
//                {
//                    mDaySpinner.setDisplayedValues(null);
//                    mDaySpinner.setMinValue(1);
//                    mDaySpinner.setMaxValue(28);
//                }
//            }
//        }
//        mDay = mDaySpinner.getValue();
//    }
//
//    private void judgeMonth() {
//        if(mMonth == 2)
//        {
//            if(mYear%4 == 0)
//            {
//                if(mDaySpinner.getMaxValue() != 29)
//                {
//                    mDaySpinner.setDisplayedValues(null);
//                    mDaySpinner.setMinValue(1);
//                    mDaySpinner.setMaxValue(29);
//                }
//            }
//            else
//            {
//                if(mDaySpinner.getMaxValue() != 28)
//                {
//                    mDaySpinner.setDisplayedValues(null);
//                    mDaySpinner.setMinValue(1);
//                    mDaySpinner.setMaxValue(28);
//                }
//            }
//        }
//        else
//        {
//            switch(mMonth){
//                case 4:
//                case 6:
//                case 9:
//                case 11:
//                    if(mDaySpinner.getMaxValue() != 30)
//                    {
//                        mDaySpinner.setDisplayedValues(null);
//                        mDaySpinner.setMinValue(1);
//                        mDaySpinner.setMaxValue(30);
//                    }
//                    break;
//                default:
//                    if(mDaySpinner.getMaxValue() != 31)
//                    {
//                        mDaySpinner.setDisplayedValues(null);
//                        mDaySpinner.setMinValue(1);
//                        mDaySpinner.setMaxValue(31);
//                    }
//            }
//        }
//        mDay = mDaySpinner.getValue();
//
//    }

}
