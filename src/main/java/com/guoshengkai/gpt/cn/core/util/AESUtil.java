package com.guoshengkai.gpt.cn.core.util;


import com.guoshengkai.gpt.cn.core.JSON;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

public class AESUtil {

    /**
     * 加密
     * @param content
     *      待加密字节
     * @param key
     *      密钥
     * @return
     *      加密后的字节
     */
    public static byte[] encrypt(byte[] content, String key) {
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES");
            // 初始化
            cipher.init(Cipher.ENCRYPT_MODE, getKey(key));
            // 加密
            return cipher.doFinal(content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密
     * @param content
     *     待解密字节
     *     @param key
     *     密钥
     *     @return
     *     解密后的字节
     */
    public static byte[] decrypt(byte[] content, String key) {
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES");
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE, getKey(key));
            // 解密
            return cipher.doFinal(content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加密对象
     * @param obj
     *      待加密对象
     * @param key
     *      密钥
     * @return
     *      加密后的字节
     */
    public static byte[] encryptObject(Object obj, String key) {
        return encrypt(JSON.stringify(obj).getBytes(StandardCharsets.UTF_8), key);
    }

    /**
     * 解密对象
     * @param content
     *      待解密字节
     * @param key
     *      密钥
     * @return
     *      解密后的对象
     */
    public static Object decryptObject(byte[] content, String key) {
        return JSON.parse(new String(decrypt(content, key), StandardCharsets.UTF_8));
    }

    /**
     * 解密对象
     * @param content
     *      待解密字节
     * @param key
     *      密钥
     * @return
     *      解密后的对象
     */
    public static <T> T decryptModel(byte[] content, String key, Class<T> clazz) {
        return JSON.parse(new String(decrypt(content, key), StandardCharsets.UTF_8), clazz);
    }

    /**
     * AES方式加密对象到字符串
     * @param obj
     *      待加密的对象
     * @param key
     *      加密密钥
     * @return
     *      加密后的字符串
     */
    public static String encryptObjectToString(Object obj, String key) {
        return Base64.getEncoder().encodeToString(AESUtil.encryptObject(obj, key));
    }

    /**
     * AES方式解密字符串到对象
     * @param str
     *     待解密的字符串
     * @param key
     *    解密密钥
     * @return
     *      解密后的对象
     */
    public static Object decryptStringToObject(String str, String key) {
        return AESUtil.decryptObject(Base64.getDecoder().decode(str), key);
    }

    /**
     * AES方式解密字符串到对象
     * @param str
     *    待解密的字符串
     * @param key
     *    解密密钥
     * @return
     *    解密后的对象
     */
    public static<T> T decryptStringToModel(String str, String key, Class<T> clazz) {
        return AESUtil.decryptModel(Base64.getDecoder().decode(str), key, clazz);
    }

    /**
     * 获取密钥
     * @param key
     *      密钥字符串
     * @return
     *      密钥
     */
    private static Key getKey(String key) {
        try {
            // 实例化密钥生成器
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            // 初始化密钥生成器
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(key.getBytes(StandardCharsets.UTF_8));
            keygen.init(128, secureRandom);
            // 产生密钥
            SecretKey secretKey = keygen.generateKey();
            // 获取密钥
            byte[] enCodeFormat = secretKey.getEncoded();
            return new SecretKeySpec(enCodeFormat, "AES");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
