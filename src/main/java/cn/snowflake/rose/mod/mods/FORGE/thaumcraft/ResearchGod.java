package cn.snowflake.rose.mod.mods.FORGE.thaumcraft;

import cn.snowflake.rose.events.impl.EventTick;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import com.darkmagician6.eventapi.EventTarget;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.ReflectionHelper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.play.client.C17PacketCustomPayload;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;


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
        } catch (Exception e) {
            this.working = false;
        }
    }

    @Override
    public String getDescription() {
        return "封包解锁神秘所有显示未解锁的笔记!";
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
            Field f = Class.forName("thaumcraft.client.gui.GuiResearchPopup").getDeclaredField("theResearch");
            f.setAccessible(true);
            ((Collection) f.get(Class.forName("thaumcraft.client.lib.ClientTickEventsFML").getField("researchPopup").get(null))).clear();
            LinkedHashMap<String, Object> researchCategories = (LinkedHashMap<String, Object>) getPrivateValue("thaumcraft.api.research.ResearchCategories", "researchCategories", null);
            for (Object listObj : researchCategories.values()) {
                Map<String, Object> research = (Map<String, Object>) getPrivateValue("thaumcraft.api.research.ResearchCategoryList", "research", listObj);
                for (Object item : research.values()) {
                    String[] parents = (String[]) getPrivateValue("thaumcraft.api.research.ResearchItem", "parents", item);
                    String[] parentsHidden = (String[]) getPrivateValue("thaumcraft.api.research.ResearchItem", "parentsHidden", item);
                    String key = (String) getPrivateValue("thaumcraft.api.research.ResearchItem", "key", item);
                    if (!isResearchComplete(key)) {
                        boolean doIt = true;
                        if (parents != null) {
                            for (String parent : parents) {
                                if (!isResearchComplete(parent)) {
                                    doIt = false;
                                    break;
                                }
                            }
                        }
                        if (!doIt) {
                            continue;
                        }
                        if (parentsHidden != null) {
                            for (String parent : parentsHidden) {
                                if (!isResearchComplete(parent)) {
                                    doIt = false;
                                    break;
                                }
                            }
                        }
                        if (!doIt) {
                            continue;
                        }
                        for (Object aObj : getAspects(item)) {
                            if (aObj == null) {
                                doIt = false;
                                break;
                            }
                        }
                        if (!doIt) {
                            continue;
                        }
                        doResearch(key);
                    }
                }
            }
        } catch (Exception ee) {
        }
    }
    
    
    private Object getPrivateValue(final String className, final String fieldName, final Object from) throws Exception {
        return ReflectionHelper.findField((Class)Class.forName(className), new String[] { fieldName }).get(from);
    }

    private boolean isResearchComplete(String researchId) throws Exception {
        return (Boolean) Class.forName("thaumcraft.common.lib.research.ResearchManager").getMethod("isResearchComplete", String.class, String.class).invoke(null, mc.thePlayer.getCommandSenderName(), researchId);
    }

    private Object[] getAspects(Object item) throws Exception {
        Object aspectListObj = getPrivateValue("thaumcraft.api.research.ResearchItem", "tags", item);
        return (Object[]) Class.forName("thaumcraft.api.aspects.AspectList").getMethod("getAspects").invoke(aspectListObj);
    }

    private void doResearch(String researchId) {
        ByteBuf buf = Unpooled.buffer(0);
        buf.writeByte(14);
        ByteBufUtils.writeUTF8String(buf, researchId);
        buf.writeInt(mc.thePlayer.dimension);
        ByteBufUtils.writeUTF8String(buf, mc.thePlayer.getCommandSenderName());
        buf.writeByte(0);
        C17PacketCustomPayload packet = new C17PacketCustomPayload("thaumcraft", buf);
        mc.thePlayer.sendQueue.addToSendQueue(packet);
    }
    
}