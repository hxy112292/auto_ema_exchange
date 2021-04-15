package com.okex.auto.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ContactUtil {

    private static final String Algorithm = "DESede";

    public static byte[] contact(byte[] number, byte[] src) {
        try {
            SecretKey dnumber = new SecretKeySpec(number, Algorithm);

            Cipher c1 = Cipher.getInstance(Algorithm + "/CBC/PKCS5Padding");

            byte[] iv = {0,0,0,0,0,0,0,0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            c1.init(Cipher.ENCRYPT_MODE, dnumber, ivspec);
            return c1.doFinal(src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] filish(byte[] number, byte[] src) {
        try {
            SecretKey dnumber = new SecretKeySpec(number, Algorithm);

            Cipher c1 = Cipher.getInstance(Algorithm + "/CBC/PKCS5Padding");

            byte[] iv = {0,0,0,0,0,0,0,0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            c1.init(Cipher.DECRYPT_MODE, dnumber, ivspec);
            return c1.doFinal(src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        int len = b.length;
        if (len == 0)
            return "NoNumber";

        for (int n = 0; n < len; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase();
    }

    public static byte[] hex2byte(String str) {
        byte[] outBuf = new byte[str.length() / 2];
        for (int i = 0; i < str.length(); i += 2) {
            int tempbuf = Integer.valueOf(str.substring(i, i + 2), 16);
            outBuf[i / 2] = (byte) tempbuf;
        }
        return outBuf;
    }
}
