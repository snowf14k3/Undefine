package cn.snowflake.rose.ui;


import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import cn.snowflake.rose.Client;
import cn.snowflake.rose.manager.ModManager;
import cn.snowflake.rose.mod.Category;
import cn.snowflake.rose.mod.Module;
import cn.snowflake.rose.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
/**
 *
 * @author SnowFlake , SuChen
 *
 * Time : 2020.01.05 12:01
 *
 */
public class CSGOGUI extends GuiScreen{
    private MouseInputHandler handlerMid = new MouseInputHandler(2);
    private MouseInputHandler handlerRight = new MouseInputHandler(1);
    private MouseInputHandler handler = new MouseInputHandler(0);
    UnicodeFontRenderer font2 = Client.instance.fontManager.simpleton13;

    public int moveX = 0;
    public int moveY = 0;

    public int startX = 50;
    public int startY = 40;
    public int lastselect;
    public int selectCategory = 0;
    private float scrollY;
    private float modscrollY;

    public static boolean binding = false;
    public Module bmod;
    public static Module currentMod = null;

    private int modlistsize = 0;
    private int valuelistsize = 0;

    private boolean caninput = false;
    private float width;

    int selectedChar;
    @Override
    protected void keyTyped(char typedChar, int keyCode)  {
        for(int i = 0; i < getModsInCategory(Category.values()[selectCategory]).size(); ++i  ) {
            Module mod = (Module) getModsInCategory(Category.values()[selectCategory]).get(i);
            for (Value value : Value.list){
                if (mod.openValues){
                    if (value.getValueName().split("_")[0].equalsIgnoreCase(mod.getName()) && value.isValueString){
                        if (caninput){
                            selectedChar =  value.getText().toString().length();
                            StringBuilder stringBuilder;
                            String oldString;
                            String textString = value.getText().toString();

                            switch (keyCode){
                                case Keyboard.KEY_BACK:
                                    if (!value.getText().toString().isEmpty()){
                                        oldString = value.getText().toString();
                                        stringBuilder = new StringBuilder(oldString);
                                        stringBuilder.charAt(selectedChar - 1);
                                        stringBuilder.deleteCharAt(selectedChar - 1);

                                        textString = ChatAllowedCharacters.filerAllowedCharacters(stringBuilder.toString());
                                        --selectedChar;

                                        if (selectedChar > textString.length()) {
                                            selectedChar = textString.length();
                                        }

                                        value.setText(textString);
                                    }
                                    break;
                                case Keyboard.KEY_LEFT:
//                                   if (selectedChar > 0) {
//                                       selectedChar = selectedChar - 1;
//                                   }
                                    selectedChar = this.selectedChar - 1;

                                    break;
                                case Keyboard.KEY_RIGHT:
                                    if (selectedChar < textString.length()) {
                                        ++selectedChar;
                                    }
                                    break;
                            }
                            width = font2.getStringWidth(textString.substring(0, selectedChar));

                            if (keyCode != Keyboard.KEY_NONE && ChatAllowedCharacters.isAllowedCharacter(Keyboard.getEventCharacter())){
                                value.setText(value.getText().toString()+Keyboard.getEventCharacter());
                                Client.instance.fileMgr.saveValues();
                            }
                            if (keyCode == Keyboard.KEY_RETURN){
                                caninput = false;
                            }
                        }
                    }
                }
            }
        }
        //the main method by hero code
        if(keyCode == 1) {
            this.lastselect = selectCategory;
        }
        if(binding) {
            if (keyCode != 1) {
                ChatUtil.sendClientMessage("Bound '" + this.bmod.getName() + "'" + " to '" + Keyboard.getKeyName(keyCode) + "'");
                this.bmod.setKey(keyCode);
            }
            binding = false;
            Client.instance.fileMgr.saveKeys();
        }
        super.keyTyped(typedChar, keyCode);
    }
    boolean move;
    private boolean dragging;


    @Override
    public void mouseMovedOrUp(int mouseX, int mouseY, int mouseButton) {
        if(dragging) {
            dragging = false;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    public float anim = 700f;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        anim = AnimationUtil.moveUD(anim, 0, 0.6f, 0.04f);
//        GlStateManager.rotate(anim, 0, 0, 1);
        GlStateManager.translate(anim,anim,anim);
        ScaledResolution rs = new ScaledResolution(Minecraft.getMinecraft(),Minecraft.getMinecraft().displayWidth,Minecraft.getMinecraft().displayHeight);
        if (isHovered(startX, startY - 8, startX + 300, startY + 5, mouseX, mouseY) && !isHovered(startX+289, startY-8, startX+296, startY+0, mouseX, mouseY)) {
            if(handler.canExcecute())
                dragging = true;
        }
        if (dragging) {
            if (moveX == 0 && moveY == 0) {
                moveX = mouseX - startX;
                moveY = mouseY - startY;
            } else {
                startX = mouseX - moveX;
                startY = mouseY - moveY;
            }
        }else {
            if (moveX != 0 || moveY != 0) {
                //moveX = 0;
                //moveY = 0;
            }
        }
        if (startX > (float)(rs.getScaledWidth() - 303)) {
            startX = rs.getScaledWidth() - 303;
        }
        if (startX < 3) {
            startX = 3;
        }
        if (startY > (float)(rs.getScaledHeight() - 190)) {
            startY = rs.getScaledHeight() - 190;
        }
        if (startY < 12.0f) {
            startY = 12;
        }


        GL11.glPushMatrix();
        GL11.glEnable(3089);
        //GLScissor
        RenderUtil.doGlScissor(startX,startY - 8, startX +300, startY + 185);
        //Category
        RenderUtil.drawRect(startX, startY - 8, startX + 66, startY  + 185,new Color(64,64,64).getRGB());
        //Mods
        RenderUtil.drawRect(startX + 60, startY + 5, startX + 150, startY  + 185,new Color(59, 59, 59).getRGB());
        //mid bar
        RenderUtil.drawRect(startX + 150, startY + 5, startX + 151, startY  + 185,new Color(42, 41, 41).getRGB());
        //value bar
        RenderUtil.drawRect(startX + 151, startY + 5, startX + 300, startY  + 185,new Color(59, 59, 59).getRGB());

        //Category Render Recode by SuChen
        int CY = 5;
        Category[] arrcategory = Category.values();
        int n2 = arrcategory.length;
        for(int i = 0; i < n2; ++i  ) {
            Category c = Category.values()[i];
            String name = c.name();

//            RenderUtil.drawRect(
//                    startX + 1,
//                    startY + 14+ CY,
//                    startX + 60,
//                    startY + 32+ CY,
//                    new Color(239, 239, 239).getRGB());
            RenderUtil.drawRect(
                    startX + 1,
                    startY + 14+ CY,
                    startX + 60,
                    startY + 32+ CY,
                    selectCategory == i ? new Color(62, 62, 62).getRGB() : new Color(83,83,83).getRGB());


            if(isHovered(startX + 3, startY + 14 + CY, startX + 50, startY + 32+ CY, mouseX, mouseY) ) {
                if (handler.canExcecute()){
                    this.selectCategory = i;
                    this.modlistsize = getModsInCategory(Category.values()[selectCategory]).size();
                }
                RenderUtil.drawRect(
                        startX + 1,
                        startY + 14+ CY,
                        startX + 60,
                        startY + 32+ CY,
                        selectCategory == i ? new Color(62, 62, 62).getRGB() : new Color(154, 154, 151).getRGB());
            }

            //Category name
            Client.instance.fontManager.simpleton13.drawCenteredString(
                    name.substring(0, 1)+ name.toLowerCase().substring(1, name.length()),
                    startX + 4* 7+1,
                    startY + 20 +CY,
                    -1);
            CY += 18;
        }
        Client.instance.fontManager.simpleton20.drawCenteredString(
                "Season",
                startX + 4* 7+1,
                startY,
                -1);
        //windows title
//        Client.instance.fontManager.simpleton13.drawCenteredString("ClickGui", startX + 3 * 10, startY - 5, new Color(170, 170, 170).getRGB());


        //Fuck U SnowFlake
	     /*Client.fontManager.simpleton13.drawCenteredString("Combat", startX + 4* 7, startY + 7, new Color(170, 170, 170).getRGB());
	     Client.fontManager.simpleton13.drawCenteredString("Movement", startX + 4* 7, startY + 17, new Color(170, 170, 170).getRGB());
	     Client.fontManager.simpleton13.drawCenteredString("Render", startX + 4 * 7, startY + 27, new Color(170, 170, 170).getRGB());
	     Client.fontManager.simpleton13.drawCenteredString("Player", startX + 4* 7, startY + 37, new Color(170, 170, 170).getRGB());
	     Client.fontManager.simpleton13.drawCenteredString("World", startX + 4* 7, startY + 47, new Color(170, 170, 170).getRGB());
	     if(isHovered(startX + 3, startY + 7, startX + 50, startY + 15, mouseX, mouseY) && handler.canExcecute()) {
		 selectCategory = 0;
	     }else if(isHovered(startX + 3, startY + 15, startX + 50, startY + 23, mouseX, mouseY) && handler.canExcecute()){
		 selectCategory = 1;
	     }else  if(isHovered(startX + 3, startY + 23, startX + 50, startY + 32, mouseX, mouseY) && handler.canExcecute()){
		 selectCategory = 2;
	     }else if(isHovered(startX + 3, startY + 32, startX + 50, startY + 40, mouseX, mouseY) && handler.canExcecute()){
		 selectCategory = 3;
	     }else if(isHovered(startX + 3, startY + 40, startX + 50, startY + 54, mouseX, mouseY) && handler.canExcecute()){
		 selectCategory = 4;
	     }
	     */

        int x =  startX + 64;
        int y = startY + 10;
        int vY= startY + 10;
        //for head
        for(int i = 0; i < getModsInCategory(Category.values()[selectCategory]).size(); ++i  ) {
            Module mod = (Module)getModsInCategory(Category.values()[selectCategory]).get(i);
            //TODO Mod scroll
            if( isHovered(startX + 60, startY + 5, startX + 150, startY  + 185, mouseX, mouseY) && getModsInCategory(Category.values()[selectCategory]).size() > 12 && isHovered(x, y - 2, x + 82, y+12, mouseX, mouseY)) {
                final float modscroll = (float)Mouse.getDWheel();
                this.modscrollY += modscroll / 10.0f;
            }
            if(getModsInCategory(Category.values()[selectCategory]).size() < 12) {
                modscrollY = 0.0F;
            }
            if (modscrollY > 0.0) {
                modscrollY = 0.0F;
            }
            if(modlistsize > 12
                    && modscrollY < (modlistsize - 12) * -15) {
                modscrollY = (modlistsize - 12) * -15;
            }

            //mod backgorund
            RenderUtil.drawRect(x, y - 2+modscrollY, x + 82, y+12 + modscrollY, mod.isEnabled() ? new Color(83,83,83).getRGB() : new Color(125, 125, 125).getRGB());
            //mod name
            font2.drawCenteredString(bmod == mod ? binding ? "....." : mod.getName()  : mod.getName() , x + 40, y + 2 + modscrollY, -1);
            // has value
            font2.drawCenteredString(mod.hasValues() ? mod.openValues ? "-" : "+" : "", x + 76 , y + 2 + modscrollY, -1);

            //binding
            if(isHovered(startX + 60, startY + 5, startX + 150, startY  + 185, mouseX, mouseY) && isHovered(x, y - 2 + modscrollY, x + 82, y+12 + modscrollY, mouseX, mouseY) && handlerMid.canExcecute()) {
                binding = true;
                bmod = mod;
            }else if (binding && handlerRight.canExcecute()){
                binding = false;
                bmod.setKey(Keyboard.KEY_NONE);
                ChatUtil.sendClientMessage("Unbound '" + this.bmod.getName() + "'");
            }

            //mod open
            if(isHovered(startX + 60, startY + 5, startX + 150, startY  + 185, mouseX, mouseY) && isHovered(x, y - 2 + modscrollY, x + 82, y+12 + modscrollY, mouseX, mouseY) && handler.canExcecute()) {
                mod.set(!mod.isEnabled());
                Client.instance.fileMgr.saveMods();
            }
            String ValueName;
            //Open Value
            if(isHovered(startX + 60, startY + 5, startX + 150, startY  + 185, mouseX, mouseY) && isHovered(x, y - 2 + modscrollY , x + 82, y+12 + modscrollY, mouseX, mouseY) && handlerRight.canExcecute()) {
                mod.openValues = !mod.openValues;
                currentMod = mod;
                this.scrollY = 0.0F;
                this.valuelistsize = getValueList(currentMod).size();
                for(Module mods : ModManager.modList) {
                    if(mods.openValues && mods.getName() != mod.getName()) {
                        mods.openValues = false;
                    }
                }
                Client.instance.fileMgr.saveValues();
            }

            for (final Value value : Value.list) {
                boolean BBoolean = value.getValueName().split("_")[0].equalsIgnoreCase(mod.getName());
                String StrValue = value.getValueName().split("_")[1];
                String StrName = value.getValueName().split("_")[0];

                if(vY > startY  + 185 && isHovered(startX + 151 ,startY - 8, startX +300, startY + 185, mouseX, mouseY)) {
                    final float scroll = (float)Mouse.getDWheel();
                    this.scrollY += scroll / 10.0f;
                }
                if (scrollY > 0.0) {
                    scrollY = 0.0F;
                }
                if(currentMod != null
                        && valuelistsize >= 12
                        && scrollY < (valuelistsize - 12) * -15) {
                    scrollY = (valuelistsize - 12) * -15;
                }
                //openValues ?
                if(mod.openValues) {
                    if (BBoolean) {
                        if (value.isValueString) {
                            GL11.glPushMatrix();
                            GL11.glEnable(3089);
                            RenderUtil.doGlScissor(x + 145, vY + scrollY, x + 230, vY + 8 + scrollY);
                            RenderUtil.drawRect(x + 145, vY + scrollY, x + 230, vY + 8 + scrollY, caninput ? new Color(84, 90, 90).getRGB() : new Color(115, 130, 140).getRGB());
                            font2.drawBoldString(value.getText() + "", x + 147, vY + 1 + scrollY, -1);
//                            font2.drawBoldString("_", x + 144 + width, vY + 1 + scrollY, -1);

                            GL11.glDisable(3089);
                            GL11.glPopMatrix();

                            if (isHovered(x + 145, vY + scrollY, x + 230, vY + 8 + scrollY, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                                this.caninput = true;
                            }
                            if (caninput && this.handlerRight.canExcecute()){
                                this.caninput = false;
                                Client.instance.fileMgr.saveValues();
                            }
                            font2.drawBoldString(StrValue, x + 90, vY + scrollY, new Color(153, 153, 169).getRGB());
                            vY += 15;
                        } else
                        if (value.isValueDouble) {
                            this.width = 100;
                            float lastMouseX = -1.0f;
                            final double val = (double) value.getValueState();
                            final double min = (double) value.getValueMin();
                            final double max = (double) value.getValueMax();
                            double percSlider = ((double) value.getValueState() - (double) value.getValueMin()) / ((double) value.getValueMax() - (double) value.getValueMin());

                            final double valAbs = mouseX - (x + 145);
                            double perc = valAbs / 85;
                            perc = Math.min(Math.max(0.0, perc), 1.0);
                            final double valRel = ((double) value.getValueMax() - (double) value.getValueMin()) * perc;
                            double valuu = (double) value.getValueMin() + valRel;
                            double valu = (x + 145) + 85 * percSlider;
                            //down bar
                            RenderUtil.drawRect(x + 145, vY + 3 + scrollY, x + 230, vY + 5 + scrollY, new Color(115, 130, 140).getRGB());
                            //
                            RenderUtil.drawRect(valu,
                                    vY + 2 + scrollY,
                                    valu + 2,
                                    vY + 6 + scrollY,
                                    new Color(253, 105, 229).getRGB());

                            RenderUtil.drawRect(x + 145,
                                    vY + 3 + scrollY,
                                    valu + 2,
                                    vY + 5 + scrollY,
                                    new Color(253, 105, 229).getRGB());

                            font2.drawBoldString("" + (Double) value.getValueState(), x + 145, vY - 4 + scrollY, new Color(153, 153, 169).getRGB());

                            font2.drawBoldString(StrValue, x + 90, vY + scrollY, new Color(153, 153, 169).getRGB());
                            //TODO Double Hovered
                            if (isHovered(startX + 151, startY + 5, startX + 300, startY + 185, mouseX, mouseY) && isHovered(x + 145, vY + 1 + scrollY, x + 230, vY + 7 + scrollY, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                                lastMouseX = (Math.min(Math.max(x + 155, mouseX), x + 145 + 100) - (float) x + 145) / 100;
                                valuu = Math.round(valuu * (1.0 / value.getSteps())) / (1.0 / value.getSteps());
                                value.setValueState(valuu);
                                Client.instance.fileMgr.saveValues();
                            } else {
                                valuu = Math.round((double) value.getValueState() * (1.0 / value.getSteps())) / (1.0 / value.getSteps());
                                value.setValueState(valuu);
                            }
                            vY += 15;
                        }
                        //MODE Recode by SuChen
                        if (value.isValueMode) {
                            String modeName = value.getModeAt(value.getCurrentMode());
                            String NameText = String.valueOf((Object) value.getModeTitle());
                            String modeCountText = String.valueOf((int) (value.getCurrentMode() + 1)) + "/" + value.mode.size();
                            RenderUtil.drawRect(x + 145, vY + scrollY - 1, x + 151, vY + 7 + scrollY, new Color(253, 105, 229).getRGB());

                            GlStateManager.pushMatrix();
                            GlStateManager.translate((double) (x + 148.0F), (double) vY + scrollY + 4, 0.0D);
                            GlStateManager.rotate(mod.openValues ? 180.0F : 90.0F, 0.0F, 0.0F, 90.0F);
                            RenderUtil.rectangle(-1.0D, 0.0D, -0.5D, 2.5D, Colors.getColor(151));
                            RenderUtil.rectangle(-0.5D, 0.0D, 0.0D, 2.5D, Colors.getColor(151));
                            RenderUtil.rectangle(0.0D, 0.5D, 0.5D, 2.0D, Colors.getColor(151));
                            RenderUtil.rectangle(0.5D, 1.0D, 1.0D, 1.5D, Colors.getColor(151));
                            GlStateManager.popMatrix();

                            RenderUtil.drawRect(x + 152, vY + scrollY - 1, x + 208, vY + 7 + scrollY, Colors.GREY.c);

                            RenderUtil.drawRect(x + 209, vY + scrollY - 1, x + 215, vY + 7 + scrollY, new Color(253, 105, 229).getRGB());

                            GlStateManager.pushMatrix();
                            GlStateManager.translate((double) (x + 212.0F), (double) vY + scrollY + 1.8, 0.0D);
                            GlStateManager.rotate(mod.openValues ? 360.0F : 90.0F, 0.0F, 0.0F, 90.0F);
                            RenderUtil.rectangle(-1.0D, 0.0D, -0.5D, 2.5D, Colors.getColor(151));
                            RenderUtil.rectangle(-0.5D, 0.0D, 0.0D, 2.5D, Colors.getColor(151));
                            RenderUtil.rectangle(0.0D, 0.5D, 0.5D, 2.0D, Colors.getColor(151));
                            RenderUtil.rectangle(0.5D, 1.0D, 1.0D, 1.5D, Colors.getColor(151));
                            GlStateManager.popMatrix();

                            font2.drawBoldString(modeName, x + 180 - font2.getStringWidth("" + modeName) / 2, vY + scrollY - 1, -1);

                            font2.drawBoldString(NameText, x + 90, vY + scrollY - 1, new Color(153, 153, 169).getRGB());

                            font2.drawBoldString(modeCountText, x + 230 - font2.getStringWidth(modeCountText), vY + scrollY, new Color(153, 153, 169).getRGB());

                            if (isHovered(startX + 151, startY + 5, startX + 300, startY + 184, mouseX, mouseY) && isHovered(x + 145, vY + scrollY - 1, x + 151, vY + 7 + scrollY, mouseX, mouseY) && handler.canExcecute()) {

                                if (value.getCurrentMode() > 0 && value.getCurrentMode() != 0) {
                                    value.setCurrentMode(value.getCurrentMode() - 1);
                                    Client.instance.fileMgr.saveValues();
                                } else {
                                    value.setCurrentMode(value.mode.size() - 1);
                                    Client.instance.fileMgr.saveValues();
                                }
                            }

                            if (isHovered(startX + 151, startY + 5, startX + 300, startY + 184, mouseX, mouseY) && isHovered(x + 209, vY + scrollY - 1, x + 215, vY + 7 + scrollY, mouseX, mouseY) && handler.canExcecute()) {
                                if (value.getCurrentMode() < value.mode.size() - 1) {
                                    value.setCurrentMode(value.getCurrentMode() + 1);
                                    Client.instance.fileMgr.saveValues();
                                } else {
                                    value.setCurrentMode(0);
                                    Client.instance.fileMgr.saveValues();
                                }
                            }

                            vY += 15;
                        }

                        //TODO Boolean
                        if (value.isValueBoolean) {
                            RenderUtil.drawRect(x + 210, vY + scrollY - 1, x + 230, vY + 7 + scrollY, new Color(115, 130, 140).getRGB());
                            if ((Boolean) value.getValueState()) {
                                RenderUtil.drawRect(x + 220, vY + scrollY, x + 229, vY + 6 + scrollY, new Color(253, 105, 229).getRGB());
                            } else {
                                RenderUtil.drawRect(x + 211, vY + scrollY, x + 220, vY + 6 + scrollY, new Color(153, 153, 153).getRGB());
                            }
                            //Boolean
                            if (isHovered(x + 210, vY + scrollY - 1, x + 230, vY + 7 + scrollY, mouseX, mouseY) && handler.canExcecute()) {
                                value.setValueState(!(Boolean) value.getValueState());
                                Client.instance.fileMgr.saveValues();
                            }

                            font2.drawBoldString(StrValue, x + 90, vY + scrollY, new Color(153, 153, 169).getRGB());
                            vY += 15;
                        }
                    }
                }
            }
            y+= 15;
        }

        //top bar
        RenderUtil.drawRect(startX + 60, startY - 8, startX + 300, startY  + 5,new Color(75, 74, 74).getRGB());
        GL11.glDisable(3089);
        GL11.glPopMatrix();
    }

    /**
     *
     * @param x left
     * @param y top
     * @param x2 right
     * @param y2 bottom
     * @param mouseX
     * @param mouseY
     * @return mouse hovered ?
     */
    public boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        if (mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2) {
            return true;
        }
        return false;
    }


    @Override
    public void initGui() {
//        mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
    }

    @Override
    public void onGuiClosed() {
//        if (mc.entityRenderer.theShaderGroup != null) {
//            mc.entityRenderer.theShaderGroup.deleteShaderGroup();
//            mc.entityRenderer.theShaderGroup = null;
//        }
        super.onGuiClosed();
    }


    public static double round(double value) {
        return (double)Math.round(value * 100.0D) / 100.0D;
    }

    /**
     * @author SuChen
     * @param mods Mod
     * @return valueList
     */
    public static List getValueList(Module mods) {
        List valueList = new ArrayList();
        Iterator var3 = Value.list.iterator();
        while(var3.hasNext()) {
            Value value = (Value)var3.next();
            if(currentMod == null) {
                return null;
            }
            if (value.getValueName().split("_")[0].equalsIgnoreCase(mods.getName())) {
                valueList.add(value);
            }
        }
        return valueList;
    }

    /**
     * @author SnowFalke
     * @param category
     * @return modList
     */
    public static List getModsInCategory(Category category) {
        List modList = new ArrayList();
        Iterator var3 = ModManager.getModList().iterator();

        while(var3.hasNext()) {
            Module mod = (Module)var3.next();
            if (mod.getCategory() == category) {
                modList.add(mod);
            }
        }
        return modList;
    }

}