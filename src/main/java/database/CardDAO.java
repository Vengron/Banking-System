package database;

import banking.Account;
import org.sqlite.SQLiteDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardDAO {
    private final SQLiteDataSource dataSource;
    private Connection con;

    public CardDAO(String fileName) {
        String url = "jdbc:sqlite:" + fileName;
        dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        try {
            con = dataSource.getConnection();
            if (con.isValid(5)) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        String table = "CREATE TABLE IF NOT EXISTS card ("
                + "id INTEGER, "
                + "number TEXT, "
                + "pin TEXT, "
                + "balance INTEGER DEFAULT 0);";
        execute(table);
    }

    public void addCard(String number, String pin) {
        String insert = String.format("INSERT INTO CARD (number, pin) " +
                "VALUES ('%s', '%s');", number, pin);
        execute(insert);

    }

    public Account selectAccount(String cardNumber) {
        String select = String.format("SELECT number, pin, balance FROM card " +
                "WHERE number = '%s';", cardNumber);
        Account account = null;
        try {
            con = dataSource.getConnection();
            if (con.isValid(5)) {
                ResultSet set = con.createStatement().executeQuery(select);
                if (set.next()) {
                    account = new Account(set.getString("number"),
                            set.getString("pin"),
                            set.getInt("balance"));
                }
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    public void updateBalance(String number, int income) {
        String update = String.format("UPDATE card SET balance = balance + %d WHERE number = '%s';",
                income, number);
        execute(update);
    }

    public void closeAccount(String number) {
        String update = String.format("DELETE FROM card WHERE number = '%s';", number);
        execute(update);
    }

    private void execute(String query) {
        try {
            con = dataSource.getConnection();
            if (con.isValid(5)) {
                con.createStatement().execute(query);
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
