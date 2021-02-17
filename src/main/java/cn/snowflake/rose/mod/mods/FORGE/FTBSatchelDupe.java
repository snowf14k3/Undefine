package cn.snowflake.rose.mod.mods.FORGE;

import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class FTBSatchelDupe extends Module {
    public FTBSatchelDupe() {
        super("FTBSatchelDupe", "FTB Satchel Dupe", Category.FORGE);
    }


    @Override
    public void onEnable() {
        try {
            if (Class.forName("cofh.thermalexpansion.gui.container.ContainerSatchel").isInstance(mc.thePlayer.openContainer)) {
                Class.forName("ftb.lib.api.net.MessageLM")
                        .getMethod("sendToServer")
                        .invoke(Class.forName("ftb.lib.mod.net.MessageClientItemAction")
                                .getConstructor(String.class, NBTTagCompound.class)
                                .newInstance("", new NBTTagCompound()));
                int dropCount = (int) Class.forName("cofh.thermalexpansion.item.ItemSatchel")
                        .getMethod("getStorageIndex", ItemStack.class)
                        .invoke(null, mc.thePlayer.getCurrentEquippedItem());

                for(int slot = mc.thePlayer.inventory.mainInventory.length;
                    slot < mc.thePlayer.inventory.mainInventory.length + dropCount * 9;
                    slot++
                ) {
                    dropSlot(slot, true);
                }
                mc.thePlayer.closeScreen();
            }
        } catch (Exception e) {}
        set(false);
        super.onEnable();
    }

    public void dropSlot(int slot, boolean allStacks) {
        clickSlot(slot, allStacks ? 1 : 0, 4);
    }

    public void swapSlot(int from, int to) {
        clickSlot(from, to, 2);
    }

    public void clickSlot(int slot, int shift, int action) {
        mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, slot, shift, action, mc.thePlayer);
    }
}
