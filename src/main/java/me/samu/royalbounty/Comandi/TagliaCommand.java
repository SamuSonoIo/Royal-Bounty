package me.samu.royalbounty.Comandi;

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
            int quantita = Integer.parseInt(args[1]);
            if (quantita <= 0) {
                player.sendMessage(Objects.requireNonNull(royalBounty.getConfig().getString("Error-Invalid-Number")));
                return true;
            }

            if (economy.getBalance(player) < quantita) {
                player.sendMessage(Objects.requireNonNull(royalBounty.getConfig().getString("Not-Enough-Money")));
                return true;
            }

            // Aggiorna il saldo del giocatore e del target
            CustomPlayer customPlayer = new CustomPlayer(royalBounty, target.getUniqueId());
            customPlayer.setSoldi(quantita);
            economy.withdrawPlayer(player, quantita);

            // Messaggi di successo
            player.sendMessage(String.format(Objects.requireNonNull(royalBounty.getConfig().getString("Bounty-Set")).replace("{player}", target.getName())));

            for (Player ps : Bukkit.getOnlinePlayers()) {
                if (ps.hasPermission(permission)) {
                    ps.sendMessage(String.format(Objects.requireNonNull(royalBounty.getConfig().getString("Bounty-Broadcast").replace("{player}", target.getName()).replace("{money}", String.valueOf(quantita)))));
                }
            }

            return true;
        } catch (NumberFormatException e) {
            player.sendMessage(Objects.requireNonNull(royalBounty.getConfig().getString("Error-Invalid-Number")));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage("Errore durante l'aggiornamento del database.");
            return true;
        }
    }
}
