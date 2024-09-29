package cc.davyy.slime.managers.general;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private final ComponentLogger LOGGER = ComponentLogger.logger("ConfigManager");
    private static final String CONFIG_FOLDER = "configs/";

    private YamlDocument config;
    private YamlDocument messages;
    private YamlDocument ui;

    public ConfigManager() {
        setup();
    }

    public void setup() {
        try {
            config = YamlDocument.create(new File(CONFIG_FOLDER + "config.yml"),
                    getClass().getClassLoader()
                            .getResourceAsStream(CONFIG_FOLDER + "config.yml"),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder()
                            .setAutoUpdate(true)
                            .build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder()
                            .setVersioning(
                                    new BasicVersioning("file-version"))
                            .build());
            messages = YamlDocument.create(new File(CONFIG_FOLDER + "messages.yml"),
                    getClass().getClassLoader()
                            .getResourceAsStream(CONFIG_FOLDER + "messages.yml"),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder()
                            .setAutoUpdate(true)
                            .build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder()
                            .setVersioning(
                                    new BasicVersioning("file-version"))
                            .build());
            ui = YamlDocument.create(new File(CONFIG_FOLDER + "ui.yml"),
                    getClass().getClassLoader()
                            .getResourceAsStream(CONFIG_FOLDER + "ui.yml"),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder()
                            .setAutoUpdate(true)
                            .build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder()
                            .setVersioning(
                                    new BasicVersioning("file-version"))
                            .build());
        } catch (IOException ex) {
            LOGGER.error("Error in creating configs", ex);
        }
    }

    public void setConfig(@NotNull String route, @NotNull Object object) {
        try {
            config.set(route, object);
            config.save();
        } catch (IOException ex) {
            LOGGER.error("Error in setting config values", ex);
        }
    }

    public void reload() {
        try {
            config.reload();
            messages.reload();
            ui.reload();
        } catch (IOException ex) {
            LOGGER.error("Error in reloading config values", ex);
        }
    }

    public YamlDocument getUi() { return ui; }

    public YamlDocument getConfig() { return config; }

    public YamlDocument getMessages() { return messages; }

}