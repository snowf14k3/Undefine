package cn.snowflake.rose.mod.mods.MOVEMENT;

import cn.snowflake.rose.events.impl.EventUpdate;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.BlockPos;
import cn.snowflake.rose.utils.MouseInputHandler;
import cn.snowflake.rose.utils.PlayerUtil;
import cn.snowflake.rose.utils.Value;
import com.darkmagician6.eventapi.EventTarget;


import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Teleport
        extends Module
{
    public static Value<Double> reach = new Value<Double>("Teleport_Reach",20d,10d,100d);
    private Value<String> mode = new Value("Teleport_Mode", "Mode", 0);
    private Value<String> useMode = new Value("Teleport_UseMode", "UseMode", 0);
    private MouseInputHandler handler = new MouseInputHandler(0);
    private int tick = 0;

    public Teleport(){
        super("Teleport", Category.MOVEMENT);
        this.mode.mode.add("Vanilla");
        this.useMode.addValue("Right");
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (Mouse.isButtonDown(1) && this.useMode.isCurrentMode("Right")) {
            double[] startPos = {mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ};
            BlockPos endPos = new BlockPos(mc.objectMouseOver.blockX,mc.objectMouseOver.blockY,mc.objectMouseOver.blockZ);
            if (mc.theWorld.getBlock(endPos.getX(),endPos.getY(),endPos.getZ()) != Blocks.air){
                teleport(startPos, endPos);
            }
        }
    }

    public void teleport(final double[] startPos, final BlockPos endPos){
        double distx = startPos[0] - endPos.getX()+ 0.5;
        double disty = startPos[1] - endPos.getY();
        double distz = startPos[2] - endPos.getZ()+ 0.5;
        double dist = Math.sqrt(mc.thePlayer.getDistanceSq(endPos.getX(),endPos.getY(),endPos.getZ()));
        double distanceEntreLesPackets = 5;
        double xtp, ytp, ztp = 0;

        if(dist> distanceEntreLesPackets){
            double nbPackets = Math.round(dist / distanceEntreLesPackets + 0.49999999999) - 1;
            xtp = mc.thePlayer.posX;
            ytp = mc.thePlayer.posY;
            ztp = mc.thePlayer.posZ;
            double count = 0;
            for (int i = 1; i < nbPackets;i++){
                double xdi = (endPos.getX() - mc.thePlayer.posX)/( nbPackets);
                xtp += xdi;

                double zdi = (endPos.getZ() - mc.thePlayer.posZ)/( nbPackets);
                ztp += zdi;

                double ydi = (endPos.getY() - mc.thePlayer.posY)/( nbPackets);
                ytp += ydi;
                count ++;
                C03PacketPlayer.C04PacketPlayerPosition Packet= new C03PacketPlayer.C04PacketPlayerPosition(xtp,mc.thePlayer.boundingBox.minY, ytp, ztp, true);

                mc.thePlayer.sendQueue.addToSendQueue(Packet);
            }

            mc.thePlayer.setPosition(endPos.getX() + 0.5, endPos.getY() + 2, endPos.getZ() + 0.5);
        }else{
            mc.thePlayer.setPosition(endPos.getX(), endPos.getY(), endPos.getZ());
        }
    }


    private void teleport(double x, double y, double z) {
        this.doVanilla(x, y, z);
    }

    private void doVanilla(double posX, double posY, double posZ) {
        ArrayList<Vector3f> vecs = PlayerUtil.vanillaTeleportPositions(posX, posY, posZ, 8.0);
        for (Vector3f vec : vecs) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec.x,mc.thePlayer.boundingBox.minY,vec.y, vec.z, true));
        }
    }

}
