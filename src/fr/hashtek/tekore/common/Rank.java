package fr.hashtek.tekore.common;

public class Rank {
	
	private String uuid;
	private String name;
	private int power;
	private String chatPrefix;
	private String tabPrefix;
	

	/**
	 * Creates a new instance of Rank.
	 * 
	 * @param	uuid		Rank's UUID
	 * @param	name		Rank's raw name
	 * @param	power		Rank's power
	 * @param	chatPrefix	Prefix used for chat
	 * @param	tabPrefix	Prefix used only for tablist
	 */
	public Rank(
		String uuid,
		String name,
		int power,
		String chatPrefix,
		String tabPrefix
	)
	{
		this.uuid = uuid;
		this.name = name;
		this.power = power;
		this.chatPrefix = chatPrefix;
		this.tabPrefix = tabPrefix;
	}
	
	
	/**
	 * Returns rank's UUID.
	 * 
	 * @return	Rank UUID
	 */
	public String getUuid()
	{
		return this.uuid;
	}
	
	/**
	 * Returns rank's power.
	 * 
	 * @return	Rank's power
	 */
	public int getPower()
	{
		return this.power;
	}
	
	/**
	 * Returns rank's raw name.
	 * 
	 * @return	Rank's raw name
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Returns rank's chat prefix.
	 * 
	 * @return	Rank's chat prefix
	 */
	public String getChatPrefix()
	{
		return this.chatPrefix;
	}
	
	/**
	 * Returns rank's tablist prefix.
	 * 
	 * @return	Rank's tablist prefix
	 */
	public String getTabPrefix()
	{
		return this.tabPrefix;
	}
	
	/**
	 * Returns rank's color based on its prefix.
	 * 
	 * @return	Rank's color
	 */
	public String getColor()
	{
		if (this.chatPrefix.charAt(0) != '§')
			return "";
		return this.chatPrefix.substring(0, 2);
	}

}
