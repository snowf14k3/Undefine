package cn.snowflake.rose.utils.verify;


import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.Sys;

import javax.swing.*;

/**
 *
 * @author Administrator
 *
 *
 *
 */
public class AntiReflex {
    private static final char[] hexArray = new String(new byte[] {78,85,77,66,69,82,95,79,70,95,80,82,79,67,69,83,83,79,82,83}).toCharArray();
    public void Test(){
        try {
            Class clazz = Class.forName("javax.swing.JOptionPane");
            String str1 = new String("未通过HWID验证！请复制以下的hwid提交给管理员");
            Method m = clazz.getDeclaredMethod("showInputDialog", Component.class, Object.class, Object.class);
            /**
             *  第一个参数 是调用的 方法Object
             *
             */
            m.invoke(m, null, str1, HWIDUtils.getHWID());
        } catch (ClassNotFoundException e) {
            LogManager.getLogger().error("NMSL");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
//        try {
//            Class clazz = Class.forName("javax.swing.JOptionPane");
//            String str1 = new String("未通过HWID验证！请复制以下的hwid提交给管理员");
//            Method m = clazz.getDeclaredMethod("showInputDialog", Component.class, Object.class, Object.class);
//            /**
//             *  第一个参数 是调用的 方法Object
//             *
//             */
//            m.invoke(m, null, str1, getHWID());
//        } catch (ClassNotFoundException e) {
//            LogManager.getLogger().error("NMSL");
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }


//        try {
//            Class clazz = Class.forName("javax.swing.JOptionPane");
//            String str = new String("没有通过HWID验证！请复制以下的hwid提交给管理员");
//            Method m = clazz.getDeclaredMethod("showInputDialog", Component.class,Object.class,Object.class);
//            m.invoke(m,null,str,getHWID());
//        } catch (ClassNotFoundException e) {
//            LogManager.getLogger().error("NMSL");
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
//       System.out.println("HWID : " + getHWID() +  " UserName: " + getUserName() + " Html : " + HttpUtils.sendGet("http://www.snowfake.cf/verify/hwid.nmsl"));
    }
    public static String https;

    public static String getUserName1() {
        NetworkUtil.checknetwork();
        String str = "";
        https = HttpUtils.sendGet("h"+"t"+"t"+"p"+":"+"/"+"/"+"w"+"w"+"w"+"."+"s"+"e"+"a"+"s"+"o"+"n"+"c"+"l"+"i"+"e"+"n"+"t"+"."+"c"+"f"+"/"+"r"+"o"+"s"+"e"+"/"+"h"+"w"+"i"+"d"+"."+"t"+"x"+"t");
        str = getSubString(https, getHWID()+"|", " ");

        return str;
    }
    public static String getUserName2() {
        NetworkUtil.checknetwork();
        String str = "";
        https = HttpUtils.sendGet("h"+"t"+"t"+"p"+":"+"/"+"/"+"w"+"w"+"w"+"."+"s"+"e"+"a"+"s"+"o"+"n"+"c"+"l"+"i"+"e"+"n"+"t"+"."+"c"+"f"+"/"+"r"+"o"+"s"+"e"+"/"+"h"+"w"+"i"+"d"+"."+"t"+"x"+"t");
        str = getSubString(https, getHWID()+"|", " ");
        return str;//https://cnsnowflake.gitee.io/seasonclient/rose/hwid.txt
    }
    public static String getUserName3() {
        NetworkUtil.checknetwork();
        String str = "";
        https = HttpUtils.sendGet("h"+"t"+"t"+"p"+":"+"/"+"/"+"w"+"w"+"w"+"."+"s"+"e"+"a"+"s"+"o"+"n"+"c"+"l"+"i"+"e"+"n"+"t"+"."+"c"+"f"+"/"+"r"+"o"+"s"+"e"+"/"+"h"+"w"+"i"+"d"+"."+"t"+"x"+"t");
        str = getSubString(https, getHWID()+"|", " ");

        return str;
    }
    public static String getUserName4() {
        NetworkUtil.checknetwork();
        String str = "";
        https = HttpUtils.sendGet("h"+"t"+"t"+"p"+":"+"/"+"/"+"w"+"w"+"w"+"."+"s"+"e"+"a"+"s"+"o"+"n"+"c"+"l"+"i"+"e"+"n"+"t"+"."+"c"+"f"+"/"+"r"+"o"+"s"+"e"+"/"+"h"+"w"+"i"+"d"+"."+"t"+"x"+"t");
        str = getSubString(https, getHWID()+"|", " ");
        return str;
    }
    public static byte[] generateHWID() {
        try {
            MessageDigest hash = MessageDigest.getInstance("MD5");
            StringBuilder s = new StringBuilder();
            s.append(encrypt32(en(System.getProperty("os.name"))));
            s.append(encrypt32(en(System.getProperty("os.arch"))));
            s.append(encrypt32(en(System.getProperty("os.version"))));
            s.append(Runtime.getRuntime().availableProcessors());
            return hash.digest(s.toString().getBytes());
        }
        catch (NoSuchAlgorithmException e) {
            throw new Error("Algorithm wasn't found.", e);
        }
    }

    //        public static String encodeBase64(byte[] input){
//            try {
//        	 Class clazz = Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
//                 Method mainMethod = clazz.getMethod("encode", byte[].class);
//                 mainMethod.setAccessible(true);
//                 Object retObj = mainMethod.invoke(null, new Object[]{input});
//                 return (String) retObj;
//	    } catch (Exception e) {
//		return null;
//	    }
//        }
    public void Test4(){
        try {
            Class clazz = Class.forName("javax.swing.JOptionPane");
            String str1 = new String("未通过HWID验证！请复制以下的hwid提交给管理员");
            Method m = clazz.getDeclaredMethod("showInputDialog", Component.class, Object.class, Object.class);
            /**
             *  第一个参数 是调用的 方法Object
             *
             */
            m.invoke(m, null, str1, HWIDUtils.getHWID());
        } catch (ClassNotFoundException e) {
            LogManager.getLogger().error("NMSL");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    public static byte[] decodeBase64(String input) {
        try {
            Class clazz = Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
            Method mainMethod = clazz.getMethod("decode", String.class);
            mainMethod.setAccessible(true);
            Object retObj = mainMethod.invoke(null, input);
            return (byte[]) retObj;
        } catch (Exception e) {
            return null;
        }
    }

    public static String encrypt32(String encryptStr) { MessageDigest md5; try { md5 = MessageDigest.getInstance("MD5"); byte[] md5Bytes = md5.digest(encryptStr.getBytes()); StringBuffer hexValue = new StringBuffer(); for (int i = 0; i < md5Bytes.length; i++) { int val = ((int) md5Bytes[i]) & 0xff; if (val < 16) hexValue.append("0"); hexValue.append(Integer.toHexString(val)); } encryptStr = hexValue.toString(); } catch (Exception e) { throw new RuntimeException(e); } return encryptStr; }

    public static String getHWID() {
        StringBuilder sb = new StringBuilder();
        String main = System.getenv("COMPUTERNAME") +
                System.getProperty("os.name") +
                System.getProperty("os.version") +
                System.getProperty("os.arch") +
                Runtime.getRuntime().availableProcessors()
                ;

        try {
            byte[] bytes = main.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5 = md.digest(bytes);
            int i = 0;
            for (byte b : md5) {
                sb.append(Integer.toHexString((b & 0xFF) | 0x300), 0, 3);
                if (i != md5.length - 1) {
                    sb.append("-");
                }
                i++;
            }
        } catch (Exception e) {

        }
        return sb.toString();
    }


    public static String en(final String a) {
        final int n = 2;
        final int n2 = n << n ^ 0x5;
        final int n3 = 3;
        final int n4 = n3 << n3 ^ 0x5;
        final int length = a.length();
        final char[] array = new char[length];
        int n5;
        int i = n5 = length - 1;
        final char[] array2 = array;
        final char c = (char)n4;
        final int n6 = n2;
        while (i >= 0) {
            final char[] array3 = array2;
            final int n7 = n5;
            final char char1 = a.charAt(n7);
            --n5;
            array3[n7] = (char)(char1 ^ n6);
            if (n5 < 0) {
                break;
            }
            final char[] array4 = array2;
            final int n8 = n5--;
            array4[n8] = (char)(a.charAt(n8) ^ c);
            i = n5;
        }
        return new String(array2);
    }

    public static String de(final byte[] a) {
        final char[] array = new char[a.length * 2];
        int n;
        int i = n = 0;
        while (i < a.length) {
            final int n2 = a[n] & 0xFF;
            final int n3 = n;
            final char[] array2 = array;
            array2[n * 2] = AntiReflex.hexArray[n2 >>> 4];
            final int n4 = n3 * 2 + 1;
            final char c = AntiReflex.hexArray[n2 & 0xF];
            ++n;
            array2[n4] = c;
            i = n;
        }
        return new String(array);
    }



    public static String getSubString(String text, String left, String right) {
        String result = "";
        int zLen;
        if (left == null || left.isEmpty()) {
            zLen = 0;
        } else {
            zLen = text.indexOf(left);
            if (zLen > -1) {
                zLen += left.length();
            } else {
                zLen = 0;
            }
        }
        int yLen = text.indexOf(right, zLen);
        if (yLen < 0 || right == null || right.isEmpty()) {
            yLen = text.length();
        }
        result = text.substring(zLen, yLen);
        return result;
    }
}

