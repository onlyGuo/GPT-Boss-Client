package com.guoshengkai.gpt.cn.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    /**
     * 计算字符串的MD5值
     * @param text 需要计算的字符串
     * @return 字符串MD5值
     */
    public static String getMD5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5"); // 获取MD5实例
            md.update(text.getBytes()); // 更新输入的字符串使用bytes数组
            byte[] digest = md.digest(); // 完成哈希计算，得到md5数组
            BigInteger bi = new BigInteger(1, digest); // 将md5数组转换为BigInteger类型的数据
            return bi.toString(16); // 以16进制形式进行输出
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}
