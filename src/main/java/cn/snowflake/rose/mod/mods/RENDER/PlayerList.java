package cn.snowflake.rose.mod.mods.RENDER;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.events.impl.EventRender2D;
import cn.snowflake.rose.management.ModManager;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerList extends Module {
    public PlayerList() {
        super("PlayerList", "Player List", Category.RENDER);
    }

    @EventTarget
    public void on2D(EventRender2D eventRender2D){
        if (Objects.requireNonNull(ModManager.getModByName("HUD")).isEnabled()){
            float width = 60;
            int height = 15;
            int posX = 2;
            int posY = 18;
//            RenderUtil.drawRect(posX, posY, posX + width + 2, posY + height, new Color(5, 5, 5, 255).getRGB());
//            RenderUtil.drawBorderedRect(posX + .5, posY + .5, posX + width + 1.5, posY + height - .5, 0.5, new Color(40, 40, 40, 255).getRGB(), new Color(60, 60, 60, 255).getRGB(), true);
//            RenderUtil.drawBorderedRect(posX + 2, posY + 2, posX + width, posY + height - 2, 0.5, new Color(22, 22, 22, 255).getRGB(), new Color(60, 60, 60, 255).getRGB(), true);
//            RenderUtil.drawRect(posX + 2.5, posY + 2.5, posX + width - .5, posY + 4.5, new Color(9, 9, 9, 255).getRGB());
            if (getTargets().isEmpty()){
                Client.instance.fontManager.simpleton11.drawStringWithColor("No enemies nearby ", 5 + posX, 5 + posY, -1,0);

            }
            for (EntityLivingBase e : Objects.requireNonNull(getTargets())){

                Client.instance.fontManager.simpleton11.drawStringWithColor(e.getCommandSenderName() + " \2472d:\247f " + (int)e.getDistanceToEntity(mc.thePlayer), 5 + posX, 5 + posY, -1,0);
                posY+=Client.instance.fontManager.simpleton11.FONT_HEIGHT;
            }


        }
    }

    private List<EntityLivingBase> getTargets() {
        try {
            ArrayList<String> nigger = new ArrayList<>();
            ArrayList<EntityLivingBase> targets = new ArrayList();
            for(Object entity : mc.theWorld.loadedEntityList) {
                if(entity instanceof EntityPlayer && entity != mc.thePlayer &&  ((Entity)entity).isEntityAlive()) {
                    targets.add((EntityLivingBase) entity);
                }
            }
            targets.sort((o1, o2) -> (int) (o1.getDistanceToEntity(mc.thePlayer) - o2.getDistanceToEntity(mc.thePlayer)));
//            for (EntityLivingBase entityLivingBase :targets){
//                nigger.add(entityLivingBase.getCommandSenderName());
//            }
            return targets;
        } catch (Exception e) {
            return null;
        }
    }

}
