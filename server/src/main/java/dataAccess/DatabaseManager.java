package dataAccess;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static final String databaseName;
    private static final String user;
    private static final String password;
    private static final String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) throw new Exception("Unable to laod db.properties");
                Properties props = new Properties();
                props.load(propStream);
                databaseName = props.getProperty("db.name");
                user = props.getProperty("db.user");
                password = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    public static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

        createTables();
    }

    /**
     * Creates all tables in the database
     * @throws DataAccessException when an SQL error occurs
     */
    private static void createTables() throws DataAccessException {
        String gameTable = "CREATE TABLE IF NOT EXISTS `game` (" +
                "  `game_id` int NOT NULL AUTO_INCREMENT," +
                "  `game_name` varchar(45) NOT NULL," +
                "  `white_username` varchar(45) DEFAULT NULL," +
                "  `black_username` varchar(45) DEFAULT NULL," +
                "  `game` mediumtext NOT NULL," +
                "  PRIMARY KEY (`game_id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";

        String userTable = "CREATE TABLE `user` (" +
                "  `username` varchar(45) NOT NULL," +
                "  `email` varchar(45) NOT NULL," +
                "  `password` varchar(60) NOT NULL," +
                "  PRIMARY KEY (`username`)," +
                "  UNIQUE KEY `email_UNIQUE` (`email`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";

        try (Connection connection = getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(gameTable)) {
                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
                throw new DataAccessException("Could not create table: game");
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(userTable)) {
                preparedStatement.executeUpdate();
            }
            catch (SQLException e) {
                throw new DataAccessException("Could not create table: user");
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("Could not open database");
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    public static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

}
