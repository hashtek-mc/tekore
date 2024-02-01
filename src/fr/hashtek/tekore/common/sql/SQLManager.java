package fr.hashtek.tekore.common.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.tekore.common.Exit;

public class SQLManager {
	
	private static final String FILENAME = "SQLManager.java";

	private Connection connection;
	
	private String database;
	private String host;
	private String user;
	private String password;
	
	public SQLManager(
		String database,
		String host,
		String user,
		String password
	)
	{
		this.database = database;
		this.host = host;
		this.user = user;
		this.password = password;
	}
	
	private boolean isConnected()
	{
		return connection != null;
	}
	
	public Exit connect()
	{
		final String connectionString = String.format(
			"jdbc:mysql://%s/%s?autoReconnect=true",
			this.host, this.database);
		
		if (this.isConnected()) {
			HashLogger.err(FILENAME, "Already connected to the database.");
			return Exit.FAILURE;
		}
		
		try {
			this.connection = DriverManager.getConnection(
				connectionString, this.user, this.password);
			this.connection.setAutoCommit(false);
			HashLogger.info("Successfully connected to database.");
		} catch (SQLException exception) {
			HashLogger.err(FILENAME, "Failed to connect to database.", exception);
			return Exit.FAILURE;
		}
		return Exit.SUCCESS;
	}
	
	public Exit disconnect()
	{
		if (!isConnected()) {
			HashLogger.err(FILENAME, "Not connected to the database.");
			return Exit.FAILURE;
		}
		
		try {
			connection.close();
		} catch (SQLException exception) {
			HashLogger.err(FILENAME, "Failed to close database connection.", exception);
			return Exit.FAILURE;
		}
		
		HashLogger.info("Successfully disconnected from the database.");
		
		return Exit.SUCCESS;
	}
	
	public Connection getConnection()
	{
		return connection;
	}
	
}
