package cc.davyy.slime.database;

import cc.davyy.slime.database.entities.Disguise;
import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

@Singleton
public class DisguiseDatabase {

    private final Dao<Disguise, String> disguisesDao;

    public DisguiseDatabase(@NotNull String path) throws SQLException {
        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:sqlite:" + path);
        TableUtils.createTableIfNotExists(connectionSource, Disguise.class);
        disguisesDao = DaoManager.createDao(connectionSource, Disguise.class);
    }

    public void saveDisguise(@NotNull Disguise disguise) throws SQLException {
        disguisesDao.createOrUpdate(disguise);
    }

    public Disguise getDisguise(@NotNull String playerId) throws SQLException {
        return disguisesDao.queryForId(playerId);
    }

    public void removeDisguise(@NotNull String playerId) throws SQLException {
        disguisesDao.deleteById(playerId);
    }

}