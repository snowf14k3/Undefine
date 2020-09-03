package cn.snowflake.rose.events.impl;

import com.darkmagician6.eventapi.events.Event;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;
import io.netty.channel.ChannelFutureListener;

import java.util.EnumMap;

/**
 *
 * Author : CNSnowFlake
 *
 */
public class EventFMLChannels implements Event {
    public IMessage iMessage;
    public EnumMap<Side, FMLEmbeddedChannel> channels;
    private boolean cancelled;

//    mv.visitFieldInsn(GETFIELD, "cn/snowflake/rose/Test", "channels", "Ljava/util/EnumMap;");

    public EventFMLChannels(Object iMessage,EnumMap channels){
        this.iMessage = (IMessage) iMessage;
        this.channels = channels;
    }
    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    /**
     *
     * @return shit
     */
    public EnumMap<Side, FMLEmbeddedChannel> getChannels() {
        return channels;
    }

    public void sendToServer(IMessage message)
    {
        channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        channels.get(Side.CLIENT).writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }



}
