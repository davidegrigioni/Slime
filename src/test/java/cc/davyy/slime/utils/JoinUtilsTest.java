package cc.davyy.slime.utils;

import cc.davyy.slime.constants.TagConstants;
import cc.davyy.slime.testing.Env;
import cc.davyy.slime.testing.EnvTest;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@EnvTest
class JoinUtilsTest {

    private static final ItemStack COMPASS = ItemStack.builder(Material.COMPASS)
            .set(TagConstants.ACTION_TAG, "serversl")
            .build();
    private static final ItemStack LOBBY_SELECTOR = ItemStack.builder(Material.NETHER_STAR)
            .set(TagConstants.ACTION_TAG, "lobbysl")
            .build();

    private static final String LOBBY_SELECTOR_ACTION_VALUE = "lobbysl";

    @Test
    void applyJoinKit(Env env) {
        var instance = env.createFlatInstance();
        var player = env.createPlayer(instance, new Pos(0, 0, 0));
        var playerInv = player.getInventory();

        playerInv.setItemStack(4, COMPASS);
        playerInv.setItemStack(8, LOBBY_SELECTOR);

        assertEquals(playerInv.getItemStack(4), COMPASS);
        assertEquals(playerInv.getItemStack(8), LOBBY_SELECTOR);

        String tagValue = LOBBY_SELECTOR.getTag(TagConstants.ACTION_TAG);
        assertEquals(LOBBY_SELECTOR_ACTION_VALUE, tagValue);
    }

}