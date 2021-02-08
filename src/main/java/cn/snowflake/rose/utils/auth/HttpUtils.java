package cn.snowflake.rose.utils.auth;


import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HttpUtils {

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
    public void Test2(){
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
    public void Test1(){
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
    public void Test3(){
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
    public static String sendGet(String url) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result = "";
        try {
            // 通过址默认配置创建一个httpClient实例
            httpClient = HttpClients.createDefault();
            // 创建httpGet远程连接实例
            HttpGet httpGet = new HttpGet(url);
            // 设置请求头信息，鉴权
            // 设置配置请求参数
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 连接主机服务超时时间
                    .setConnectionRequestTimeout(35000)// 请求超时时间
                    .setSocketTimeout(60000)// 数据读取超时时间
                    .build();
            // 为httpGet实例设置配置
            httpGet.setConfig(requestConfig);
            httpGet.setHeader("User-Agent", "Mozilla/5.0 AppIeWebKit");
            // 执行get请求得到返回对象
            response = httpClient.execute(httpGet);
            // 通过返回对象获取返回数据
            HttpEntity entity = response.getEntity();
            // 通过EntityUtils中的toString方法将结果转换为字符串
            result = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
