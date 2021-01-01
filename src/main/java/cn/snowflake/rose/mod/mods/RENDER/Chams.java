package cn.snowflake.rose.mod.mods.RENDER;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.events.impl.EventRenderPlayer;
import cn.snowflake.rose.events.impl.EventUpdate;
import cn.snowflake.rose.manager.FriendManager;
import cn.snowflake.rose.manager.ModManager;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.ChatUtil;
import cn.snowflake.rose.utils.JReflectUtility;
import cn.snowflake.rose.utils.Value;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityEnderChest;
import org.lwjgl.opengl.GL11;

import java.util.Objects;

public class Chams extends Module {
    public Chams() {
        super("Chams","Chams", Category.RENDER);
    }

    public static boolean chams;

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @EventTarget
    public void onupdate(EventUpdate e){
        chams = this.isEnabled();
    }

//    @EventTarget
//    public void onrenderplayer(EventRenderPlayer e){
//        if (e.getType() == EventType.PRE){
//            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
//            GL11.glPolygonOffset(1.0F, -2000000F);
//        }else if (e.getType() == EventType.POST){
//            GL11.glPolygonOffset(1.0F, 2000000F);
//            GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
//        }
//    }
}