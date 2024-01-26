package com.guoshengkai.gpt.cn.util;

public class CodeUtil {

    public static String getVerifyCode(){
        return String.valueOf((int)((Math.random()*9+1)*100000));
    }

    /**
     * 脱敏
     * @param text
     *      等待脱敏的字符串
     * @return
     */
    public static String desensitization(String text) {
        if(text == null || text.length() < 2){
            return text;
        }
        if(text.length() < 3){
            return text.substring(0, 1) + "*";
        }
        if(text.length() < 4){
            return text.substring(0, 1) + "**";
        }
        if (text.contains("@")) {
            String[] split = text.split("@");
            if (split[0].length() < 2){
                return "*" + "@" + split[1];
            }
            return desensitization(split[0]) + "@" + split[1];
        }
        if (text.length() < 6){
            return text.substring(0, 2) + "***";
        }
        if (text.length() < 8){
            return text.substring(0, 4) + "***";
        }
        String substring = text.substring(text.length() - 3);
        if (text.length() < 10){
            return text.substring(0, 3) + "***" + substring;
        }
        return text.substring(0, 3) + "****" + substring;
    }
}
