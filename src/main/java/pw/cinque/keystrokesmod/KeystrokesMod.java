package pw.cinque.keystrokesmod;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.client.ClientCommandHandler;
import pw.cinque.keystrokesmod.gui.GuiKeystrokes;
import pw.cinque.keystrokesmod.gui.key.FillerKey;
import pw.cinque.keystrokesmod.gui.key.Key;
import pw.cinque.keystrokesmod.gui.key.KeyHolder;

/**
 * The mod's base class.
 */
@Mod(name = "KeystrokesMod", modid = "keystrokesmod", version = "v5")
@Getter
public class KeystrokesMod {

    private KeyHolder keyHolder;
    private GuiKeystrokes gui;
    private boolean showGui;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new KeystrokesCommand(this));
        FMLCommonHandler.instance().bus().register(new KeystrokesRenderer(this));
        FMLCommonHandler.instance().bus().register(this);
        buildKeyHolder();

        gui = new GuiKeystrokes(this);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (showGui) {
            Minecraft.getMinecraft().displayGuiScreen(gui);
            showGui = false;
        }
    }

    void openGui() {
        showGui = true;
    }

    private void buildKeyHolder() {
        GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;

        // the key layout is currently not configurable
        Key none = new FillerKey();
        Key keyW = new Key(gameSettings.keyBindForward);
        Key keyA = new Key(gameSettings.keyBindLeft);
        Key keyS = new Key(gameSettings.keyBindBack);
        Key keyD = new Key(gameSettings.keyBindRight);
        Key leftMouse = new Key(gameSettings.keyBindAttack).setLeftMouse();
        Key rightMouse = new Key(gameSettings.keyBindUseItem).setRightMouse();
        Key keySpaceBar = new Key(gameSettings.keyBindJump).setSpaceBar();

        keyHolder = new KeyHolder.Builder()
                .setWidth(82)
                .setGapSize(2)
                .addRow(none, keyW, none)
                .addRow(keyA, keyS, keyD)
                .addRow(leftMouse, rightMouse)
                .addRow(keySpaceBar)
                .build();
    }

}
