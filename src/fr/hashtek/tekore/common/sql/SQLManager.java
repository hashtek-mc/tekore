package fr.hashtek.tekore.common.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.hashlogger.HashLogger;

public class SQLManager implements HashLoggable {

	private final HashLogger logger;
	
	private Connection connection;
	
	private final String database;
	private final String host;
	private final String user;
	private final String password;
	
	
	/**
	 * Creates a new instance of SQLManager.
	 * 
	 * @param	logger		Logger
	 * @param	database	Database name
	 * @param	host		Host
	 * @param	user		Username
	 * @param	password	Password
	 */
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
	 * Returns true if instance is connected to the database.
	 */
	private boolean isConnected()
	{
		return this.connection != null;
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
			this.host, this.database
		);
		
		this.logger.info(this, "Connecting to the database...");
		
		if (this.isConnected()) {
			this.logger.warning(this, "Already connected to the database. Can't connect.");
			return;
		}
		
		try {
			this.connection = DriverManager.getConnection(connectionString, this.user, this.password);
			this.connection.setAutoCommit(false);	
		} catch (SQLException exception) {
			this.logger.fatal(this, "Failed to connect to the database.", exception);
			throw exception;
		}

		this.logger.info(this, "Successfully connected to the database.");
	}
	
	/**
	 * Disconnects from the database.
	 * 
	 * @throws	SQLException	SQL failure
	 */
	public void disconnect() throws SQLException
	{
		this.logger.info(this, "Disconnecting from the database...");
		
		if (!isConnected()) {
			this.logger.warning(this, "Not connected to the database. Can't disconnect.");
			return;
		}
		
		try {
			this.connection.close();
		} catch (SQLException exception) {
			this.logger.error(this, "Failed to close database connection.", exception);
			throw exception;
		}
		
		this.logger.info(this, "Successfully disconnected from the database.");
	}
	
	
	/**
	 * Returns the active connection to the SQL database.
	 * 
	 * @return	SQL connection
	 */
	public Connection getConnection()
	{
		return this.connection;
	}
	
}
