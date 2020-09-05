package cn.snowflake.rose.mod.mods.RENDER;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.manager.FriendManager;
import cn.snowflake.rose.manager.ModManager;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.ChatUtil;
import cn.snowflake.rose.utils.JReflectUtility;
import cn.snowflake.rose.utils.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Objects;

public class Chams extends Module {
    public Chams() {
        super("Chams", Category.RENDER);
    }
    public static boolean c ;


    @Override
    public void onDisable() {
        c = false;
        super.onDisable();
    }

    @Override
    public void onEnable() {
        c = true;
        super.onEnable();
    }

    public static Value<Boolean> customnpcs = new Value("Chams_CustomNPCs", false);
    public static Value<Boolean> customnpcsteam = new Value("Chams_CustomNPCTeam", false);


    public static Value<Boolean> players = new Value("Chams_Player", true);
    public static Value<Boolean> otherentity = new Value("Chams_Otherentity", true);
    public static Value<Boolean> animal = new Value("Chams_Animal", false);
    public static Value<Boolean> moster = new Value("Chams_Mob", false);
    public static Value<Boolean> village = new Value("Chams_village", false);

    public static  boolean canTarget(Entity entity) {
        if (Client.nshowmod){
            if (Objects.requireNonNull(JReflectUtility.getEntityNumber()).isInstance(entity)){
                return false;
            }
        }
        if (Client.customnpcs) {
            if (Objects.requireNonNull(JReflectUtility.getNPCEntity()).isInstance(entity) ){
                if (!customnpcs.getValueState()){
                    return false;
                }
                if (!customnpcsteam.getValueState() && Minecraft.getMinecraft().thePlayer.isOnSameTeam((EntityLivingBase) entity)){
                    return false;
                }
            }
        }else{
            if (customnpcs.getValueState() || customnpcsteam.getValueState()){
                customnpcs.setValueState(false);
                customnpcsteam.setValueState(false);
                ChatUtil.sendClientMessage("You have no install the customeNPCs");
            }
        }
        if (!ModManager.getModByName("NoFriend").isEnabled() && FriendManager.isFriend(entity.getCommandSenderName())){
            return false;
        }
        if (entity instanceof EntityPlayer && !players.getValueState()) {
            return false;
        }
        if ((entity instanceof EntityAnimal || entity instanceof EntitySquid) && !animal.getValueState()) {
            return false;
        }
        if ((entity instanceof EntityMob || entity instanceof EntityBat) && !moster.getValueState()) {
            return false;
        }
        if (entity instanceof EntityVillager && !village.getValueState()) {
            return false;
        }
//        if (!(entity instanceof EntityMob || entity instanceof EntityAnimal)
//                && entity instanceof EntityCreature
//                && !otherentity.getValueState()) {
//            return false;
//        }
        return true;
    }
}
