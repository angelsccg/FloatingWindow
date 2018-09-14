package com.angels.world.tool;


import java.io.ByteArrayOutputStream;

import java.security.Key;

import java.security.KeyFactory;

import java.security.PublicKey;

import java.security.spec.X509EncodedKeySpec;



import javax.crypto.Cipher;





/**

 * RSA非对称加密解密工具类

 *

 * @ClassName RsaEncryptUtil

 * @author kokjuis 189155278@qq.com

 * @date 2016-4-6

 * @content

 */

public class RsaEncryptUtil {

    final static String str_pubK = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqPvovSfXcwBbW8cKMCgwqNpsYuzF8RPAPFb7LGsnVo44JhM/xxzDyzoYtdfNmtbIuKVi9PzIsyp6rg+09gbuI6UGwBZ5DWBDBMqv5MPdOF5dCQkB2Bbr5yPfURPENypUz+pBFBg41d+BC+rwRiXELwKy7Y9caD/MtJyHydj8OUwIDAQAB";
    final static String str_priK = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKo++i9J9dzAFtbxwowKDCo2mxi7MXxE8A8VvssaydWjjgmEz/HHMPLOhi1182a1si4pWL0/MizKnquD7T2Bu4jpQbAFnkNYEMEyq/kw904Xl0JCQHYFuvnI99RE8Q3KlTP6kEUGDjV34EL6vBGJcQvArLtj1xoP8y0nIfJ2Pw5TAgMBAAECgYAGGB8IllMwxceLhjf6n1l0IWRH7FuHIUieoZ6k0p6rASHSgWiYNRMxfecbtX8zDAoG0QAWNi7rn40ygpR5gS1fWDAKhmnhKgQIT6wW0VmD4hraaeyP78iy8BLhlvblri2nCPIhDH5+l96v7D47ZZi3ZSOzcj89s1eS/k7/N4peEQJBAPEtGGJY+lBoCxQMhGyzuzDmgcS1Un1ZE2pt+XNCVl2b+T8fxWJH3tRRR8wOY5uvtPiK1HM/IjT0T5qwQeH8Yk0CQQC0tcv3d/bDb7bOe9QzUFDQkUSpTdPWAgMX2OVPxjdq3Sls9oA5+fGNYEy0OgyqTjde0b4iRzlD1O0OhLqPSUMfAkEAh5FIvqezdRU2/PsYSR4yoAdCdLdT+h/jGRVefhqQ/6eYUJJkWp15tTFHQX3pIe9/s6IeT/XyHYAjaxmevxAmlQJBAKSdhvQjf9KAjZKDEsa7vyJ/coCXuQUWSCMNHbcR5aGfXgE4e45UtUoIE1eKGcd6AM6LWhx3rR6xdFDpb9je8BkCQB0SpevGfOQkMk5i8xkEt9eeYP0fi8nv6eOUcK96EXbzs4jV2SAoQJ9oJegPtPROHbhIvVUmNQTbuP10Yjg59+8=";


    /** */

    /**

     * 加密算法RSA

     */

    public static final String KEY_ALGORITHM = "RSA";// RSA/NONE/NoPadding,RSA/NONE/PKCS1Padding



    /**

     * String to hold name of the encryption padding.

     */

    public static final String PADDING = "RSA/NONE/PKCS1Padding";// RSA/NONE/NoPadding



    /**

     * String to hold name of the security provider.

     */

    public static final String PROVIDER = "BC";



    /** */

    /**

     * 签名算法

     */

    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";



    /** */

    /**

     * 获取公钥的key

     */

    private static final String PUBLIC_KEY = "RSAPublicKey";



    /** */

    /**

     * 获取私钥的key

     */

    private static final String PRIVATE_KEY = "RSAPrivateKey";



    /** */

    /**

     * RSA最大加密明文大小

     */

    private static final int MAX_ENCRYPT_BLOCK = 117;



    /** */

    /**

     * RSA最大解密密文大小

     */

    private static final int MAX_DECRYPT_BLOCK = 128;



    /*

     * 公钥加密

     */

    public static String encryptByPublicKey(String str) throws Exception {



        Cipher cipher = Cipher.getInstance(PADDING, PROVIDER);

        // 获得公钥

        Key publicKey = getPublicKey();



        // 用公钥加密

        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        // 读数据源

        byte[] data = str.getBytes("UTF-8");



        int inputLen = data.length;

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int offSet = 0;

        byte[] cache;

        int i = 0;

        // 对数据分段加密

        while (inputLen - offSet > 0) {

            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {

                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);

            } else {

                cache = cipher.doFinal(data, offSet, inputLen - offSet);

            }

            out.write(cache, 0, cache.length);

            i++;

            offSet = i * MAX_ENCRYPT_BLOCK;

        }

        byte[] encryptedData = out.toByteArray();

        out.close();



        return Base64Util.encode(encryptedData);

    }



    /*

     * 公钥解密

     */

    public static String decryptByPublicKey(String str) throws Exception {

        Cipher cipher = Cipher.getInstance(PADDING, PROVIDER);



        // 获得公钥

        Key publicKey = getPublicKey();



        // 用公钥解密

        cipher.init(Cipher.DECRYPT_MODE, publicKey);



        // 读数据源

        byte[] encryptedData = Base64Util.decode(str);



        int inputLen = encryptedData.length;

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int offSet = 0;

        byte[] cache;

        int i = 0;

        // 对数据分段解密

        while (inputLen - offSet > 0) {

            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {

                cache = cipher

                        .doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);

            } else {

                cache = cipher

                        .doFinal(encryptedData, offSet, inputLen - offSet);

            }

            out.write(cache, 0, cache.length);

            i++;

            offSet = i * MAX_DECRYPT_BLOCK;

        }

        byte[] decryptedData = out.toByteArray();

        out.close();



        return new String(decryptedData, "UTF-8");

    }



    /**

     * 读取公钥

     *

     * @return

     * @throws Exception

     * @author kokJuis

     * @date 2016-4-6 下午4:38:22

     * @comment

     */

    private static Key getPublicKey() throws Exception {

//        String key = Constant.rsa_public_key;
        String key = str_pubK;

        byte[] keyBytes;

        keyBytes = Base64Util.decode(key);

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        return publicKey;

    }



}

