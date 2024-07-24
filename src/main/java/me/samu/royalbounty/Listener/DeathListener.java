package me.samu.royalbounty.Listener;

import me.samu.royalbounty.RoyalBounty;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Objects;

public class DeathListener implements Listener {

    private RoyalBounty royalBounty;
    private final Economy economy;

    public DeathListener(RoyalBounty royalBounty) {
        this.royalBounty = royalBounty;
        this.economy = RoyalBounty.getEcon();
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (e.getEntity().getType().equals(EntityType.PLAYER) &&
                e.getEntity().getKiller() != null &&
                e.getEntity().getKiller().getType().equals(EntityType.PLAYER))
        {
            Player player = e.getEntity().getKiller();
            Player target = (Player) e.getEntity();
            if (royalBounty.getPlayerManager().getCustomPlayer(target.getUniqueId()).getSoldi() != 0) {
                economy.withdrawPlayer(player, royalBounty.getPlayerManager().getCustomPlayer(target.getUniqueId()).getSoldi());
                royalBounty.getPlayerManager().getCustomPlayer(target.getUniqueId()).setSoldi(0);
                for ( Player ps : Bukkit.getOnlinePlayers() ) {
                    ps.sendMessage(Objects.requireNonNull(royalBounty.getConfig().getString("Bounty-Rewarded")));
                }
            }
        }
    }
}
