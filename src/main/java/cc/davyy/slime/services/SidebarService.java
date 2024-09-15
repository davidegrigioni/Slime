package cc.davyy.slime.services;

import cc.davyy.slime.model.SlimePlayer;
import org.jetbrains.annotations.NotNull;

public interface SidebarService {

    void showSidebar(@NotNull SlimePlayer player);

    void toggleSidebar(@NotNull SlimePlayer player);

    void removeSidebar(@NotNull SlimePlayer player);

}