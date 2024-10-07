package cc.davyy.slime.utils;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public final class AsyncUtils {

    private static final ComponentLogger LOGGER = ComponentLogger.logger(AsyncUtils.class);

    private AsyncUtils() {}

    public static @NotNull CompletableFuture<Void> runAsync(@NotNull Runnable runnable) {
        return CompletableFuture.runAsync(() -> {
            try {
                runnable.run();
            } catch (Exception ex) {
                LOGGER.error("Error in running async task", ex);
            }
        });
    }

    public static <T> @NotNull CompletableFuture<T> supplyAsync(@NotNull Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return supplier.get();
            } catch (Exception ex) {
                LOGGER.error("Error in supplying async task", ex);
                return null;
            }
        });
    }

}