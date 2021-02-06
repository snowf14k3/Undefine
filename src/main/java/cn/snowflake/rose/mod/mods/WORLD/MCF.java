package cn.snowflake.rose.mod.mods.WORLD;

import cn.snowflake.rose.events.impl.EventUpdate;
import cn.snowflake.rose.friend.Friend;
import cn.snowflake.rose.management.FriendManager;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.MouseInputHandler;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.player.EntityPlayer;

public class MCF extends Module {
    public MCF() {
        super("MCF","MCF", Category.WORLD);
    }

    private MouseInputHandler handler = new MouseInputHandler(2);

    @EventTarget
    public void onupdate(EventUpdate e){
        if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.entityHit != null && this.mc.objectMouseOver.entityHit instanceof EntityPlayer) {
            String name = this.mc.objectMouseOver.entityHit.getCommandSenderName();
            if (this.handler.canExcecute()) {
                if (FriendManager.isFriend(name)) {
                    for(int i = 0; i < FriendManager.getFriends().size(); ++i) {
                        Friend f = (Friend)FriendManager.getFriends().get(i);
                        if (f.getName().equalsIgnoreCase(name)) {
                            FriendManager.getFriends().remove(i);
                        }
                    }
                } else {
                    FriendManager.getFriends().add(new Friend(name, name));
                }
            }
        }

    }



}
