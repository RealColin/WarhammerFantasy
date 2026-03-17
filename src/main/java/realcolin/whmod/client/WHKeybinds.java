package realcolin.whmod.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class WHKeybinds {
    public static final String CATEGORY = "key.categories.whmod";

    public static KeyMapping OPEN_MENU;

    public static void init() {
        OPEN_MENU = new KeyMapping(
                "key.whmod.open_menu",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                CATEGORY
        );
    }
}
