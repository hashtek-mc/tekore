package fr.hashtek.tekore.common;

public class Rank
{
	
	private final String uuid;
	private final String name;
	private final int power;
	private final String fullName;
	private final String shortName;
	

	/**
	 * Creates a new instance of Rank.
	 * 
	 * @param	uuid		Rank's UUID
	 * @param	name		Rank's raw name
	 * @param	power		Rank's power
	 * @param	fullName	Prefix used for chat
	 * @param	shortName	Prefix used only for tablist
	 */
	public Rank(
		String uuid,
		String name,
		int power,
		String fullName,
		String shortName
	)
	{
		this.uuid = uuid;
		this.name = name;
		this.power = power;
		this.fullName = fullName;
		this.shortName = shortName;
	}
	
	
	/**
	 * @return	Rank UUID
	 */
	public String getUuid()
	{
		return this.uuid;
	}
	
	/**
	 * @return	Rank's power
	 */
	public int getPower()
	{
		return this.power;
	}
	
	/**
	 * @return	Rank's raw name
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * @return	Rank's full name
	 */
	public String getFullName()
	{
		return this.fullName;
	}
	
	/**
	 * @return	Rank's short name
	 */
	public String getShortName()
	{
		return this.shortName;
	}
	
	/**
	 * @return	Rank's color (based on its prefix)
	 */
	public String getColor()
	{
		if (this.fullName.charAt(0) != '§')
			return "";

		return this.fullName.substring(0, 2);
	}

}
