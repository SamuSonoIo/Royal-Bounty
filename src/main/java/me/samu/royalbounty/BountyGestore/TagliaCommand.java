package me.samu.royalbounty.BountyGestore;

import me.samu.royalbounty.Database.CustomPlayer;
import me.samu.royalbounty.RoyalBounty;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Objects;

public class TagliaCommand implements CommandExecutor {

    private final RoyalBounty royalBounty;
    private final Economy economy;

    public TagliaCommand(RoyalBounty royalBounty) {
        this.royalBounty = royalBounty;
        this.economy = RoyalBounty.getEcon();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only Players can execute this command.");
            return false;
        }

        Player player = (Player) commandSender;

        // Controllo dei permessi
        String permission = royalBounty.getConfig().getString("Permission-Name");
        if (!player.hasPermission(Objects.requireNonNull(permission))) {
            player.sendMessage(Objects.requireNonNull(royalBounty.getConfig().getString("No-Permission")));
            return true;
        }

        // Controlli argomenti
        if (args.length != 2) {
            player.sendMessage(Objects.requireNonNull(royalBounty.getConfig().getString("Error-Usage")));
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            player.sendMessage(Objects.requireNonNull(royalBounty.getConfig().getString("Error-Target-Not-Found")));
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage(Objects.requireNonNull(royalBounty.getConfig().getString("Cant-Bounty-Yourself")));
            return true;
        }


        // Gestione importo
        try {
            double quantita = Double.parseDouble(args[1]);
            if (quantita <= 0) {
                player.sendMessage(Objects.requireNonNull(royalBounty.getConfig().getString("Error-Invalid-Number")));
                return true;
            }

            if (royalBounty.getPlayerManager().getCustomPlayer(target.getUniqueId()).getSoldi() > 0) {
                String message = Objects.requireNonNull(royalBounty.getConfig().getString("Bounty-Updated-Broadcast"))
                        .replace("{money}", String.valueOf(quantita))
                        .replace("{player}", target.getName())
                        .replace("{allmoney}", String.valueOf(quantita + royalBounty.getPlayerManager().getCustomPlayer(target.getUniqueId()).getSoldi()));

                Bukkit.broadcastMessage(message);
                royalBounty.getPlayerManager().getCustomPlayer(target.getUniqueId()).setSoldi(
                        royalBounty.getPlayerManager().getCustomPlayer(target.getUniqueId()).getSoldi() + quantita
                );
            } else {
                String message = Objects.requireNonNull(royalBounty.getConfig().getString("Bounty-Broadcast"))
                        .replace("{money}", String.valueOf(quantita + royalBounty.getPlayerManager().getCustomPlayer(target.getUniqueId()).getSoldi()))
                        .replace("{player}", target.getName());

                Bukkit.broadcastMessage(message);
                royalBounty.getPlayerManager().getCustomPlayer(target.getUniqueId()).setSoldi(quantita);
            }


            if (economy.getBalance(player) < quantita) {
                player.sendMessage(Objects.requireNonNull(royalBounty.getConfig().getString("Not-Enough-Money")));
                return true;
            }


            // Aggiorna il saldo del target e il saldo del giocatore
            economy.withdrawPlayer(player, quantita);

            // Messaggi di successo
            player.sendMessage(String.format(Objects.requireNonNull(royalBounty.getConfig().getString("Bounty-Set")).replace("{player}", target.getName()).replace("{money}", String.valueOf(royalBounty.getPlayerManager().getCustomPlayer(target.getUniqueId()).getSoldi()))));

            return true;
        } catch (NumberFormatException e) {
            player.sendMessage(Objects.requireNonNull(royalBounty.getConfig().getString("Error-Invalid-Number")));
            return true;
        }
    }
}
