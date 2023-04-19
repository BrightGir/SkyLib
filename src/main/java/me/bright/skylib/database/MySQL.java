package me.bright.skylib.database;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class MySQL extends Database {

    private MysqlDataSource dataSource;

    private String createTableString =
            "CREATE TABLE IF NOT EXISTS player_bingo_info (" +
                    "`player` varchar(36) NOT NULL," +
                    "`wins` int(11) NOT NULL," +
                    "`games` int(11) NOT NULL," +
                    "PRIMARY KEY (`player`)" +
                    ");";

    public MySQL() {
       // this.createTableString = getCreateTableString();
    }

    public void loadDb() {
        init();
        if(this instanceof Creatable) {
            this.createTableString = ((Creatable)this).getCreateTableString();
            loadTable();
        }
    }

    private void init() {
        dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setServerName(getDbHost());
        dataSource.setPortNumber(getDbPort());
        dataSource.setDatabaseName(getDbName());
        dataSource.setUser(getDbUser());
        dataSource.setPassword(getDbPassword());
        connection = getSQLConnection();
    }

    public abstract String getDbHost();

    public abstract String getDbName();

    public abstract int getDbPort();

    public abstract String getDbUser();

    public abstract String getDbPassword();

//    public abstract String getCreateTableString();

    public Connection getSQLConnection()  {
        Connection conn = null;
        try  {
            conn = dataSource.getConnection();
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }


    public void loadTable() {
        connection = getSQLConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(createTableString);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        //initialize();
    }

}