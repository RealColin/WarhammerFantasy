package realcolin.whmod.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import realcolin.whmod.WHMod;

@SuppressWarnings("deprecation")
public class WHKeybinds {
    public static final KeyMapping.Category WARHAMMER =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath(WHMod.MOD_ID, "warhammer"));

    public static KeyMapping OPEN_MENU;

    public static void init() {
        OPEN_MENU = new KeyMapping(
                "key.whmod.open_menu",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                WARHAMMER
        );
    }
}
