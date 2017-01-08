package com.harlan.smonitor.monitor.common;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AESUtil
 * Created by harlan on 2017/1/7.
 */
public class AESUtil {
    /**
     * AES 加密
     * @param input
     * @param key AES可以使用128位密钥
     * @return
     * @throws Exception
     */
    public static String encrypt(String input, String key) throws Exception {
        byte[] crypted ;
        SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skey);
        crypted = cipher.doFinal(input.getBytes());
        return new String(Base64.encodeBase64(crypted));
    }

    public static String decrypt(String input, String key)throws Exception{
        byte[] output;
        SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skey);
        output = cipher.doFinal(Base64.decodeBase64(input));
        return new String(output);
    }

    public static void main(String[] args) {
        String key = "04c3cddbb8a4e96b";
        String data = "example";
        try {
            System.out.println("key:"+key.getBytes().length);
            System.out.println(AESUtil.encrypt(data, key));
            System.out.println(AESUtil.decrypt(AESUtil.encrypt(data, key), key));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
