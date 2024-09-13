package cc.davyy.slime.managers;

import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.MinecraftServer;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static cc.davyy.slime.utils.FileUtils.getConfig;
import static net.kyori.adventure.text.Component.text;

/**
 * Manages and animates the server's brand name.
 * <p>
 * This class handles the animation of the server's brand name by cycling through a list of styles
 * defined in the configuration. The animation interval and styles are configured through YAML configuration files.
 */
@Singleton
public class BrandManager {

    /**
     * Logger for the BrandManager class.
     */
    private static final ComponentLogger LOGGER = ComponentLogger.logger(BrandManager.class);

    /**
     * List of brand name styles to animate through.
     */
    private final List<String> brandNameStyles;

    /**
     * Interval between brand name style changes, in milliseconds.
     */
    private final long animationInterval;

    /**
     * Default brand name when animation is disabled.
     */
    private final String defaultBrandName;

    /**
     * Index of the current brand name style in the list.
     */
    private int currentIndex = 0;

    /**
     * Constructs a BrandManager instance and initializes the animation settings.
     * <p>
     * Loads the brand name styles and animation interval from the configuration. If animation is enabled,
     * starts the brand name animation.
     */
    public BrandManager() {
        this.brandNameStyles = getConfig().getStringList("branding.animation-styles");
        this.animationInterval = getConfig().getLong("branding.animation-interval");
        this.defaultBrandName = getConfig().getString("branding.brand-name");
        final boolean animateEnabled = getConfig().getBoolean("branding.animate");

        if (!animateEnabled) {
            setDefaultBrandName();
            return;
        }

        if (animationInterval <= 0) {
            LOGGER.warn("Animation interval is non-positive, animation will not start.");
            setDefaultBrandName();
            return;
        }

        startAnimation();
    }

    /**
     * Starts the brand name animation task.
     * <p>
     * Schedules a repeating task that updates the brand name at the interval specified in the configuration.
     */
    public void startAnimation() {
        MinecraftServer.getSchedulerManager().buildTask(this::updateBrandName)
                .repeat(Duration.of(animationInterval, TimeUnit.SECONDS.toChronoUnit()))
                .schedule();
    }

    /**
     * Updates the server's brand name to the next style in the list.
     * <p>
     * Retrieves the current brand name style from the list, updates the server's brand name, and advances
     * to the next style. If there are no styles available, logs a message and does not update the brand name.
     */
    private void updateBrandName() {
        if (brandNameStyles == null || brandNameStyles.isEmpty()) {
            LOGGER.warn("No brand name styles available for animation.");
            return;
        }

        try {
            final String brandName = brandNameStyles.get(currentIndex);

            final Component brandComponent = text(brandName);
            final String serializedBrandComponent = LegacyComponentSerializer.legacySection().serialize(brandComponent);

            MinecraftServer.setBrandName(serializedBrandComponent);

            currentIndex = (currentIndex + 1) % brandNameStyles.size();
        } catch (Exception ex) {
            LOGGER.error("Error updating brand name", ex);
        }
    }

    /**
     * Sets the default brand name when animation is not enabled.
     */
    private void setDefaultBrandName() {
        try {

            final Component brandComponent = text(defaultBrandName);
            final String serializedBrandComponent = LegacyComponentSerializer
                    .legacySection()
                    .serialize(brandComponent);

            MinecraftServer.setBrandName(serializedBrandComponent);

            LOGGER.info("Animation disabled, setting default brand name: {}", defaultBrandName);
        } catch (Exception ex) {
            LOGGER.error("Error setting default brand name", ex);
        }
    }

}