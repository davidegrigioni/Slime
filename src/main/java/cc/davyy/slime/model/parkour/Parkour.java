package cc.davyy.slime.model.parkour;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public record Parkour(int id, @NotNull Component parkourName,
                      @NotNull ParkourCheckpoint parkourCheckpoint) {}