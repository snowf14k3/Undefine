package cn.snowflake.rose.mod.mods.PLAYER;


import java.util.Objects;

import cn.snowflake.rose.events.impl.EventMotion;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.BlockPos;
import cn.snowflake.rose.utils.JReflectUtility;
import com.darkmagician6.eventapi.EventTarget;


import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

public class AutoTools extends Module {

    public AutoTools() {
        super("AutoTools", Category.PLAYER);
    }

    public Entity getItems(double n) {
        Entity entity = null;
        double n2 = n;
        for (Object entity2 : mc.theWorld.loadedEntityList) {
            if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically && entity2 instanceof EntityItem) {
                double n3 = mc.thePlayer.getDistanceToEntity((Entity)entity2);
                if (n3 > n2) {
                    continue;
                }
                n2 = n3;
                entity = (Entity)entity2;
            }
        }
        return entity;
    }

    @EventTarget
    public void onClickBlock(EventMotion eventPostMotion) {
        if (eventPostMotion.getEventType() == EventType.POST){
            if (!mc.thePlayer.isEating() && JReflectUtility.getHittingBlock() && !Objects.isNull(new BlockPos(mc.objectMouseOver.blockX,mc.objectMouseOver.blockY,mc.objectMouseOver.blockZ))) {
                this.bestTool(mc.objectMouseOver.blockX, mc.objectMouseOver.blockY, mc.objectMouseOver.blockZ);
            }
        }
    }



    public void bestTool(int n, int n2, int n3) {
        int getIdFromBlock = Block.getIdFromBlock(mc.theWorld.getBlock(n, n2, n3));
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
