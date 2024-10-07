package cc.davyy.slime.factories;

import cc.davyy.slime.database.entities.Disguise;
import com.google.inject.Singleton;

@Singleton
public final class DisguiseFactory {

    public Disguise createDisguise() {
        return new Disguise();
    }

}