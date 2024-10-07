package cc.davyy.slime.database;

import cc.davyy.slime.database.entities.Disguise;
import cc.davyy.slime.database.entities.PlayerProfile;
import cc.davyy.slime.utils.AsyncUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Singleton
public class DatabaseManager {

    private static final ComponentLogger LOGGER = ComponentLogger.logger(DatabaseManager.class);

    private final Dao<PlayerProfile, UUID> playerProfileDao;
    private final Dao<Disguise, UUID> disguiseDao;

    @Inject
    public DatabaseManager(@NotNull String path) throws SQLException {
        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:sqlite:" + path);

        playerProfileDao = DaoManager.createDao(connectionSource, PlayerProfile.class);
        disguiseDao = DaoManager.createDao(connectionSource, Disguise.class);

        TableUtils.createTableIfNotExists(connectionSource, PlayerProfile.class);
        TableUtils.createTableIfNotExists(connectionSource, Disguise.class);
    }

    // Save or update PlayerProfile
    public CompletableFuture<Void> saveOrUpdatePlayerProfile(@NotNull PlayerProfile playerProfile) {
        return AsyncUtils.runAsync(() -> {
            try {
                playerProfileDao.createOrUpdate(playerProfile);
            } catch (SQLException ex) {
                LOGGER.error("Error in creating or updating PlayerProfile DAO", ex);
            }
        });
    }

    // Retrieve PlayerProfile by player ID
    public CompletableFuture<Optional<PlayerProfile>> getPlayerProfile(@NotNull UUID playerID) {
        return AsyncUtils.supplyAsync(() -> {
            try {
                return Optional.ofNullable(playerProfileDao.queryForId(playerID));
            } catch (SQLException ex) {
                LOGGER.error("Error in getting PlayerProfile", ex);
                return Optional.empty();
            }
        });
    }

    // Delete PlayerProfile by player ID
    public CompletableFuture<Void> deletePlayerProfile(@NotNull UUID playerID) {
        return AsyncUtils.runAsync(() -> {
            try {
                playerProfileDao.deleteById(playerID);
            } catch (SQLException ex) {
                LOGGER.error("Error in deleting PlayerProfile", ex);
            }
        });
    }

    // Save or update Disguise
    public CompletableFuture<Void> saveOrUpdateDisguise(@NotNull Disguise disguise) {
        return AsyncUtils.runAsync(() -> {
            try {
                disguiseDao.createOrUpdate(disguise);
            } catch (SQLException ex) {
                LOGGER.error("Error in creating or updating DisguiseDAO", ex);
            }
        });
    }

    // Retrieve Disguise by player ID
    public CompletableFuture<Optional<Disguise>> getDisguise(@NotNull UUID playerID) {
        return AsyncUtils.supplyAsync(() -> {
            try {
                return Optional.ofNullable(disguiseDao.queryForId(playerID));
            } catch (SQLException ex) {
                return Optional.empty();
            }
        });
    }

    // Delete Disguise by player ID
    public CompletableFuture<Void> deleteDisguise(@NotNull UUID playerID) {
        return AsyncUtils.runAsync(() -> {
            try {
                disguiseDao.deleteById(playerID);
            } catch (SQLException ex) {
                LOGGER.error("Error in deleting DisguiseDAO", ex);
            }
        });
    }

    // Utility to check if a player has a saved profile
    public CompletableFuture<Boolean> hasPlayerProfile(@NotNull UUID playerID) {
        return AsyncUtils.supplyAsync(() -> {
            try {
                return playerProfileDao.idExists(playerID);
            } catch (SQLException ex) {
                LOGGER.error("Error checking if PlayerProfile exists", ex);
                return false;
            }
        });
    }

}