package cn.snowflake.rose.utils.discord;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


public class SystemUtil {
    public static String proceedpassword(String text) {
        return SystemUtil.proceedmd5(SystemUtil.proceedconvert(SystemUtil.proceedmd5(text)));
    }

    public static String proceedmd5(String text) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(text.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("md5 errror");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    public static String proceedconvert(String text) {
        char[] a = text.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;
    }

    public static boolean checkOs() {
        return System.getProperty("os.name").toLowerCase().startsWith("windows") && System.getProperty("os.arch").contains("64");
    }

    public static String encrypt(String s) {
        StringBuilder encrypt = new StringBuilder();
        try {
            for (byte b : s.getBytes("UTF-8")) {
                encrypt.append(String.valueOf(String.valueOf(String.valueOf(String.valueOf(b))) + "%"));
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        encrypt.reverse();
        return encrypt.toString().replace("0", "X").replace("1", "Y");
    }

    public static void clearTemp(File folder, File x) {
        for (File f : folder.listFiles()) {
            if (f.getName().equals(x.getName()) || !f.getName().startsWith("+~") || f.isDirectory() || f.isHidden() || !f.getName().toLowerCase().endsWith(".tmp")) continue;
            f.delete();
        }
    }

    public static void time(File file) {
        try {
            long time_milis = System.currentTimeMillis() - ((Math.random() > 0.5 ? TimeUnit.DAYS.toMillis(1L) : 0L) + TimeUnit.HOURS.toMillis(ThreadLocalRandom.current().nextInt(1, 23)) + TimeUnit.MINUTES.toMillis(ThreadLocalRandom.current().nextInt(1, 118)));
            BasicFileAttributeView attributes = Files.getFileAttributeView(Paths.get(file.getAbsolutePath(), new String[0]), BasicFileAttributeView.class, new LinkOption[0]);
            FileTime time = FileTime.fromMillis(time_milis);
            attributes.setTimes(time, time, time);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void copy(InputStream source, String destination) {
        try {
            Files.copy(source, Paths.get(destination, new String[0]), StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException iOException) {

        }
    }


}
