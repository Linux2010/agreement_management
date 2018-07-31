package com.rytc.common.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;

public class HtmlAESUtil {
    private static final String encoding = "UTF-8";
    private static final String AES_CBC_PKC_ALG = "AES/CBC/PKCS5Padding";
    private static final byte[] AES_IV = initIV(AES_CBC_PKC_ALG);
    private static final String RSA_ECB_PKCS1 = "RSA/ECB/PKCS1Padding";

    public static String decode(String content, String password)throws Exception {

       // byte[] byteContent = Base64Utils.decode(content);
        byte[] byteContent = decode(content.getBytes(encoding));
        byte[] enCodeFormat = password.getBytes(encoding);
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance(AES_CBC_PKC_ALG);// 创建密码器
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(AES_IV));// 初始化
        byte[] result = cipher.doFinal(byteContent);
        return new String(result, encoding);



    }
    private static byte[] initIV(String aesCbcPkcAlg) {
        Cipher cp;
        try {
            cp = Cipher.getInstance(aesCbcPkcAlg);
            int blockSize = cp.getBlockSize();
            byte[] iv = new byte[blockSize];
            for (int i = 0; i < blockSize; ++i) {
                iv[i] = 0;
            }
            return iv;

        } catch (Exception e) {
            int blockSize = 16;
            byte[] iv = new byte[blockSize];
            for (int i = 0; i < blockSize; ++i) {
                iv[i] = 0;
            }
            return iv;
        }
    }
    public static byte[] decode(byte[] var0) throws RuntimeException {
        ByteArrayInputStream var1 = new ByteArrayInputStream(var0);
        ByteArrayOutputStream var2 = new ByteArrayOutputStream();

        try {
            decode((InputStream)var1, (OutputStream)var2);
        } catch (IOException var14) {
            throw new RuntimeException("Unexpected I/O error", var14);
        } finally {
            try {
                var1.close();
            } catch (Throwable var13) {
                ;
            }

            try {
                var2.close();
            } catch (Throwable var12) {
                ;
            }

        }

        return var2.toByteArray();
    }
    public static void decode(InputStream var0, OutputStream var1) throws IOException {
        copy(new Base64InputStream(var0), var1);
    }
    private static void copy(InputStream var0, OutputStream var1) throws IOException {
        byte[] var2 = new byte[1024];

        int var3;
        while((var3 = var0.read(var2)) != -1) {
            var1.write(var2, 0, var3);
        }

    }

}
