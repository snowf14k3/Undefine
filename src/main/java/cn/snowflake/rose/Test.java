package cn.snowflake.rose;


import cn.snowflake.rose.asm.MinecraftHook;
import cn.snowflake.rose.events.impl.EventMove;
import cn.snowflake.rose.mod.mods.PLAYER.NoSlow;
import com.darkmagician6.eventapi.EventManager;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

public class Test  {
    public List foundClassList;

    public Test(List foundClassList) {
        if (MinecraftHook.anticat){
            return;
        }
        this.foundClassList = foundClassList;
    }



}
