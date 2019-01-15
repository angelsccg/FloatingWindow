package com.angels.library.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AcFileUtil {
    /**
     * 通过文件路径直接修改文件名
     * @param filePath 需要修改的文件的完整路径
     * @param newFileName 需要修改的文件的名称
     * @return
     */
    public static String FixFileName(String filePath, String newFileName) {
        File f = new File(filePath);
        if (!f.exists()) { // 判断原文件是否存在
            return null;
        }

        newFileName = newFileName.trim();
        if ("".equals(newFileName) || newFileName == null) // 文件名不能为空
            return null;

        String newFilePath = null;
        if (f.isDirectory()) { // 判断是否为文件夹
            newFilePath = filePath.substring(0, filePath.lastIndexOf("/")) + "/" + newFileName;
        } else {
            newFilePath = filePath.substring(0, filePath.lastIndexOf("/"))+ "/"  + newFileName + filePath.substring(filePath.lastIndexOf("."));
        }
        File nf = new File(newFilePath);
        if (!nf.exists()) { // 判断需要修改为的文件是否存在（防止文件名冲突）
            return null;
        }

        try {
            f.renameTo(nf); // 修改文件名
        } catch(Exception err) {
            err.printStackTrace();
            return null;
        }

        return newFilePath;
    }

    /**
     *
     * @param srcFromPath 源文件路径
     * @param height
     * @param width
     * @return
     */
    public static String compressImage(String srcFromPath, float height,float width) {
        String result = "压缩失败";
        File f = new File(srcFromPath);
        if (!f.exists()) { // 判断原文件是否存在
            result = "原图片不存在";
            return result;
        }
        float hh = height;
        float ww = width;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        opts.inJustDecodeBounds = false;
        int w = opts.outWidth;
        int h = opts.outHeight;
        int size = 0;
        if (w <= ww && h <= hh) {
            size = 1;
        } else {
            double scale = w >= h ? w / ww : h / hh;
            double log = Math.log(scale) / Math.log(2);
            double logCeil = Math.ceil(log);
            size = (int) Math.pow(2, logCeil);
        }
        opts.inSampleSize = size;
        Bitmap bitmap = BitmapFactory.decodeFile(srcFromPath, opts);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        System.out.println(baos.toByteArray().length);
        while (baos.toByteArray().length > 45 * 1024) {
            if(quality <= 0 || quality > 100){
                break;
            }
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            quality -= 20;
            AcLogUtil.i("文件-->压缩图片-->长度：" + baos.toByteArray().length);
        }
        try {
//            baos.writeTo(new FileOutputStream("/mnt/sdcard/Servyou/photo/buffer/22.jpg"));
            baos.writeTo(new FileOutputStream(srcFromPath));
            result = "压缩成功";
        } catch (Exception e) {
            e.printStackTrace();
            result = "压缩失败";
        } finally {
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
