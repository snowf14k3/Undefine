package cn.snowflake.rose;


import cn.snowflake.rose.asm.MinecraftHook;
import cn.snowflake.rose.events.impl.EventFMLChannels;
import cn.snowflake.rose.events.impl.EventMove;
import cn.snowflake.rose.mod.mods.PLAYER.NoSlow;
import com.darkmagician6.eventapi.EventManager;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import io.netty.channel.ChannelFutureListener;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.EnumMap;
import java.util.List;

public class Test  {
    public EnumMap<Side, FMLEmbeddedChannel> channels;

    public Test(List<String> foundClassList){

    }


    public void sendToServer(IMessage message)
    {
        EventFMLChannels eventFMLChannels = new EventFMLChannels(message,channels);
        EventManager.call(eventFMLChannels);
        if (eventFMLChannels.isCancelled()){
            return;
        }
        channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        channels.get(Side.CLIENT).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }


}
