package cn.snowflake.rose.mod.mods.WORLD;

import cn.snowflake.rose.events.impl.EventUpdate;
import cn.snowflake.rose.friend.Friend;
import cn.snowflake.rose.manager.FriendManager;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.MouseInputHandler;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.player.EntityPlayer;

public class MCF extends Module {
    public MCF() {
        super("MCF","MCF", Category.WORLD);
    }

    MouseInputHandler midhandler = new MouseInputHandler(2);

    @EventTarget
    public void onupdate(EventUpdate e){
        if (mc.objectMouseOver.entityHit != null && midhandler.canExcecute()){
            if (mc.objectMouseOver.entityHit instanceof EntityPlayer){
                EntityPlayer entity = (EntityPlayer) mc.objectMouseOver.entityHit;

                for (Friend f : FriendManager.friends){

                    if (f.getName().equalsIgnoreCase(entity.getCommandSenderName())){
                        FriendManager.friends.remove(f);
                    }else{
                        FriendManager.friends.add(new Friend(entity.getCommandSenderName(),entity));
                    }

                }
            }
        }

    }



}
