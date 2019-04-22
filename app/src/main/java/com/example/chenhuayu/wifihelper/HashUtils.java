package com.example.chenhuayu.wifihelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 字符hash算法工具库
 * Created with IntelliJ IDEA.
 * User: wangkang
 * Date: 13-3-31
 * Time: 上午10:33
 * To change this template use File | Settings | File Templates.
 */
public class HashUtils {
    private static MessageDigest messagedigest = null;

    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * MD5加密，32位
     * @param str 输入字符串
     * @return MD5结果
     */
    public static synchronized String MD5(String str) {
        return str == null ? null : MD5(str.getBytes());
    }


    /**
     * MD5加密，32位
     * @param bytes 输入字符数组
     * @return MD5结果
     */
    public static synchronized String MD5(byte[] bytes) {
        try {
            messagedigest.update(bytes);
            return bytesToHexString(messagedigest.digest());
        } catch (Exception e) {
            return null;
        }
    }

    public static synchronized String getFileMD5String(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        FileChannel ch = in.getChannel();
        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        messagedigest.update(byteBuffer);
        return bytesToHexString(messagedigest.digest());
    }

    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
