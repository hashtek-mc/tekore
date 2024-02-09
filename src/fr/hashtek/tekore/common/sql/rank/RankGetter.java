package fr.hashtek.tekore.common.sql.rank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.hashtek.tekore.common.Rank;

public class RankGetter {

	private Connection sqlConnection;
	
	
	/**
	 * Creates a new instance of RankGetter.
	 * 
	 * @param	sqlConnection	SQL connection
	 */
	public RankGetter(Connection sqlConnection)
	{
		this.sqlConnection = sqlConnection;
	}
	
	
	/**
	 * Creates a Rank according to a ResultSet results.
	 * 
	 * @param	resultSet		ResultSet results
	 * @param	prefix			ResultSet prefix (if statement has "JOIN"s)
	 * @return	Fetched	rank
	 * @throws	SQLException	SQL failure
	 */
	public Rank getRankFromResultSet(ResultSet resultSet, String prefix) throws SQLException
	{
		return new Rank(
			resultSet.getString(prefix + "uuid"),
			resultSet.getString(prefix + "name"),
			resultSet.getInt(prefix + "power"),
			resultSet.getString(prefix + "chatPrefix"),
			resultSet.getString(prefix + "tabPrefix")
		);
	}
	
	/**
	 * Creates a Rank according to a ResultSet results (without prefixes).
	 * 
	 * @param	resultSet		ResultSet results
	 * @return	Fetched	rank
	 * @throws	SQLException	SQL failure
	 */
	public Rank getRankFromResultSet(ResultSet resultSet) throws SQLException
	{
		return this.getRankFromResultSet(resultSet, "");
	}
	
	/**
	 * Gets a rank from the SQL database.
	 * 
	 * @param	type					Type of rank fetching (name or uuid)
	 * @param	typeValue				Type value
	 * @return	Fetched rank
	 * @throws	SQLException			SQL failure
	 * @throws	NoSuchFieldException	No rank found
	 */
	private Rank getRank(String type, String typeValue) throws SQLException, NoSuchFieldException
	{
		Rank rank = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query = "SELECT * FROM ranks WHERE " + type + " = ?;";
		
		statement = this.sqlConnection.prepareStatement(query);
		statement.setString(1, typeValue);
		resultSet = statement.executeQuery();
		
		this.sqlConnection.commit();
		
		if (!resultSet.next())
			throw new NoSuchFieldException("Could not find a rank with " + type + " \"" + typeValue + "\".");
		
		rank = getRankFromResultSet(resultSet);
		
		if (resultSet != null)
			resultSet.close();
		
		if (statement != null)
			statement.close();
		
		return rank;
	}
	
	/**
	 * Gets a rank from the SQL database by its UUID.
	 * 
	 * @param	uuid					Rank's UUID
	 * @return	Fetched rank
	 * @throws	SQLException			SQL failure
	 * @throws	NoSuchFieldException	No rank found
	 */
	public Rank getRankByUuid(String uuid) throws SQLException, NoSuchFieldException
	{
		return this.getRank("uuid", uuid);
	}
	
	/**
	 * Gets a rank from the SQL database by its name.
	 * 
	 * @param	uuid					Rank's name
	 * @return	Fetched rank
	 * @throws	SQLException			SQL failure
	 * @throws	NoSuchFieldException	No rank found
	 */
	public Rank getRankByName(String name) throws SQLException, NoSuchFieldException
	{
		return this.getRank("name", name);
	}
	
}
