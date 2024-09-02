package cc.davyy.slime.model;

import net.minestom.server.instance.SharedInstance;
import org.jetbrains.annotations.NotNull;

public record Lobby(int id, @NotNull String name, @NotNull SharedInstance sharedInstance) {}