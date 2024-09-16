package cc.davyy.slime.cosmetics.model;

import cc.davyy.slime.model.SlimePlayer;
import org.jetbrains.annotations.NotNull;

public interface Cosmetic {

    int id();
    String name();
    CosmeticType type();

    // Apply the cosmetic to the player
    void apply(@NotNull SlimePlayer player);

    // Remove the cosmetic from the player
    void remove(@NotNull SlimePlayer player);

    enum CosmeticType {
        HAT,
        ARMOR,
        PET,
        PARTICLE,
    }

}