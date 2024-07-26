package me.samu.royalbounty.Database;

import me.samu.royalbounty.RoyalBounty;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CustomPlayer {

    private UUID uuid;

    private double soldi;

    private RoyalBounty royalBounty;

    public CustomPlayer(RoyalBounty royalBounty, UUID uuid) throws SQLException{
        this.royalBounty = royalBounty;
        this.uuid = uuid;
        PreparedStatement statement = royalBounty.getDatabase().getConnection().prepareStatement("SELECT SOLDI FROM players WHERE UUID = ?;");
        statement.setString(1, uuid.toString());
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            soldi = rs.getDouble("SOLDI");
        } else {
            soldi = 0;
            PreparedStatement statement1 = royalBounty.getDatabase().getConnection().prepareStatement("INSERT INTO players (ID, UUID, SOLDI) VALUES(" +
                    "default," +
                    "'" + uuid + "'," +
                    soldi + ");"
            );
            statement1.executeUpdate();
        }
    }

    public void setSoldi(double soldi) {
        this.soldi = soldi;
        try {
            PreparedStatement statement = royalBounty.getDatabase().getConnection().prepareStatement("UPDATE players SET SOLDI = " + soldi + " WHERE UUID = '" + uuid + "';");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getSoldi() { return soldi; }

}
