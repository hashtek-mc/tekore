package fr.hashtek.tekore.common.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.hashlogger.HashLogger;

public class SQLManager implements HashLoggable {
	
	private Connection connection;
	
	private HashLogger logger;
	
	private String database;
	private String host;
	private String user;
	private String password;
	
	
	public SQLManager(
		HashLogger logger,
		String database,
		String host,
		String user,
		String password
	)
	{
		this.logger = logger;
		this.database = database;
		this.host = host;
		this.user = user;
		this.password = password;
	}
	
	
	/**
	 * Returns true if plugin is connected to the database.
	 */
	private boolean isConnected()
	{
		return connection != null;
	}
	
	/**
	 * Connects to the database.
	 * 
	 * @throws	SQLException	SQL failure
	 */
	public void connect() throws SQLException
	{
		final String connectionString = String.format(
			"jdbc:mysql://%s/%s?autoReconnect=true",
			this.host, this.database);
		
		logger.info(this, "Connecting to the database...");
		
		if (this.isConnected()) {
			logger.warning(this, "Already connected to the database. Can't connect.");
			return;
		}
		
		try {
			this.connection = DriverManager.getConnection(
				connectionString, this.user, this.password);
			this.connection.setAutoCommit(false);
			logger.info(this, "Successfully connected to the database.");
		} catch (SQLException exception) {
			logger.fatal(this, "Failed to connect to the database.", exception);
			throw exception;
		}
	}
	
	/**
	 * Disconnects from the database.
	 * 
	 * @throws	SQLException	SQL failure
	 */
	public void disconnect() throws SQLException
	{
		logger.info(this, "Disconnecting from the database.");
		
		if (!isConnected()) {
			logger.warning(this, "Not connected to the database. Can't disconnect.");
			return;
		}
		
		try {
			connection.close();
		} catch (SQLException exception) {
			logger.error(this, "Failed to close database connection.", exception);
			throw exception;
		}
		
		logger.info(this, "Successfully disconnected from the database.");
	}
	
	
	public Connection getConnection()
	{
		return connection;
	}
	
}
