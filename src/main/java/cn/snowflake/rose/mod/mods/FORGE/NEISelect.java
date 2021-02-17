package cn.snowflake.rose.mod.mods.FORGE;

import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.client.ChatUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NEISelect extends Module {
    public NEISelect() {
        super("NEISelect", "NEI Select", Category.FORGE);
    }


    public static NBTTagCompound STATIC_NBT = new NBTTagCompound();
    public static ItemStack STATIC_ITEMSTACK = null;
    public static int STATIC_INT = 0;
    
    @Override
    public void onEnable() {
            try {
                GuiContainer container = mc.currentScreen instanceof GuiContainer ? ((GuiContainer) mc.currentScreen) : null;
                if (container == null) {
                    return;
                }
                Object checkItem = Class.forName("codechicken.nei.guihook.GuiContainerManager")
                        .getDeclaredMethod("getStackMouseOver", GuiContainer.class)
                        .invoke(null, container);

                if (checkItem instanceof ItemStack) {
                    ItemStack item = (ItemStack) checkItem;
                    int count = GuiContainer.isShiftKeyDown() ? item.getMaxStackSize() : 1;
                    STATIC_ITEMSTACK = item.copy().splitStack(count);
                    STATIC_NBT = STATIC_ITEMSTACK.getTagCompound() == null ? new NBTTagCompound() : STATIC_ITEMSTACK.getTagCompound();
                }
                ChatUtil.sendClientMessage("ItemStack selected");
            } catch (Exception ignored) {

            }
        super.onEnable();
    }
}
