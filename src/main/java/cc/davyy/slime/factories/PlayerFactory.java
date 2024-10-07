package cc.davyy.slime.factories;

import cc.davyy.slime.database.entities.PlayerProfile;
import com.google.inject.Singleton;

@Singleton
public class PlayerFactory {

    public PlayerProfile createNewProfile() {
        return new PlayerProfile();
    }

}