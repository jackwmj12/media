package com.example.administrator.video;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;

import com.dyhdyh.support.countdowntimer.CountDownTimerSupport;
import com.dyhdyh.support.countdowntimer.OnCountDownTimerListener;
import com.example.administrator.video.DateSelect.DateTimepickerDialog;
import com.example.administrator.video.DateTimeSelect.DateTimePickDialogUtil;
import com.example.administrator.video.Utils.FileUtil;
import com.example.administrator.video.View.LoadingView;
import com.example.administrator.video.View.MarqueeMustView;
import com.shuyu.gsyvideoplayer.GSYBaseActivityDetail;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.model.GSYVideoModel;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.superluo.textbannerlibrary.TextBannerView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

public class MainActivity extends GSYBaseActivityDetail<ListGSYVideoPlayer> implements OnBannerListener {
    private static final int COMPLETED = 0;
    final static String TAG = "流媒体DEMO";
    MarqueeMustView tv_banner;
    String text;
    List<GSYVideoModel> list;
    ListGSYVideoPlayer detailPlayer;
    Banner banner;
    String[] urls;
    public static List<?> images=new ArrayList<>();
    ArrayList<String> stringArrayList;

    LoadingView loadingView;

    int minute1;
    int second1;
    TextView tv_time;
    CountDownTimerSupport mTimer;

    Calendar calendars;
    TextView tv_dateTime;
    String min;
    String month;
    String day;
    String hour;
    String time;
    String device;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        tv_banner=(MarqueeMustView)findViewById(R.id.tv_banner);
        detailPlayer=(ListGSYVideoPlayer)findViewById(R.id.detail_player);
        banner=(Banner)findViewById(R.id.banner);
        tv_time=(TextView)findViewById(R.id.time);
        tv_dateTime=(TextView)findViewById(R.id.datetime);

        initLoadingView();
        showLoadingDialog("");
//        device = android.os.Build.DEVICE;
        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
//        device = myDevice.getName();
        device="30-0B-0308D1002";
        Log.d("name",device);

        initVideo();
        LoadTXT();
        LoadVideo();
        LoadPhoto();
        time(tv_time);

        calendars = Calendar.getInstance();
        calendars.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        String year = String.valueOf(calendars.get(Calendar.YEAR));
        month = String.valueOf(calendars.get(Calendar.MONTH));
        if (Integer.parseInt(month)<10){
            month = "0"+String.valueOf(calendars.get(Calendar.MONTH));
        }else{
            month = String.valueOf(calendars.get(Calendar.MONTH));
        }
        day = String.valueOf(calendars.get(Calendar.DATE));
        if (Integer.parseInt(day)<10){
            day = "0"+String.valueOf(calendars.get(Calendar.DATE));
        }else{
            day = String.valueOf(calendars.get(Calendar.DATE));
        }
        hour = String.valueOf(calendars.get(Calendar.HOUR_OF_DAY));
        if (Integer.parseInt(hour)<10){
            hour = "0"+String.valueOf(calendars.get(Calendar.HOUR_OF_DAY));
        }else{
            hour = String.valueOf(calendars.get(Calendar.HOUR_OF_DAY));
        }
        min= String.valueOf(calendars.get(Calendar.MINUTE));
        if (Integer.parseInt(min)<10){
            min = "0"+String.valueOf(calendars.get(Calendar.MINUTE));
        }else{
            min = String.valueOf(calendars.get(Calendar.MINUTE));
        }
        tv_dateTime.setText(year+"年"+month+"月"+day+"日 "+hour+":"+min);
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    public static   void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void LoadTXT(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int code;
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(""+device);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("content-type", "application/json");
                    connection.setRequestProperty("charset", "UTF-8");
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    Log.d("save", url.toString());
                    if (connection.getResponseCode() == 200) {
                        InputStream in = connection.getInputStream();

                        //下面对获取到的输入流进行读取
                        reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        Log.d("PersonalActivity",response.toString());
                        parseTXT(response.toString());
                    }else if (connection.getResponseCode() >= 400 ) {
                        InputStream in = connection.getErrorStream();

                        //下面对获取到的输入流进行读取
                        reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        String result = response.toString();
                        Log.d("PersonalActivity",result);
                    }

//                    else {
//                        Looper.prepare();
//                        Toast.makeText(MainActivity.this, String.valueOf(connection.getResponseCode()), Toast.LENGTH_LONG).show();
//                        Looper.loop();
//                    }


                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
    private void parseTXT(String response){
        try{
            //单条数据解析
            JSONObject jsonObject = new JSONObject(response);
            Integer code = jsonObject.getInt("code");
            if (code==200) {
                JSONObject data = jsonObject.getJSONObject("data");
                Log.d("txt", data.toString());
                JSONArray x25175Text = data.getJSONArray("x25175Text");
                List weightList=new ArrayList();
                List txtList=new ArrayList();
                for (int i=0;i<x25175Text.length();i++) {
                    JSONObject data2=x25175Text.getJSONObject(i);
                    String id = data2.getString("id");
                    text = data2.getString("text");
                    String creatTime = data2.getString("creatTime");
                    String publicTime = data2.getString("publicTime");
                    String weight = data2.getString("weight");
//                    String sort = data2.getString("sort");
                    String remark = data2.getString("remark");
                    weightList.add(weight);
                    txtList.add(text);
                }
                Log.d("list", txtList.toString());
                Message msg = new Message();
                msg.what = COMPLETED;
                handler.sendMessage(msg);
            }else{
                String msg=jsonObject.getString("msg");
                Looper.prepare();
                Toast.makeText(MainActivity.this,msg,Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            if (msg.what==COMPLETED){
                tv_banner.setText(text);
                dismissLoadingDialog();
            }
        }
    };

    public void LoadVideo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int code;
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(""+device);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("content-type", "application/json");
                    connection.setRequestProperty("charset", "UTF-8");
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    Log.d("save", url.toString());
                    if (connection.getResponseCode() == 200) {
                        InputStream in = connection.getInputStream();

                        //下面对获取到的输入流进行读取
                        reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        Log.d("PersonalActivity",response.toString());
                        parseVideo(response.toString());
                    }else if (connection.getResponseCode() >= 400 ) {
                        InputStream in = connection.getErrorStream();

                        //下面对获取到的输入流进行读取
                        reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        String result = response.toString();
                        Log.d("PersonalActivity",result);
                    }

//                    else {
//                        Looper.prepare();
//                        Toast.makeText(MainActivity.this, String.valueOf(connection.getResponseCode()), Toast.LENGTH_LONG).show();
//                        Looper.loop();
//                    }


                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
    private void parseVideo(String response){
        try{
            //单条数据解析
            JSONObject jsonObject = new JSONObject(response);
            Integer code = jsonObject.getInt("code");
            if (code==200){
                JSONObject data=jsonObject.getJSONObject("data");
                JSONArray x25175Videos=data.getJSONArray("x25175Videos");
                Log.d("photo",x25175Videos.toString());
                list=new ArrayList();
                List weightList=new ArrayList();
                List videoList=new ArrayList();
                for (int i=0;i<x25175Videos.length();i++){
                    JSONObject data2=x25175Videos.getJSONObject(i);
                    String id=data2.getString("id");
                    String video=data2.getString("video");
                    String weight=data2.getString("weight");
                    String creatTime=data2.getString("creatTime");
                    String publicTime=data2.getString("publicTime");
                    weightList.add(weight);
                    videoList.add(video);

                }
                for (int a=0;a<videoList.size();a++){
                    download(videoList.get(a)+"");
                }
                String filename=videoList.get(videoList.size()-1).toString().substring(videoList.get(videoList.size()-1).toString().lastIndexOf("/"));
                File file=new File(FileUtil.cacheFile()+filename);
                if (!file.exists()){

                }else {
                    Log.d("weightlist", weightList.toString());
                    int we = 0;
                    List videolist = new ArrayList();
                    for (int a = 0; a < weightList.size(); a++) {
                        int w = Integer.parseInt(weightList.get(a).toString());
                        we = we + w;
                        for (int b = 0; b < Integer.parseInt(weightList.get(a).toString()); b++) {
                            videolist.add(videoList.get(a));
                        }
                    }
                    Log.d("videolist", videolist.get(0).toString().substring(videolist.get(0).toString().lastIndexOf("/")) + "");
                    for (int c = 0; c < we; c++) {
                        int r = (int) (Math.random() * videolist.size());
                        Log.d("r", r + "");
                        String filename2 = videolist.get(r).toString().substring(videolist.get(r).toString().lastIndexOf("/"));
                        File file1=new File(FileUtil.cacheFile()+filename2);
                        if (!file1.exists()){
                            list.add(new GSYVideoModel(videolist.get(r).toString(), filename2.replace("/", "")));
                            videolist.remove(r);
                        }else {
                            list.add(new GSYVideoModel(FileUtil.cacheFile() + filename2, filename2.replace("/", "")));
                            videolist.remove(r);
                        }

                    }
                    Log.d("we", we + "");
                    for (int s = 0; s < list.size(); s++) {
                        Log.d("1111", list.size() + "");
                        Log.d("list", list.get(s).getTitle().toString());
                    }

                    Message msg = new Message();
                    msg.what = COMPLETED;
                    handler3.sendMessage(msg);
                }

            }else {
                String msg=jsonObject.getString("msg");
                Looper.prepare();
                Toast.makeText(MainActivity.this,msg,Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void download(String path){
        String dirName=FileUtil.cacheFile();
        File file = new File(dirName);
        // 文件夹不存在时创建
        if (!file.exists()) {
            Log.d(TAG,"当前视频文件不存在，开始下载...");
            file.mkdir();
        }

        // 下载后的文件名
        int i = path.lastIndexOf("/"); // 取的最后一个斜杠后的字符串为名
        String fileName = dirName + path.substring(i, path.length());
        File file1 = new File(fileName);

        if (file1.exists()) {
            // 如果已经存在, 就不下载了

        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DOWNLOAD(path,fileName);
                }
            }).start();
        }
    }
    // 下载具体操作
    private void DOWNLOAD(String path,String fileName) {
        try {
            URL url = new URL(path);
            // 打开连接
            URLConnection conn = url.openConnection();
            // 打开输入流
            InputStream is = conn.getInputStream();
            // 创建字节流
            byte[] bs = new byte[1024*10];
            int len;
            OutputStream os = new FileOutputStream(fileName);
            // 写数据
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完成后关闭流
            Log.e("Main", "download-finish");
            LoadVideo();
            os.close();
            is.close();
            //            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Main", "e.getMessage() --- " + e.getMessage());
        }
    }

    private Handler handler3=new Handler(){
        @Override
        public void handleMessage(Message msg){
            if (msg.what==COMPLETED){
                String path=FileUtil.cacheFile();
                File file=new File(path);
                if (!file.exists()){
                    file.mkdir();
                }
                if (list!=null) {
                    detailPlayer.setUp(list, true, 0, file);
                    resolveNormalVideoUI();

                    detailPlayer.setIsTouchWiget(true);
                    //关闭自动旋转
                    detailPlayer.setRotateViewAuto(false);
                    detailPlayer.setLockLand(false);
                    detailPlayer.setShowFullAnimation(false);
                    //detailPlayer.setNeedLockFull(true);
                    detailPlayer.setAutoFullWithSize(true);

                    detailPlayer.setVideoAllCallBack(MainActivity.this);

                    detailPlayer.setLockClickListener(new LockClickListener() {
                        @Override
                        public void onClick(View view, boolean lock) {
                            if (orientationUtils != null) {
                                //配合下方的onConfigurationChanged
                                orientationUtils.setEnable(!lock);
                            }
                        }
                    });
                    dismissLoadingDialog();
                    detailPlayer.onAutoCompletion();
                }
            }
        }
    };

    @Override
    public ListGSYVideoPlayer getGSYVideoPlayer() {
        return detailPlayer;
    }

    @Override
    public GSYVideoOptionBuilder getGSYVideoOptionBuilder() {
        //不需要builder的
        return null;
    }

    @Override
    public void clickForFullScreen() {

    }

    /**
     * 是否启动旋转横屏，true表示启动
     *
     * @return true
     */
    @Override
    public boolean getDetailOrientationRotateAuto() {
        return true;
    }

    @Override
    public void onEnterFullscreen(String url, Object... objects) {
        super.onEnterFullscreen(url, objects);
        //隐藏调全屏对象的返回按键
        GSYVideoPlayer gsyVideoPlayer = (GSYVideoPlayer) objects[1];
        gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
    }


    private void resolveNormalVideoUI() {
        //增加title
        detailPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        detailPlayer.getBackButton().setVisibility(View.VISIBLE);
    }

    private GSYVideoPlayer getCurPlay() {
        if (detailPlayer.getFullWindowPlayer() != null) {
            return detailPlayer.getFullWindowPlayer();
        }
        return detailPlayer;
    }

    public void LoadPhoto(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int code;
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(""+device);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("content-type", "application/json");
                    connection.setRequestProperty("charset", "UTF-8");
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    Log.d("save", url.toString());
                    if (connection.getResponseCode() == 200) {
                        InputStream in = connection.getInputStream();

                        //下面对获取到的输入流进行读取
                        reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        Log.d("PersonalActivity",response.toString());
                        parsePhoto(response.toString());
                    }else if (connection.getResponseCode() >= 400 ) {
                        InputStream in = connection.getErrorStream();

                        //下面对获取到的输入流进行读取
                        reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        String result = response.toString();
                        Log.d("PersonalActivity",result);
                    }

//                    else {
//                        Looper.prepare();
//                        Toast.makeText(MainActivity.this, String.valueOf(connection.getResponseCode()), Toast.LENGTH_LONG).show();
//                        Looper.loop();
//                    }


                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
    private void parsePhoto(String response){
        try{
            //单条数据解析
            JSONObject jsonObject = new JSONObject(response);
            Integer code = jsonObject.getInt("code");
            if (code==200){
                JSONObject data=jsonObject.getJSONObject("data");
                JSONArray x25175Imgs=data.getJSONArray("x25175Imgs");
                Log.d("photo",x25175Imgs.toString());
                stringArrayList = new ArrayList<String>();
                List weightList=new ArrayList();
                List imgList=new ArrayList();
                for (int i=0;i<x25175Imgs.length();i++){
                    JSONObject data2=x25175Imgs.getJSONObject(i);
                    String id=data2.getString("id");
                    String img=data2.getString("img");
                    String weight=data2.getString("weight");
                    String creatTime=data2.getString("creatTime");
                    String publicTime=data2.getString("publicTime");
                    weightList.add(weight);
                    imgList.add(img);
                }
                Log.d("weightlist",weightList.toString());
                int we=0;
                List imglist=new ArrayList();
                for (int a=0;a<weightList.size();a++){
                    int w=Integer.parseInt(weightList.get(a).toString());
                    we=we+w;
                    for (int b=0;b<Integer.parseInt(weightList.get(a).toString());b++){
                        imglist.add(imgList.get(a)+"");
                    }
                }
                for (int c=0;c<we;c++) {
                    int r = (int) (Math.random() * imglist.size());
                    Log.d("r",r+"");
                    stringArrayList.add(imglist.get(r).toString());
                    imglist.remove(r);
                    Log.d("imglist",imglist+"");
                }
                Log.d("we",we+"");
                Log.d("stringArrayList",stringArrayList+"");
//                Looper.prepare();
//                Toast.makeText(MainActivity.this,list.toString(),Toast.LENGTH_LONG).show();
//                Looper.loop();
                Message msg = new Message();
                msg.what = COMPLETED;
                handler2.sendMessage(msg);

            }else {
                String msg=jsonObject.getString("msg");
                Looper.prepare();
                Toast.makeText(MainActivity.this,msg,Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Handler handler2=new Handler(){
        @Override
        public void handleMessage(Message msg){
            if (msg.what==COMPLETED){
//                Collections.reverse(stringArrayList);
                urls= stringArrayList.toArray(new String[stringArrayList.size()]);
                List list = Arrays.asList(urls);
                Log.d("images-----",list.toString());
                images = new ArrayList(list);

                banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
                //简单使用
                banner.setImages(images)
                        .setImageLoader(new GlideImageLoader())
                        .setOnBannerListener(MainActivity.this)
                        .start();
                dismissLoadingDialog();
            }
        }
    };

    @Override
    public void OnBannerClick(int position) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingView != null && loadingView.isShowing()) {
            loadingView.dismiss();
            loadingView = null;
        }
    }

    private void initLoadingView() {
        if (loadingView == null) {
            loadingView = LoadingView.getNewInstance(this);
        }
    }

    /**
     * 显示加载dialog
     *
     * @param info
     */
    protected final void showLoadingDialog(String info) {
        if (!isFinishing()) {
            loadingView.show(info);
        }
    }

    /**
     * 隐藏加载dialog
     */
    protected final void dismissLoadingDialog() {
        if (loadingView != null) {
            loadingView.dismiss();
        }
    }


    protected final void setLoadViewCancelable(boolean cancelable) {
        if (loadingView != null) {
            loadingView.setCancelable(cancelable);
        }
    }

    public void setTime(View view) {
        DateTimepickerDialog datetimedialog = new DateTimepickerDialog(this,System.currentTimeMillis(),minute1,second1);
/**
 *   * 实现接口
 */
        datetimedialog.setOnDateTimeSetListener(new DateTimepickerDialog.OnDateTimeSetListener() {
            public void OnDateTimeSet(DialogInterface dialog, String datetimestr, int minute, int second) {
//                CommToast.show(datetimestr);
                tv_time.setText(datetimestr);
                minute1=minute;
                second1=second;
                mTimer.stop();
                time(tv_time);
            }
        });
        datetimedialog.show();
    }
    public void time(TextView tv_time){
        if (mTimer==null) {
            String str = tv_time.getText().toString();
            String m = str.substring(0, str.indexOf(":"));
            String s = str.substring(str.indexOf(":") + 1);
            int time = Integer.parseInt(m) * 60 + Integer.parseInt(s);
            long millisInFuture = Long.parseLong(String.valueOf(time * 1000));
            mTimer = new CountDownTimerSupport(millisInFuture, 1000);
            mTimer.setOnCountDownTimerListener(new OnCountDownTimerListener() {
                @Override
                public void onTick(long millisUntilFinished) {
                    //间隔回调
                    tv_time.setText(com.example.administrator.video.Utils.TimeUtils.secToTime(Integer.parseInt(String.valueOf(millisUntilFinished / 1000))));
                }

                @Override
                public void onFinish() {
                    //计时器停止

                    initVideo();
                    LoadTXT();
                    LoadVideo();
                    LoadPhoto();
                    mTimer.reset();
                    mTimer.start();
                }
            });
            mTimer.start();
        }else {
            mTimer.stop();
            mTimer.reset();
            String str = tv_time.getText().toString();
            String m = str.substring(0, str.indexOf(":"));
            String s = str.substring(str.indexOf(":") + 1);
            int time = Integer.parseInt(m) * 60 + Integer.parseInt(s);
            long millisInFuture = Long.parseLong(String.valueOf(time * 1000));
            mTimer = new CountDownTimerSupport(millisInFuture, 1000);
            mTimer.setOnCountDownTimerListener(new OnCountDownTimerListener() {
                @Override
                public void onTick(long millisUntilFinished) {
                    //间隔回调
                    tv_time.setText(com.example.administrator.video.Utils.TimeUtils.secToTime(Integer.parseInt(String.valueOf(millisUntilFinished / 1000))));
                }

                @Override
                public void onFinish() {
                    //计时器停止
                    initVideo();
                    LoadTXT();
                    LoadVideo();
                    LoadPhoto();
                    mTimer.reset();
                    mTimer.start();
                }
            });
            mTimer.start();
        }
    }

    public void set(View view) {
        mTimer.stop();
        mTimer.setOnCountDownTimerListener(new OnCountDownTimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    public void setDateTime(View view) {
        calendars = Calendar.getInstance();
        calendars.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String year = String.valueOf(calendars.get(Calendar.YEAR));
        month = String.valueOf(calendars.get(Calendar.MONTH));
        if (Integer.parseInt(month)<10){
            month = "0"+String.valueOf(calendars.get(Calendar.MONTH));
        }else{
            month = String.valueOf(calendars.get(Calendar.MONTH));
        }
        day = String.valueOf(calendars.get(Calendar.DATE));
        if (Integer.parseInt(day)<10){
            day = "0"+String.valueOf(calendars.get(Calendar.DATE));
        }else{
            day = String.valueOf(calendars.get(Calendar.DATE));
        }
        hour = String.valueOf(calendars.get(Calendar.HOUR_OF_DAY));
        if (Integer.parseInt(hour)<10){
            hour = "0"+String.valueOf(calendars.get(Calendar.HOUR_OF_DAY));
        }else{
            hour = String.valueOf(calendars.get(Calendar.HOUR_OF_DAY));
        }
        min= String.valueOf(calendars.get(Calendar.MINUTE));
        if (Integer.parseInt(min)<10){
            min = "0"+String.valueOf(calendars.get(Calendar.MINUTE));
        }else{
            min = String.valueOf(calendars.get(Calendar.MINUTE));
        }
        String second = String.valueOf(calendars.get(Calendar.SECOND));
        DateTimePickDialogUtil dateTimePicker=new DateTimePickDialogUtil(
                MainActivity.this,year+"年"+month+"月"+day+"日 "+hour+":"+min);
        dateTimePicker.dateTimePickDialog(tv_dateTime);
        time();
    }

    public void time(){
        new TimeThread().start();
    }

    public void download(View view) {
        showLoadingDialog("");
        LoadVideo();
    }

    class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;  //消息(一个整型值)
                    mHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    //在主线程里面处理消息并更新UI界面
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    calendars = Calendar.getInstance();
                    calendars.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
                    String year = String.valueOf(calendars.get(Calendar.YEAR));
                    month = String.valueOf(calendars.get(Calendar.MONTH));
                    if (Integer.parseInt(month)<10){
                        month = "0"+String.valueOf(calendars.get(Calendar.MONTH));
                    }else{
                        month = String.valueOf(calendars.get(Calendar.MONTH));
                    }
                    day = String.valueOf(calendars.get(Calendar.DATE));
                    if (Integer.parseInt(day)<10){
                        day = "0"+String.valueOf(calendars.get(Calendar.DATE));
                    }else{
                        day = String.valueOf(calendars.get(Calendar.DATE));
                    }
                    hour = String.valueOf(calendars.get(Calendar.HOUR_OF_DAY));
                    if (Integer.parseInt(hour)<10){
                        hour = "0"+String.valueOf(calendars.get(Calendar.HOUR_OF_DAY));
                    }else{
                        hour = String.valueOf(calendars.get(Calendar.HOUR_OF_DAY));
                    }
                    min= String.valueOf(calendars.get(Calendar.MINUTE));
                    if (Integer.parseInt(min)<10){
                        min = "0"+String.valueOf(calendars.get(Calendar.MINUTE));
                    }else{
                        min = String.valueOf(calendars.get(Calendar.MINUTE));
                    }
                    String second = String.valueOf(calendars.get(Calendar.SECOND));
                    time=year+"年"+month+"月"+day+"日 "+hour+":"+min;
//                    tv_time.setText(time);
                    if (second.equals("0")) {
                        Log.d("second",second);
                        if (time.equals(tv_dateTime.getText().toString())) {
                            initVideo();
                            LoadTXT();
                            LoadVideo();
                            LoadPhoto();
                        }
                    }
             break;
             default:break;

            }
        }
    };

}
