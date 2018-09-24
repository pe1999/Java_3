// Java 3
// Lesson 2
// Homework
// Автор: Евгений Пермяков

//------------------------------------ public interface AuthHandler
package ru.geekbrains;

public interface AuthHandler {
    void start();
    String getNickByLoginPass(String login, String pass);
    boolean changeNick(String login, String newNick);
    void stop();
}
//------------------------------------public class DBAuthHandler
package ru.geekbrains;

import java.sql.*;

public class DBAuthHandler implements AuthHandler{
    private static Connection connection;
    private static PreparedStatement psSelect;
    private static PreparedStatement psSelectNick;
    private static PreparedStatement psUpdate;


    public DBAuthHandler() {
    }

    @Override
    public void start() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:chatusers.db");
            psSelect = connection.prepareStatement("SELECT * FROM users WHERE login = ? AND password = ?;");
            psSelectNick = connection.prepareStatement("SELECT * FROM users WHERE nickname = ?;");
            psUpdate = connection.prepareStatement("UPDATE users SET nickname = ? WHERE login = ?;");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getNickByLoginPass(String login, String pass) {
        try {
            psSelect.setString(1, login);
            psSelect.setString(2, pass);
            ResultSet rs = psSelect.executeQuery();
            if(rs.next()) {
                return rs.getString("nickname");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean changeNick(String login, String newNick) {
        try {
            psSelectNick.setString(1, newNick);
            ResultSet rs = psSelectNick.executeQuery();
            if(rs.next()) {
                return false;
            }

            psUpdate.setString(1, newNick);
            psUpdate.setString(2, login);
            psUpdate.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void stop() {
        try {
            psSelect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            psUpdate.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
//------------------------------------ public class ClientHandler
// ...
    private String login;
// ...
                        this.login = tokens[1];
// ...
                        if(msg.startsWith("/changenick ")) {
                            // * HomeWork here
                            String[] tokens = msg.split(" ");
                            if(tokens.length == 2) {
                                if(server.getAuthHandler().changeNick(this.login, tokens[1])) {
                                    server.broadcastMsg(this, "Пользователь " + this.nick + " сменил ник на " + tokens[1] + ".");
                                    this.nick = tokens[1];
                                    server.broadcastClientsList();
                                } else sendMessage("Ник уже используется.");
                            } else {
                                sendMessage("Неправильный формат команды /changenick.");
                            }
                        }
//------------------------------------ End
