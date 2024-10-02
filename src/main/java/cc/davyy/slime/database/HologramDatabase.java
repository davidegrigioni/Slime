package cc.davyy.slime.database;

import cc.davyy.slime.database.entities.Hologram;
import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

@Singleton
public class HologramDatabase {

    private final Dao<Hologram, Integer> hologramsDao;

    public HologramDatabase(@NotNull String path) throws SQLException {
        ConnectionSource connectionSource = new JdbcConnectionSource("jdbc:sqlite:" + path);
        TableUtils.createTableIfNotExists(connectionSource, Hologram.class);
        hologramsDao = DaoManager.createDao(connectionSource, Hologram.class);
    }

    public void createHologram(int id, @NotNull Component text) throws SQLException {
        if (!hologramExists(id)) {
            Hologram hologram = new Hologram();
            hologram.setId(id);
            hologram.setText(text);
            hologramsDao.create(hologram);
        }
    }

    public boolean hologramExists(int id) throws SQLException {
        return hologramsDao.idExists(id);
    }

    public void deleteHologram(int id) throws SQLException {
        if (hologramExists(id)) {
            hologramsDao.deleteById(id);
        }
    }

    public Hologram getHologram(int id) throws SQLException {
        return hologramsDao.queryForId(id);
    }

}