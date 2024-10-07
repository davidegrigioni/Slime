package cc.davyy.slime.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

@DatabaseTable(tableName = "playerprofile")
public class PlayerProfile {

    @DatabaseField(id = true)
    private UUID playerID;

    @DatabaseField()
    private String playerName;

    @DatabaseField(defaultValue = "false")
    private boolean isStaff;

    @DatabaseField(defaultValue = "false")
    private boolean isVanished;

    @DatabaseField
    private long playtime;

    @DatabaseField(defaultValue = "0")
    private int kills;

    @DatabaseField(defaultValue = "0")
    private int deaths;

    @DatabaseField(defaultValue = "0")
    private int level;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Disguise disguise;

    public PlayerProfile() {
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public void setPlayerID(UUID playerID) {
        this.playerID = playerID;
    }

    public boolean isStaff() {
        return isStaff;
    }

    public void setStaff(boolean staff) {
        isStaff = staff;
    }

    public boolean isVanished() {
        return isVanished;
    }

    public void setVanished(boolean vanished) {
        isVanished = vanished;
    }

    public Disguise getDisguise() {
        return disguise;
    }

    public void setDisguise(Disguise disguise) {
        this.disguise = disguise;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public long getPlaytime() {
        return playtime;
    }

    public void setPlaytime(long playtime) {
        this.playtime = playtime;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}