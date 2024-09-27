package cc.davyy.slime.services;

import cc.davyy.slime.model.SlimePlayer;
import org.jetbrains.annotations.NotNull;

public interface GUIService {

    void open(@NotNull SlimePlayer player);

    void updateGUI();

    void listen();

}