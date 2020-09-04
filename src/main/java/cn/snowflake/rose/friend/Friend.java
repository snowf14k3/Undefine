package cn.snowflake.rose.friend;

import net.minecraft.entity.EntityLivingBase;

public class Friend {
    public String name ;
    public EntityLivingBase friend;

    public Friend(String name, EntityLivingBase friend){
        this.name = name;
        this.friend = friend;
    }

    public EntityLivingBase getFriend() {
        return friend;
    }

    public String getName() {
        return name;
    }
}
