package cn.snowflake.rose.mod.mods.FORGE;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.darkmagician6.eventapi.EventTarget;

import cn.snowflake.rose.events.impl.EventTick;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.client.ChatUtil;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.ReflectionHelper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.play.client.C17PacketCustomPayload;


public class ResearchGod extends Module {
	
    public int tickId;
    
    public ResearchGod() {
        super("ResearchGod", "Research God", Category.FORGE);
        this.tickId = -1;
        try {
            Class.forName("thaumcraft.api.research.ResearchCategories");
            Class.forName("thaumcraft.api.research.ResearchCategoryList");
            Class.forName("thaumcraft.api.research.ResearchItem");
            Class.forName("thaumcraft.common.lib.research.ResearchManager");
            Class.forName("thaumcraft.api.aspects.AspectList");
        } catch (Exception var2) {
            this.Working = false;
        }
    }
    
    
    @Override
     public void onEnable() {


     }

    @EventTarget 
    public void onTicks(EventTick e) {
        ++this.tickId;
        if (this.tickId % 40 != 0) {
            return;
        }
        this.tickId = 0;
        try {
            final Field f = Class.forName("thaumcraft.client.gui.GuiResearchPopup").getDeclaredField("theResearch");
            f.setAccessible(true);
            ((Collection)f.get(Class.forName("thaumcraft.client.lib.ClientTickEventsFML").getField("researchPopup").get(null))).clear();
            final LinkedHashMap<String, Object> researchCategories = (LinkedHashMap<String, Object>)this.getPrivateValue("thaumcraft.api.research.ResearchCategories", "researchCategories", null);
            for (final Object listObj : researchCategories.values()) {
                final Map<String, Object> research = (Map<String, Object>)this.getPrivateValue("thaumcraft.api.research.ResearchCategoryList", "research", listObj);
                for (final Object item : research.values()) {
                    final String[] parents = (String[])this.getPrivateValue("thaumcraft.api.research.ResearchItem", "parents", item);
                    final String[] parentsHidden = (String[])this.getPrivateValue("thaumcraft.api.research.ResearchItem", "parentsHidden", item);
                    final String key = (String)this.getPrivateValue("thaumcraft.api.research.ResearchItem", "key", item);
                    if (!this.isResearchComplete(key)) {
                        boolean doIt = true;
                        if (parents != null) {
                            for (final String parent : parents) {
                                if (!this.isResearchComplete(parent)) {
                                    doIt = false;
                                    break;
                                }
                            }
                        }
                        if (!doIt) {
                            continue;
                        }
                        if (parentsHidden != null) {
                            for (final String parent : parentsHidden) {
                                if (!this.isResearchComplete(parent)) {
                                    doIt = false;
                                    break;
                                }
                            }
                        }
                        if (!doIt) {
                            continue;
                        }
                        for (final Object aObj : this.getAspects(item)) {
                            if (aObj == null) {
                                doIt = false;
                                break;
                            }
                        }
                        if (!doIt) {
                            continue;
                        }
                        this.doResearch(key);
                    }
                }
            }
        }
        catch (Exception ex) {}
    }
    
    
    private Object getPrivateValue(final String className, final String fieldName, final Object from) throws Exception {
        return ReflectionHelper.findField((Class)Class.forName(className), new String[] { fieldName }).get(from);
    }
    
    private boolean isResearchComplete(final String researchId) throws Exception {
        return (boolean)Class.forName("thaumcraft.common.lib.research.ResearchManager").getMethod("isResearchComplete", String.class, String.class).invoke(null, mc.thePlayer.getCommandSenderName(), researchId);
    }
    
    private Object[] getAspects(final Object item) throws Exception {
        final Object aspectListObj = this.getPrivateValue("thaumcraft.api.research.ResearchItem", "tags", item);
        return (Object[])Class.forName("thaumcraft.api.aspects.AspectList").getMethod("getAspects", (Class<?>[])new Class[0]).invoke(aspectListObj, new Object[0]);
    }
    
    private void doResearch(final String researchId) {
        final ByteBuf buf = Unpooled.buffer(0);
        buf.writeByte(14);
        ByteBufUtils.writeUTF8String(buf, researchId);
        buf.writeInt(mc.thePlayer.dimension);
        ByteBufUtils.writeUTF8String(buf, mc.thePlayer.getCommandSenderName());
        buf.writeByte(0);
        final C17PacketCustomPayload packet = new C17PacketCustomPayload("thaumcraft", buf);
        mc.thePlayer.sendQueue.addToSendQueue(packet);
    }
    
}