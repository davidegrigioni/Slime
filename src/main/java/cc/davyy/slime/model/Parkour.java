package cc.davyy.slime.model;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public record Parkour(int id, @NotNull Component parkourName,
                      @NotNull ParkourCheckpoint parkourCheckpoint) {}