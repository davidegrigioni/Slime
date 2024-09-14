package cc.davyy.slime.interfaces;

import cc.davyy.slime.model.SlimePlayer;
import org.jetbrains.annotations.NotNull;

public interface ISidebar {

    void showSidebar(@NotNull SlimePlayer player);

    void toggleSidebar(@NotNull SlimePlayer player);

    void removeSidebar(@NotNull SlimePlayer player);

}