package me.bright.skylib.database;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.PluginLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Database {

    protected Connection connection;

    public void initialize() {
        connection = getSQLConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + getDbName());
            ResultSet rs = ps.executeQuery();
            close(ps, rs);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public abstract String getDbName();

    public abstract Connection getSQLConnection();

    public abstract void loadTable();

    public Connection getConnection() {
        return connection;
    }

    public Object getData(OfflinePlayer player, DataType datatype) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT " + datatype.getDataString() + " FROM " + getDbName() + " WHERE player = ?;");
            ps.setString(1, String.valueOf(player.getUniqueId()));
            rs = ps.executeQuery();

            if (!rs.next()) {
     //           insertPlayer(player);
                return 0;
            }

            return rs.getObject(datatype.getDataString());
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return 0;
    }

    public Object get(String query, String data) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getSQLConnection();
          //  Bukkit.getLogger().info(query + " query");
            ps = conn.prepareStatement(query.replace("*","'" + data + "'"));
         //  ps.setString(1,data);
            rs = ps.executeQuery();

            if (!rs.next()) {
                PluginLogger.getGlobal().info("Cannot get information from Mysql Db.");
                return 0;
            }
            return rs.getObject(data);
        } catch (SQLException ex) {
         //   Bukkit.getLogger().info(query + " query");
            ex.printStackTrace();

        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return 0;
    }

    public void execute(String query, Object... args) {
        try (Connection conn = getSQLConnection(); PreparedStatement stmt = conn.prepareStatement(
                query)) {
            int i = 1;
            for(Object arg: args){
                stmt.setObject(i,arg);
                i++;
            }
            stmt.execute();
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  // private void insertPlayer(OfflinePlayer player, Object defaultValue) {
  //     try (Connection conn = getSQLConnection(); PreparedStatement stmt = conn.prepareStatement(
  //             "INSERT INTO " + dbName + "(player, "+DataType.GAMES.getDataString()+", " + DataType.WINS.getDataString() + ") VALUES(?, ?, ?);")) {
  //         stmt.setString(1, player.getUniqueId().toString());
  //         stmt.setObject(2, defaultValue);
  //         stmt.setObject(3, defaultValue);
  //         stmt.execute();
  //     } catch (SQLException e) {
  //         e.printStackTrace();
  //     }
  // }


    public void updateData(OfflinePlayer player, String datatype, Object newValue) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "UPDATE " + getTableName() + " SET " + datatype + " = ? WHERE uuid = ?;";
     //   Bukkit.getLogger().info(query + " <- quer");
        try {

         //   Bukkit.getLogger().info(query);
          //  Bukkit.getLogger().info((String)newValue);
           // Bukkit.getLogger().info(player.getUniqueId().toString());
            ps = getSQLConnection().prepareStatement(query);
            ps.setObject(1, newValue);
            ps.setString(2, String.valueOf(player.getUniqueId()));
            ps.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public abstract String getTableName();


    public void close(PreparedStatement ps, ResultSet rs) {
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

