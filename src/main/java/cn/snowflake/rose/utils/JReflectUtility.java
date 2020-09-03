package cn.snowflake.rose.utils;

import cn.snowflake.rose.asm.ClassTransformer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Timer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//Coded for SNOW_FLAKES.
public class JReflectUtility {
    //Cache fields and methods makes less work finding them.
    private static Map<String, Field> cachedFields;
    private static Map<String, Method> cachedMethods;


    public static Object getMethodAsObject(Class<?> inClass, String name,
                                           boolean secureAccess, Object... parameter) {
        try {
            return Objects.requireNonNull(getMethod(inClass, name, secureAccess))
                    .invoke(inClass.newInstance(), parameter);
        } catch (IllegalAccessException | InvocationTargetException
                | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Object getMethodAsObject(Class<?> inClass, Object instance,
                                           String name, Object... parameter){
        try{
            return Objects.requireNonNull(getMethod(inClass, name, true))
                    .invoke(instance, parameter);
        }catch (IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
            return null;
        }
    }

    public static Object getMethodAsObject(Class<?> inClass, Object instance,
                                           String name, boolean secureAccess, Object... parameter){
        try{
            return Objects.requireNonNull(getMethod(inClass, name, secureAccess))
                    .invoke(instance, parameter);
        }catch (IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
            return null;
        }
    }

    public static Method getMethod(Class<?> inClass, String name, boolean secureAccess){
        if(cachedMethods.containsKey(getUniquePath(inClass, name)))
            return cachedMethods.get(getUniquePath(inClass, name));

        for (Method method : inClass.getDeclaredMethods())
            if(method.getName().equals(name)) {
                if(secureAccess && !method.isAccessible())
                    method.setAccessible(true);
                cachedMethods.put(getUniquePath(inClass, name), method);
                return method;
            }
        return null;
    }

    //We don't provide auto-instance-generator for fields.
    //Non-static fields may be not init-ed
    public static boolean getFieldAsBoolean(Class<?> inClass, Object instance, String name){
        try{
            //All of them requires to not be null.
            return Objects.requireNonNull(getField(inClass, name, true)).getBoolean(instance);
        }catch (IllegalAccessException e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean getFieldAsBoolean(Class<?> inClass, Object instance, String name,
                                            boolean secureAccess){
        try{
            return Objects.requireNonNull(getField(inClass, name,secureAccess)).getBoolean(instance);
        }catch (IllegalAccessException e){
            e.printStackTrace();
            return false;
        }
    }

    public static Object getFieldAsObject(Class<?> inClass, Object instance, String name){
        try{
            return Objects.requireNonNull(getField(inClass, name, true)).get(instance);
        }catch (IllegalAccessException e){
            e.printStackTrace();
            return false;
        }
    }

    public static Object getFieldAsObject(Class<?> inClass, Object instance, String name,
                                          boolean secureAccess){
        try{
            return Objects.requireNonNull(getField(inClass, name, secureAccess)).get(instance);
        }catch (IllegalAccessException e){
            e.printStackTrace();
            return false;
        }
    }

    public static void setField(Class<?> inClass, Object instance, String name, Object to){
        try{
            Objects.requireNonNull(getField(inClass, name, true)).set(instance, to);
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }

    public static Field getField(Class<?> inClass, String name, boolean secureAccess){
        if(cachedFields.containsKey(getUniquePath(inClass, name)))
            return cachedFields.get(getUniquePath(inClass, name));

        for (Field field : inClass.getDeclaredFields())
            if(field.getName().equals(name)) {
                if(secureAccess && !field.isAccessible())
                    field.setAccessible(true);
                cachedFields.put(getUniquePath(inClass, name), field);
                return field;
            }
        return null;
    }

    private static String getUniquePath(Class klass, String name){
        return klass.getName().replace(".", "/") + "-" + name;
    }

    //If you edited some classes, then,
    //you might have to refresh the caches.
    public static void cleanCachedMethodsAndFields(){
        cachedMethods.clear();
        cachedFields.clear();
    }

    static{
        cachedFields = new HashMap<>();
        cachedMethods = new HashMap<>();
    }

    private static final Minecraft mc = Minecraft.getMinecraft();



    public static void setRightClickDelayTimer(int i) {
      try{
        Field rightClickDelayTimer =
                mc.getClass().getDeclaredField(ClassTransformer.runtimeDeobfuscationEnabled
                        ? "field_71467_ac" : "rightClickDelayTimer");//field_71467_ac
        rightClickDelayTimer.setAccessible(true);
        rightClickDelayTimer.setInt(mc, 0);
    }catch(ReflectiveOperationException e)
    {
        throw new RuntimeException(e);
    }
    }


    public static AxisAlignedBB newAxisAlignedBBInstance(double x1, double y1, double z1, double x2, double y2, double z2){
        Class<?> clazz = null;
        try {
            clazz = Class.forName("net.minecraft.util.AxisAlignedBB");
        } catch (ClassNotFoundException e) {
        }
        Constructor<?> constructor = null;
        try {
            assert clazz != null;
            constructor = clazz.getDeclaredConstructor(double.class,double.class,double.class,double.class,double.class,double.class);
        } catch (NoSuchMethodException e) {
        }
        try {
            return (AxisAlignedBB)constructor.newInstance(x1,y1,z1,x2,y2,z2);
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return null;
    }

    public static float getRenderPartialTicks(){
        Field fTimer = null;
        try {
            fTimer = mc.getClass().getDeclaredField(
                    ClassTransformer.runtimeDeobfuscationEnabled ? "field_71428_T" : "timer");
            fTimer.setAccessible(true);
        } catch (NoSuchFieldException ev) {
            return 0;
        }
        Field frenderPartialTicks = null;
        try {
            frenderPartialTicks = Timer.class.getDeclaredField(
                    ClassTransformer.runtimeDeobfuscationEnabled ? "field_74281_c" : "renderPartialTicks");
        } catch (NoSuchFieldException v) {
            return 0;
        }

        float pTicks = 0;
        try {
            frenderPartialTicks.setAccessible(true);
            pTicks = (float) frenderPartialTicks.get(fTimer.get(mc));
        } catch (IllegalAccessException ev) {
            return 0;
        }
        return pTicks;
    }

    public static Class<?>[] getInterfaces(Object clazz){
        Class<?> c = clazz.getClass();
        Class<?> interfaces[] = c.getInterfaces();
        return interfaces;
    }
    public static boolean isSRG() {
        try {
            if(getField(Minecraft.class,"theMinecraft",true) != null) {
                return false;
            }
            return true;
        } catch(Exception ex) {
            return true;
        }
    }

    public static boolean getProning(EntityPlayer entityPlayer){
        try {
            Class<?> clazz = Class.forName("com.vicmatskiv.weaponlib.ClientEventHandler");
            Method m = clazz.getDeclaredMethod("isProning");
            return (boolean) m.invoke(clazz,entityPlayer);
        }catch (Exception ev){
        }
        return false;
    }


        public static void setCurBlockDamageMP(int i) {
        Field field = null;
        try {
            field = PlayerControllerMP.class.getDeclaredField(
                    ClassTransformer.runtimeDeobfuscationEnabled ? "field_78770_f" : "curBlockDamageMP");
        } catch (NoSuchFieldException e) {
        }
        field.setAccessible(true);
        try {
            field.setInt(mc.playerController, i);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static boolean getHittingBlock(){
        Field field = null;
        try {
            field = Minecraft.getMinecraft().playerController.getClass().getDeclaredField(
                    ClassTransformer.runtimeDeobfuscationEnabled ? "field_78778_j" : "isHittingBlock");
            field.setAccessible(true);
            return field.getBoolean(Minecraft.getMinecraft().playerController.getClass());
        }catch (Exception e){
        }

        return false;
    }
    public static void setwasSprinting(boolean wasSprinting)
    {
        Field field = null;
        try {
            field = mc.thePlayer.getClass().getDeclaredField(
                    ClassTransformer.runtimeDeobfuscationEnabled ? "field_71171_cn" : "wasSprinting");
        } catch (NoSuchFieldException e) {
        }
        field.setAccessible(true);
        try {
            field.setBoolean(mc.thePlayer, wasSprinting);
        } catch (IllegalAccessException e) {
        }
    }
    public static void setBlockHitDelay(int blockHitDelay)
    {
        Field field = null;
        try {
            field = PlayerControllerMP.class.getDeclaredField(
                    ClassTransformer.runtimeDeobfuscationEnabled ? "field_78781_i" : "blockHitDelay");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        field.setAccessible(true);
        try {
            field.setInt(mc.playerController, blockHitDelay);
        } catch (IllegalAccessException e) {
        }
    }

    public static float getCurBlockDamageMP()  {
        Field field = null;
        try {
            field = PlayerControllerMP.class.getDeclaredField(
                    ClassTransformer.runtimeDeobfuscationEnabled ? "field_78770_f" : "curBlockDamageMP");
        } catch (NoSuchFieldException e) {

        }
        field.setAccessible(true);
        try {
            return field.getFloat(mc.playerController);
        } catch (IllegalAccessException e) {
        }
        return 0;
    }


    public static Class getCPacketInjectDetect(){
        try {
            return Class.forName("luohuayu.anticheat.message.CPacketInjectDetect");
        } catch (ClassNotFoundException e) {

        }
        return null;
    }


    public static Class getDeciEntity(){
        try {
            return Class.forName("deci.ag.d");
        } catch (ClassNotFoundException e) {

        }
        return null;
    }

    //枪械物品的父类
    public static Class getGunItem(){
        try {
            return Class.forName("deci.ao.b");
        } catch (ClassNotFoundException e) {

        }
        return null;
    }

    public static Class getEntityNumber(){
        try {
            return Class.forName("nianshow.nshowmod.entity.EntityNumber");
        } catch (ClassNotFoundException e) {

        }
        return null;
    }

    public static Class getNPCEntity(){
        try {
            return Class.forName("noppes.npcs.entity.EntityNPCInterface");
        } catch (ClassNotFoundException e) {
        }
        return null;
    }
}

