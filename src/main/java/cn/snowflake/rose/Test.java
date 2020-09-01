package cn.snowflake.rose;


import cn.snowflake.rose.events.impl.EventMove;
import com.darkmagician6.eventapi.EventManager;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class Test extends Entity {


    public Test(World p_i1582_1_) {
        super(p_i1582_1_);
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbtTagCompound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbtTagCompound) {

    }


    @Override
    public void moveEntity(double x, double y, double z) {
        EventMove eventMove = new EventMove(x,y,z);
        EventManager.call(eventMove);
        x = eventMove.x;
        y = eventMove.y;
        z = eventMove.z;
        super.moveEntity(x, y, z);
    }
}
