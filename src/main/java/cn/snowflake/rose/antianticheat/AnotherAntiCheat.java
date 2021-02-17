package cn.snowflake.rose.antianticheat;

import cn.snowflake.rose.events.impl.EventFMLChannels;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class AnotherAntiCheat {

    public AnotherAntiCheat(){
        EventManager.register(this);
    }

    @EventTarget
    public void onFml(EventFMLChannels eventFMLChannels){
        try {
            if (eventFMLChannels.iMessage.toString().contains("AntiCheatSTCPacketMessage")){
//                eventFMLChannels.setCancelled(true);//À¹½Ø
//                Constructor constructor = eventFMLChannels.iMessage.getClass().getConstructor(byte[][].class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @EventTarget
//    public void frombyte(EventFromByte eventFromByte){
//        if (eventFromByte.iMessage.toString().contains("AntiCheatSTCPacketMessage") && eventFromByte.getEventType() == EventType.SEND){
//            System.out.println(Arrays.deepToString(md5s));
//        }
//    }

    public byte[][] md5s;


    public void getMD5fromNBT(ByteBuf buf,boolean is1122){
        NBTTagCompound nbt = ByteBufUtils.readTag(buf);
        NBTTagList md5List = nbt.getTagList("md5s", 7);
        this.md5s = new byte[md5List.tagCount()][];
        for (int i = this.md5s.length - 1; i >= 0; --i) {
            this.md5s[i] = ((NBTTagByteArray)md5List.removeTag(i)).func_150292_c();//func_150292_c GetByteArray
        }
    }

}
