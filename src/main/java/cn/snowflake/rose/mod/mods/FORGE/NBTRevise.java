package cn.snowflake.rose.mod.mods.FORGE;

import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;

public class NBTRevise extends Module {

    public NBTRevise() {
        super("NBTRevise", "NBT Revise", Category.FORGE);
        try {
            Class.forName("com.mcf.davidee.nbtedit.NBTEdit");
        } catch (Exception var2) {
            this.working = false;
        }
    }

    @Override
    public void onEnable() {
        try {
            final Class clazz = Class.forName("com.mcf.davidee.nbtedit.NBTEdit");
            final Object proxy = clazz.getField("proxy").get(null);
            final Class clazz2 = Class.forName("com.mcf.davidee.nbtedit.forge.ClientProxy");
            final NBTTagCompound compound = new NBTTagCompound();
            final MovingObjectPosition mop = mc.objectMouseOver;
            final TileEntity entity = mc.theWorld.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
            if (entity != null) {
                entity.writeToNBT(compound);
                clazz2.getMethod("openEditGUI", Integer.TYPE, Integer.TYPE, Integer.TYPE, NBTTagCompound.class).invoke(proxy, entity.xCoord, entity.yCoord, entity.zCoord, compound);
            }
            else if (mop.entityHit != null) {
                mop.entityHit.writeToNBT(compound);
                clazz2.getMethod("openEditGUI", Integer.TYPE, NBTTagCompound.class).invoke(proxy, mop.entityHit.getEntityId(), compound);
            }
            else {
                mc.thePlayer.writeToNBT(compound);
                clazz2.getMethod("openEditGUI", Integer.TYPE, NBTTagCompound.class).invoke(proxy, mc.thePlayer.getEntityId(), compound);
            }
            this.set(false);
        }
        catch (Throwable ex) {
            this.set(false);
        }
        super.isEnabled();
    }
}
