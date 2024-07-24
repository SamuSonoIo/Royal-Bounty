package me.samu.royalbounty;

import me.samu.royalbounty.Comandi.TagliaCommand;
import me.samu.royalbounty.Database.CustomPlayer;
import me.samu.royalbounty.Database.Database;
import me.samu.royalbounty.Database.PlayerManager;
import me.samu.royalbounty.Listener.ConnectionListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class RoyalBounty extends JavaPlugin {

    private Database database;
    private PlayerManager playerManager;
    private CustomPlayer customPlayer;

    // VAULT
    private static Economy econ = null;

    @Override
    public void onEnable() {
        // CONFIG
        getConfig();
        saveDefaultConfig();
        // DATABASE
        database = new Database(this);
        database.initDataConfig();
        try {
            database.connect();
        } catch (SQLException e) {
            Bukkit.getLogger().warning("ERROR WITH DATABASE SETTINGS!");
        }
        System.out.println("Database status: " + database.isConnected());
        playerManager = new PlayerManager();
        // LISTENER
        getServer().getPluginManager().registerEvents(new ConnectionListener(this), this);
        // VAULT
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        // COMANDI
        getCommand("taglia").setExecutor(new TagliaCommand(this));
    }

    @Override
    public void onDisable() {
        // DATABASE
        database.disconnect();
    }

    public Database getDatabase() { return database; }

    public PlayerManager getPlayerManager() { return playerManager; }

    // VAULT
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEcon() { return econ; }
}
