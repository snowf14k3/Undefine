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
        KeyBinding[] moveKeys = new KeyBinding[]{mc.gameSettings.keyBindRight, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindForward, mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSprint};
        KeyBinding[] array2;
        int length2;
        int j;
        KeyBinding bind;
        if (this.mc.currentScreen != null && !(this.mc.currentScreen instanceof GuiChat)) {
            array2 = moveKeys;
            length2 = moveKeys.length;
            for(j = 0; j < length2; ++j) {
                bind = array2[j];
                KeyBinding.setKeyBindState(bind.getKeyCode(),Keyboard.isKeyDown(bind.getKeyCode()));
            }
        } else if (Objects.isNull(mc.currentScreen)) {
            array2 = moveKeys;
            length2 = moveKeys.length;

            for(j = 0; j < length2; ++j) {
                bind = array2[j];
                if (!Keyboard.isKeyDown(bind.getKeyCode())) {
                    KeyBinding.setKeyBindState(bind.getKeyCode(), false);
                }
            }
        }
    }

}
