package me.samu.royalbounty.Database;

import me.samu.royalbounty.Database.CustomPlayer;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

    private HashMap<UUID, CustomPlayer> customPlayers = new HashMap();

    public CustomPlayer getCustomPlayer(UUID uuid) {
        return customPlayers.get(uuid);
    }

    public void addCustomPlayer(UUID uuid, CustomPlayer customPlayer) {
        customPlayers.put(uuid, customPlayer);
    }

    public void removeCustomPlayer(UUID uuid) {
        customPlayers.remove(uuid);
    }

}
