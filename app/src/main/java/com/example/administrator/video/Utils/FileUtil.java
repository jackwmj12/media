package com.example.administrator.video.Utils;

import android.os.Environment;

import java.io.File;

public class FileUtil {
    public static String cacheFile(){
        // 创建文件夹，在存储卡下
        String dirName = Environment.getExternalStorageDirectory() + "/com.video" ;

        return dirName;
    }
}
