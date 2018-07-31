package com.rytc.common.util;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * RSA加密解密工具
 * 
 *@author lzp
 * 2018年6月12日
 */
public class RSAUtils {
	private static String RSA = "RSA";
    public static String rsaPubkey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuXdV8nsm4UFloHiuyll2SKsytmIjZEgzL37QD+BPsoh+ilbC8LdNfSx0PrE+ncG89LXYSr3rHLZgj7KilhLK8EQitgNwuIG24gZmL7BXh4UquLCoNX2mAEdgX3oONJWkfJja2/3RCwA/XEQ8GVXU+eCfZuOY/8UFMXmAfwXKXYkphJV5CYRVjDAuFfQcO0nB/a988U6xdTha+HQCmWNNeWMIYks819qH++ftlua2iMGCrgMIP11IAgreTtOLjtu00SZbQHdMikOhN92aXCAYrv1H78/MTqN27H7fvbnGOpHtdM6Fw5iOf5i+Iylz41eIsbgI5WL4Wl9pTerFC5adWwIDAQAB";
    private static String BASE64UTILS_STR2 = "US-ASCII";
    private static String BASE64UTILS_STR1 = "iso8859-1";
    protected static final String ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";//加密填充方式

   
    /**
     * 加密
     *
     * @param data
     * @return
     */
    public static String encode(byte[] data) {
        StringBuffer sb = new StringBuffer();
        int len = data.length;
        int i = 0;
        int b1, b2, b3;
        while (i < len) {
            b1 = data[i++] & 0xff;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
                sb.append("==");
                break;
            }
            b2 = data[i++] & 0xff;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
                sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
                sb.append("=");
                break;
            }
            b3 = data[i++] & 0xff;
            sb.append(base64EncodeChars[b1 >>> 2]);
            sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
            sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
            sb.append(base64EncodeChars[b3 & 0x3f]);
        }
        return sb.toString();
    }

    /**
     * 解密
     *
     * @param str
     * @return
     */
    public static byte[] decode(String str) {
        try {
            return decodePrivate(str);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new byte[]
                {};
    }

    /**
     * 用公钥加密 <br>
     * 每次加密的字节数，不能超过密钥的长度值减去11
     *
     * @param data      需加密数据的byte数据
     * @param publicKey 公钥
     * @return 加密后的byte型数据
     */
    public static byte[] encryptData(byte[] data, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            // 编码前设定编码方式及密钥
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 传入编码数据并返回编码结果
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过公钥byte[](publicKey.getEncoded())将公钥还原，适用于RSA算法
     *
     * @param keyBytes
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PublicKey getPublicKey(byte[] keyBytes) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }
    private static byte[] decodePrivate(String str) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        byte[] data = null;
        data = str.getBytes(BASE64UTILS_STR2);
        int len = data.length;
        int i = 0;
        int b1, b2, b3, b4;
        while (i < len) {

            do {
                b1 = base64DecodeChars[data[i++]];
            } while (i < len && b1 == -1);
            if (b1 == -1)
                break;

            do {
                b2 = base64DecodeChars[data[i++]];
            } while (i < len && b2 == -1);
            if (b2 == -1)
                break;
            sb.append((char) ((b1 << 2) | ((b2 & 0x30) >>> 4)));

            do {
                b3 = data[i++];
                if (b3 == 61)
                    return sb.toString().getBytes(BASE64UTILS_STR1);
                b3 = base64DecodeChars[b3];
            } while (i < len && b3 == -1);
            if (b3 == -1)
                break;
            sb.append((char) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));

            do {
                b4 = data[i++];
                if (b4 == 61)
                    return sb.toString().getBytes(BASE64UTILS_STR1);
                b4 = base64DecodeChars[b4];
            } while (i < len && b4 == -1);
            if (b4 == -1)
                break;
            sb.append((char) (((b3 & 0x03) << 6) | b4));
        }
        return sb.toString().getBytes(BASE64UTILS_STR1);
    }
    private static char[] base64EncodeChars = new char[]
            {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                    'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                    'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',
                    '6', '7', '8', '9', '+', '/'};

    private static byte[] base64DecodeChars = new byte[]
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53,
                    54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
                    12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29,
                    30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1,
                    -1, -1, -1};
    
    public static String getEncryptData(String RSASign,String rsaKey) {
    	String encrptyString = "";
    	try {
    		//获取公钥
    		PublicKey publicKey = getPublicKey(decode(rsaKey));
    		//利用公钥对key进行加密
    		byte[] encrptyString1byte = encryptData(RSASign.getBytes(), publicKey);
            encrptyString = encode(encrptyString1byte);
    	}catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    	
    	return encrptyString;
    }
    
    
    public static void main(String[] args) {
//      rsaPubkey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuXdV8nsm4UFloHiuyll2SKsytmIjZEgzL37QD+BPsoh+ilbC8LdNfSx0PrE+ncG89LXYSr3rHLZgj7KilhLK8EQitgNwuIG24gZmL7BXh4UquLCoNX2mAEdgX3oONJWkfJja2/3RCwA/XEQ8GVXU+eCfZuOY/8UFMXmAfwXKXYkphJV5CYRVjDAuFfQcO0nB/a988U6xdTha+HQCmWNNeWMIYks819qH++ftlua2iMGCrgMIP11IAgreTtOLjtu00SZbQHdMikOhN92aXCAYrv1H78/MTqN27H7fvbnGOpHtdM6Fw5iOf5i+Iylz41eIsbgI5WL4Wl9pTerFC5adWwIDAQAB";
      String RSASign = "CUST_NAME=朱心勇&CERT_CODE=511321198903061599&APP_AMT=1000.00&LOAN_CARD_NO=6222082806001965974&REPAY_CARD_NO=6222082806001965974";
//      String RSASign = "aaaaaaaaaaaaaaaa";
      String encrptyString1 = "";
//      // 5, 获取公钥
      PublicKey publicKey = null;
      try {
          publicKey = getPublicKey(decode(rsaPubkey));
          // 6, 利用公钥对key进行加密
          byte[] encrptyString1byte = encryptData(RSASign.getBytes(), publicKey);
          encrptyString1 = encode(encrptyString1byte);
          System.out.println(encrptyString1);
      } catch (NoSuchAlgorithmException e) {
          e.printStackTrace();
      } catch (InvalidKeySpecException e) {
          e.printStackTrace();
      }
//      String s=RSAEncryptData(RSASign, SDKConfig.rsaPubkeyLevelt);
//      String s=RSAEncryptData(RSASign, SDKConfig.rsaPubkey);
//      System.out.println(s);



  }
}
