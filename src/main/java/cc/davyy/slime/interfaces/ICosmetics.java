package cc.davyy.slime.interfaces;

import cc.davyy.slime.model.Cosmetic;
import cc.davyy.slime.model.SlimePlayer;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ICosmetics {

    void createCosmetic(@NotNull String cosmeticName, @NotNull String cosmeticDescription,
                        @NotNull ItemStack cosmeticItem, @NotNull Cosmetic.CosmeticType cosmeticType);

    void removeCosmetic(int id);

    Cosmetic getCosmetic(int id);

    void applyCosmetic(@NotNull SlimePlayer player, int id);

    void removeCosmetic(@NotNull SlimePlayer player, int id);

    List<Cosmetic> getAvailableCosmetics();

}