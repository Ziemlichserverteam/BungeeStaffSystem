package de.ziemlich.bungeeStaffSystem.accountsystem.db;

import de.ziemlich.bungeeStaffSystem.StaffSystem;
import de.ziemlich.bungeeStaffSystem.accountsystem.util.Account;
import de.ziemlich.bungeeStaffSystem.mySql.SQL;
import de.ziemlich.bungeeStaffSystem.utils.StaffSystemManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

public class AccountDAO {

    private static AccountDAO INSTANCE = new AccountDAO();

    public static AccountDAO getInstance() {
        return INSTANCE;
    }

    private static SQL sql = StaffSystemManager.ssm.getMainSQL();

    public void createAccount(Account account) {
        sql.executeUpdate("INSERT INTO accounts(UUID,Password,Salt,Rank,Grouppower) VALUES (?,?,?,?,?)", account.getUuid().toString(),account.getPassword(),account.getSalt(), account.getRank(),account.getGroupPower());
    }

    public void updateAccount(Account account) {
        sql.executeUpdate("UPDATE accounts SET Password = ?, Rank = ?, Grouppower = ? WHERE UUID = ?",account.getPassword(),account.getRank(),account.getGroupPower(),account.getUuid().toString());
    }
    public Account getAccount(UUID uuid) throws SQLException{
        ResultSet rs = sql.getResult("SELECT * FROM accounts where UUID = ?", Arrays.asList(uuid.toString()));
        if(rs.next()) {
            String rank = rs.getString("Rank");
            String password = rs.getString("Password");
            int groupPower = rs.getInt("Grouppower");
            byte[] salt = rs.getBytes("Salt");
            rs.close();
            return new Account(uuid,rank,password, StaffSystemManager.loggedIn.contains(StaffSystem.getInstance().getProxy().getPlayer(uuid)),groupPower,salt);
        }
        rs.close();
        return null;
    }

    public byte[] getSalt(UUID uuid) throws SQLException {
        ResultSet rs = sql.getResult("SELECT * FROM accounts where UUID = ?", Arrays.asList(uuid.toString()));
        if(rs.next()) {
            byte[] salt = rs.getBytes("Salt");
            rs.close();
            return salt;
        }
        rs.close();
        return null;
    }


    public void login(UUID uuid) {
        StaffSystemManager.loggedIn.add(StaffSystem.getInstance().getProxy().getPlayer(uuid));
    }

    public void logOut(UUID uuid) {
        StaffSystemManager.loggedIn.remove(StaffSystem.getInstance().getProxy().getPlayer(uuid));
    }

    public void setPassword(UUID uuid, String password) throws SQLException {
        Account account = getAccount(uuid);
        account.setPassword(password);
        updateAccount(account);
    }

    public void setRank(UUID uuid, String rank) throws SQLException {
        Account account = getAccount(uuid);
        account.setRank(rank);
        updateAccount(account);
    }

    public String getRank(UUID uuid) throws SQLException{
        return getAccount(uuid).getRank();
    }

    public boolean isLoggedIn(UUID uuid) {
        return StaffSystemManager.loggedIn.contains(StaffSystem.getInstance().getProxy().getPlayer(uuid));
    }

    public boolean doesPasswordMatch(UUID uuid, String password) throws SQLException {
        return password.equals(getAccount(uuid).getPassword());
    }

    public boolean hasAccount(UUID uuid) throws SQLException {
        return (getAccount(uuid) != null);
    }

    public void loadTableStaff() {
        sql.executeUpdate("CREATE TABLE IF NOT EXISTS accounts(UUID VARCHAR(36) PRIMARY KEY, Password VARCHAR(100), Salt VARCHAR(20), Rank VARCHAR(16), Grouppower INT(5))");
    }




}
