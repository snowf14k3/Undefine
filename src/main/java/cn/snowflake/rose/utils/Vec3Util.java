package cn.snowflake.rose.utils;


import net.minecraft.util.Vec3;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Vec3Util
{
    private double x;
    private double y;
    private double z;

    public Vec3Util(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public Vec3Util addVector(final double n, final double n2, final double n3) {
        return new Vec3Util(this.x + n, this.y + n2, this.z + n3);
    }

    public Vec3Util floor() {
        return new Vec3Util(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z));
    }

    public double squareDistanceTo(final Vec3Util class235) {
        return Math.pow(class235.x - this.x, 2.0) + Math.pow(class235.y - this.y, 2.0) + Math.pow(class235.z - this.z, 2.0);
    }

    public Vec3Util add(final Vec3Util class235) {
        return this.addVector(class235.getX(), class235.getY(), class235.getZ());
    }

    public Vec3 mc() throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<Vec3> clazz = (Class<Vec3>) Class.forName("net.minecraft.util.Vec3");
        Constructor vec3 = clazz.getDeclaredConstructor(double.class,double.class,double.class);
        vec3.setAccessible(true);
        return (Vec3)vec3.newInstance(x,y,z);
    }


    @Override
    public String toString() {
        return "[" + this.x + ";" + this.y + ";" + this.z + "]";
    }
}
