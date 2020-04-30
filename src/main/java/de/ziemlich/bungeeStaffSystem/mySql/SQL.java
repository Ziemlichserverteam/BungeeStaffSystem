package de.ziemlich.bungeeStaffSystem.mySql;

import de.ziemlich.bungeeStaffSystem.StaffSystem;
import net.md_5.bungee.api.ProxyServer;

import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class SQL {

    private String host;
    private String port;
    private String database;
    private String username;
    private String password;
    private Connection con;
    private static ExecutorService executor;

    public SQL(HashMap<String, String> config) {
        this.host 		= config.get("host");
        this.port 		= config.get("port");
        this.database 	= config.get("database");
        this.username 	= config.get("username");
        this.password 	= config.get("password");
    }

    public void connect() {
        if(!isConnected()) {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password);
                executor = Executors.newCachedThreadPool();
                System.out.println("[MySQL] " + database + "@" + host + ":" + port + " Verbindung hergestellt!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect() {
        if(isConnected()) {
            try {
                con.close();
                System.out.println("[MySQL] " + database + "@" + host + ":" + port + " Verbindung getrennt!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        return (con == null ? false : true);
    }

    public Connection getConnection() {
        try {
            if(isConnected()) {
                if(con.isValid(2)) {
                    return con;
                } else {
                    con.close();
                    System.out.println("[MySQL] " + database + "@" + host + ":" + port + " Verbindung getrennt!");
                    con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password);
                    System.out.println("[MySQL] " + database + "@" + host + ":" + port + " Verbindung wieder hergestellt!");
                    return con;
                }
            } else {
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password);
                System.out.println("[MySQL] " + database + "@" + host + ":" + port + " Verbindung wieder hergestellt!");
                return con;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public PreparedStatement getSatement(String statement) {
        try {
            return getConnection().prepareStatement(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void executeUpdate(String query, Object... obj) {
        try {
            PreparedStatement ps = getConnection().prepareStatement(query);
            for(int i = 0; i < obj.length; i++) {
                ps.setObject(i + 1, obj[i]);
            }
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean existResult(String table, String where, Object is) {
        ResultSet rs = getResult("SELECT " + where + " FROM " + table + " WHERE " + where + " = ?", Arrays.asList(is));
        try {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int countResult(String table, String where, Object is) {
        ResultSet rs = getResult("SELECT count(*) FROM " + table + " WHERE " + where + " = ?", Arrays.asList(is));
        try {
            if(rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ResultSet getResult(String query, List<Object> values) {
        try {
            PreparedStatement ps = getConnection().prepareStatement(query);
            if(values != null) {
                int stmts = values.size();
                for(int i = 1; i <= stmts; i++) {
                    Object obj = values.get(i-1);
                    if(obj instanceof String || obj instanceof UUID) {
                        ps.setString(i, obj.toString());
                    } else if(obj instanceof Integer) {
                        ps.setInt(i, Integer.parseInt(obj.toString()));
                    } else if(obj instanceof Long) {
                        ps.setLong(i, Long.parseLong(obj.toString()));
                    } else if(obj instanceof Boolean) {
                        ps.setInt(i, (Boolean.getBoolean(obj.toString()) ? 1 : 0));
                    } else {
                        ps.setObject(i, obj);
                    }
                }
            }
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    //Asyc
    public void executeUpdateAsync(String query, List<Object> values) {
        executor.execute(() -> executeUpdate(query, values));
    }

    public void getResultAsync(String query, List<Object> values, Consumer<ResultSet> consumer) {
        executor.execute(() -> {
            ResultSet result = getResult(query, values);
            ProxyServer.getInstance().getScheduler().runAsync(StaffSystem.getInstance(), () -> consumer.accept(result));
        });
    }

}
