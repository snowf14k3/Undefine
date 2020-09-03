package cn.snowflake.rose.mod.mods.PLAYER;


import cn.snowflake.rose.events.impl.EventUpdate;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import org.lwjgl.input.Keyboard;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import java.util.Objects;

public class InvMove extends Module {

    public InvMove() {
        super("InvMove", Category.PLAYER);
    }

    @EventTarget
    public void call(EventUpdate event) {
        if ((this.mc.currentScreen != null) && (!(this.mc.currentScreen instanceof GuiChat)))
        {

            KeyBinding[] key = { this.mc.gameSettings.keyBindForward, this.mc.gameSettings.keyBindBack,
                    this.mc.gameSettings.keyBindLeft, this.mc.gameSettings.keyBindRight, this.mc.gameSettings.keyBindSprint, this.mc.gameSettings.keyBindJump };
            KeyBinding[] arrayOfKeyBinding1;
            int j = (arrayOfKeyBinding1 = key).length;
            for (int i = 0; i < j; i++)
            {
                KeyBinding b = arrayOfKeyBinding1[i];
                KeyBinding.setKeyBindState(b.getKeyCode(), Keyboard.isKeyDown(b.getKeyCode()));
            }


            if (Keyboard.isKeyDown(200)) {
                mc.thePlayer.rotationPitch -= 1;
            }
            if (Keyboard.isKeyDown(208)) {
                mc.thePlayer.rotationPitch += 1;
            }
            if (Keyboard.isKeyDown(203)) {
                mc.thePlayer.rotationYaw -= 3;
            }
            if (Keyboard.isKeyDown(205)) {
                mc.thePlayer.rotationYaw += 3;
            }

        }
    }
    

}
