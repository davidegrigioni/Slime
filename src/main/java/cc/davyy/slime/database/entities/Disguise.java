package cc.davyy.slime.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

@DatabaseTable(tableName = "disguises")
public class Disguise {

    @DatabaseField(id = true)
    private UUID playerId;

    @DatabaseField(canBeNull = false)
    private String disguiseType;

    @DatabaseField
    private String nickname;

    @DatabaseField
    private String entityType;

    public Disguise() {
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public String getDisguiseType() {
        return disguiseType;
    }

    public void setDisguiseType(String disguiseType) {
        this.disguiseType = disguiseType;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

}