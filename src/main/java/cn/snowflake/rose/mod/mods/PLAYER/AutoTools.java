package cn.snowflake.rose.mod.mods.PLAYER;


import cn.snowflake.rose.events.impl.EventPacket;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import com.darkmagician6.eventapi.EventTarget;


import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C07PacketPlayerDigging;

public class AutoTools extends Module {

    public AutoTools() {
        super("AutoTool","Auto Tool", Category.PLAYER);
    }


    @EventTarget
    public void onClickBlock(EventPacket e) {
        if (e.getPacket() instanceof C07PacketPlayerDigging){
            if (!(mc.thePlayer.isEating() || this.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemBow)) {
                this.bestTool(mc.objectMouseOver.blockX, mc.objectMouseOver.blockY, mc.objectMouseOver.blockZ);
            }
        }
    }
    
    public void bestTool(int x, int y, int z) {
        int getIdFromBlock = Block.getIdFromBlock(mc.theWorld.getBlock(x, y, z));
        int currentItem = 0;
        float getStrVsBlock = -1.0f;
        for (int i = 36; i < 45; ++i) {
            try {
                ItemStack getStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if ((getStack.getItem() instanceof ItemTool || getStack.getItem() instanceof ItemSword || getStack.getItem() instanceof ItemShears) && getStack.func_150997_a(Block.getBlockById(getIdFromBlock)) > getStrVsBlock) {
                    currentItem = i - 36;
                    getStrVsBlock = getStack.func_150997_a(Block.getBlockById(getIdFromBlock));
                }
            }
            catch (Exception ex) {}
        }
        if (getStrVsBlock != -1.0f) {
            mc.thePlayer.inventory.currentItem = currentItem;
            mc.playerController.updateController();
        }
    }
}
