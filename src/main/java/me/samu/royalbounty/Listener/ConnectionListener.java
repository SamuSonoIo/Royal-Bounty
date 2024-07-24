package me.samu.royalbounty.Listener;

import me.samu.royalbounty.Database.CustomPlayer;
import me.samu.royalbounty.RoyalBounty;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.UUID;

public class ConnectionListener implements Listener {

    private RoyalBounty royalBounty;

    public ConnectionListener(RoyalBounty royalBounty) { this.royalBounty = royalBounty; }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        try {
            CustomPlayer playerData = new CustomPlayer(royalBounty, uuid);
            royalBounty.getPlayerManager().addCustomPlayer(uuid, playerData);
        } catch (SQLException ex) {
            player.kickPlayer("Impossible to load your data! Contact an admin.");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        royalBounty.getPlayerManager().removeCustomPlayer(e.getPlayer().getUniqueId());
    }
}
