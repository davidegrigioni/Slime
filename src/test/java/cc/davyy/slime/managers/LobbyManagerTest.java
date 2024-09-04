package cc.davyy.slime.managers;

import cc.davyy.slime.testing.Env;
import cc.davyy.slime.testing.EnvTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@EnvTest
class LobbyManagerTest {

    private LobbyManager lobbyManager;

    @BeforeEach
    void setup() {
        lobbyManager = new LobbyManager();
    }

    @Test
    public void lobbyTest(Env env) {
        var instance = env.createFlatInstance();
        lobbyManager.createNewLobby();

    }

}