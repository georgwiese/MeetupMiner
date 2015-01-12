package de.hpi.smm.meetup_miner.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.hpi.smm.meetup_miner.config.Secrets;

public class DatabaseConnector {

    private static ArrayList<Connection> connections;
    public static DbConnectionParameters connectionParameters;

    public static class DbConnectionParameters {
        public String host = Secrets.HANA_IP_VPN;
        public int port = Secrets.HANA_PORT;
        public String dbName = "";
        public String userName = Secrets.HANA_USER;
        public String password = Secrets.HANA_PASSWORD;
        public String defaultSchema = "Meetup";
        
        public DbConnectionParameters() {        	
        }

        public DbConnectionParameters(String host, int port, String dbName,
                String userName, String password, String defaultSchema) {
            this.host = host;
            this.port = port;
            this.dbName = dbName;
            this.userName = userName;
            this.password = password;
            this.defaultSchema = defaultSchema;
        }

        public DbConnectionParameters(String host, int port, String dbName,
                String userName, String password) {
            this.host = host;
            this.port = port;
            this.dbName = dbName;
            this.userName = userName;
            this.password = password;
        }

        public DbConnectionParameters(String host, String dbName,
                String password, String defaultSchema) {
            this.host = host;
            this.dbName = dbName;
            this.password = password;
            this.defaultSchema = defaultSchema;
        }

        public DbConnectionParameters(String host, String dbName,
                String password) {
            this.host = host;
            this.dbName = dbName;
            this.password = password;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getDbName() {
            return dbName;
        }

        public void setDbName(String dbName) {
            this.dbName = dbName;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDefaultSchema() {
            return defaultSchema;
        }

        public void setDefaultSchema(String defaultSchema) {
            this.defaultSchema = defaultSchema;
        }
    }

    public static void setup(DbConnectionParameters connectionParameters) {
        DatabaseConnector.connectionParameters = connectionParameters;
        DatabaseConnector.connections = new ArrayList<>();
    }

    public static void setup() {
    	setup(new DbConnectionParameters());
    }
    
    public static Connection getNewConnection() throws ClassNotFoundException, SQLException {    	
        if (connectionParameters == null) {
            throw new IllegalStateException("Can't create new connection, have no connection parameters!");
        }
        Class.forName("com.sap.db.jdbc.Driver");
        String url;
        url = "jdbc:sap://"+ connectionParameters.host + ":" + connectionParameters.port  + "/"
                + connectionParameters.dbName + ";";

        Connection connection = DriverManager.getConnection(url, connectionParameters.userName,
                connectionParameters.password);
        if (connection != null) {
            connections.add(connection);
        }

        Statement schemaStmt = connection.createStatement();
        schemaStmt.execute("SET SCHEMA " + connectionParameters.defaultSchema);
        
        return connection;
    }

    public static void closeConnection(Connection connection) throws SQLException {
        connections.remove(connection);
        connection.close();
    }


}