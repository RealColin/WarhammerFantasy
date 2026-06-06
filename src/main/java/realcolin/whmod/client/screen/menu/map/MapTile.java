package realcolin.whmod.client.screen.menu.map;

import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;

public final class MapTile implements AutoCloseable {
    private final Identifier identifier;
    private final DynamicTexture texture;

    public MapTile(Identifier identifier, DynamicTexture texture) {
        this.identifier = identifier;
        this.texture = texture;
    }

    public Identifier identifier() {
        return identifier;
    }

    @Override
    public void close() {
        texture.close();
    }
}
