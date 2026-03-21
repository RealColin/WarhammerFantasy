package realcolin.whmod.faction;

import net.minecraft.network.chat.Component;

public record FactionTrait(Component text, Component desc, TraitType type) {
}
