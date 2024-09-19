package cc.davyy.slime.managers;

import com.google.inject.Singleton;
import net.minestom.server.entity.PlayerSkin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.*;

@Singleton
public class SkinManager {

    // Cache to store player skins, with the key being UUID or Username
    private final Map<String, CachedSkin> skinCache = new ConcurrentHashMap<>();

    // Executor for running async tasks
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    // Cache expiration time (e.g., 1 hour)
    private static final long CACHE_EXPIRATION_TIME_MS = TimeUnit.HOURS.toMillis(1);

    public CompletableFuture<PlayerSkin> getSkinFromUsernameAsync(@NotNull String username) {
        return getSkinAsync(username, () -> PlayerSkin.fromUsername(username));
    }

    private CompletableFuture<PlayerSkin> getSkinAsync(@NotNull String key, @NotNull SkinProvider skinProvider) {
        return CompletableFuture.supplyAsync(() -> {
            final CachedSkin cachedSkin = skinCache.get(key);

            if (cachedSkin != null && !cachedSkin.isExpired()) {
                return cachedSkin.playerSkin();
            }

            final PlayerSkin playerSkin = skinProvider.requestSkin();
            skinCache.put(key, new CachedSkin(playerSkin, System.currentTimeMillis()));
            return playerSkin;
        }, executorService);
    }

    @FunctionalInterface
    private interface SkinProvider {
        PlayerSkin requestSkin();
    }

    private record CachedSkin(@NotNull PlayerSkin playerSkin, long cacheTimeStamp) {

        public boolean isExpired() {
            return (System.currentTimeMillis() - cacheTimeStamp) > CACHE_EXPIRATION_TIME_MS;
        }

    }

}