package cc.davyy.slime.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

@DatabaseTable(tableName = "holograms")
public class Hologram {

    @DatabaseField(id = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String serializedText;

    public Hologram() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Component getText() {
        return GsonComponentSerializer.gson().deserialize(serializedText);
    }

    public void setText(Component text) {
        this.serializedText = GsonComponentSerializer.gson().serialize(text);
    }

}