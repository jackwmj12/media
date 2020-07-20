package com.example.administrator.video.DateSelect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.Calendar;

/**
 * Created by Administrator on 2019/5/7 0007.
 */

public class DateTimepickerDialog extends AlertDialog {
    private DateTimepicker mDateTimePicker;
    private Calendar mDate = Calendar.getInstance();
    private int Year,Month,Day,Hour,Minute,Second;
    private OnDateTimeSetListener mOnDateTimeSetListener;
    private String datetimeStr;
    private String str;
    private final static String FILE_NAME = "xth.txt"; // 设置文件的名称
    public DateTimepickerDialog(Context context, long date, int minute, int second) {
        super(context);
        mDateTimePicker = new DateTimepicker(context,minute,second);
        /*
         *实现DateTimepicker里的接口
         */
        mDateTimePicker.setOnDateTimeChangedListener(new DateTimepicker.OnDateTimeChangedListener() {
            public void onDateTimeChanged(DateTimepicker view,
                                          int year, int month, int day, int hour, int minute, int second) {
                Year = year;
                Month = month;
                Day = day;
                Hour = hour;
                Minute = minute;
//                CommToast.show(String.valueOf(Minute));
                Second = second;
            }
        });
        setTitle("请设置倒计时");
        Year = mDate.get(Calendar.YEAR);
        Month = mDate.get(Calendar.MONTH)+1;
        Day = mDate.get(Calendar.DAY_OF_MONTH);
        Hour = mDate.get(Calendar.HOUR_OF_DAY);
//        Minute = mDate.get(Calendar.MINUTE);
////        Second = mDate.get(Calendar.SECOND);
        Minute=minute;
        Second=second;
        setView(mDateTimePicker);//装载刚才建立的布局，把定义好的日期时间布局显示在这个自定义对话框上

        setButton(DialogInterface.BUTTON_POSITIVE,"确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
//                datetimeStr = Year +"-"+ Month +"-"+ Day +" "+Hour+":"+Minute+":"+Second;
                if (Second<10){
                    str="0"+ String.valueOf(Second);
                    datetimeStr = Minute+":"+str;


                }else{
                    str= String.valueOf(Second);
                    datetimeStr = Minute+":"+Second;
                }

                if (mOnDateTimeSetListener != null) {
                    mOnDateTimeSetListener.OnDateTimeSet(dialog, datetimeStr,Minute, Integer.parseInt(str));
                }
            }
        });
        setButton(DialogInterface.BUTTON_NEGATIVE,"取消", (OnClickListener) null);
        setCanceledOnTouchOutside(false);//点击对话框外无法关闭对话框
    }

    /*
     *
     *接口回调
     */
    public interface OnDateTimeSetListener {
        void OnDateTimeSet(DialogInterface dialog, String datetimeStr, int minute, int second);
    }


    /*
     *对外公开方法让Activity实现
     */
    public void setOnDateTimeSetListener(OnDateTimeSetListener callBack) {
        mOnDateTimeSetListener = callBack;
    }

}
