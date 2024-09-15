package cc.davyy.slime.cosmetics;

import cc.davyy.slime.cosmetics.model.Cosmetic;
import cc.davyy.slime.model.SlimePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface CosmeticService<T> {

    void createCosmetic(@NotNull String name,
                        @NotNull T data,
                        @NotNull Cosmetic.CosmeticType type);

    void removeCosmetic(int id);

    Optional<Cosmetic<T>> getCosmeticByID(int id);

    void applyCosmetic(@NotNull SlimePlayer player, int id);

    void removeCosmetic(@NotNull SlimePlayer player, int id);

    @NotNull
    List<Cosmetic<T>> getAvailableCosmetics();

}