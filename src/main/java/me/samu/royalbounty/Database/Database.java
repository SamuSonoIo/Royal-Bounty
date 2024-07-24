package me.samu.royalbounty.Database;

import me.samu.royalbounty.RoyalBounty;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class Database {

    private RoyalBounty royalBounty;

    public Database(RoyalBounty royalBounty) { this.royalBounty = royalBounty; }

    private int HOST;
    private int PORT;
    private String DATABASE;
    private String USERNAME;
    private String PASSWORD;

    private Connection connection;

    public void initDataConfig() {
        this.HOST = royalBounty.getConfig().getInt("Database" + ".HOST");
        this.PORT = royalBounty.getConfig().getInt("Database" + ".PORT");
        this.DATABASE = royalBounty.getConfig().getString("Database" + ".DATABASE");
        this.USERNAME = royalBounty.getConfig().getString("Database" + ".USERNAME");
        this.PASSWORD = royalBounty.getConfig().getString("Database" + ".PASSWORD");

        royalBounty.getLogger().log(Level.INFO, "Database Configuration: ");
        royalBounty.getLogger().log(Level.INFO, "HOST: " + HOST);
        royalBounty.getLogger().log(Level.INFO, "PORT: " + PORT);
        royalBounty.getLogger().log(Level.INFO, "DATABASE: " + DATABASE);
        royalBounty.getLogger().log(Level.INFO, "USERNAME: " + USERNAME);
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?useSSL=false",
                USERNAME,
                PASSWORD
        );
    }

    public boolean isConnected() { return connection != null; }

    public Connection getConnection() {
        return connection;
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
