package fr.hashtek.tekore.common;

public enum Rank {
	
	// TODO: Change power values (cf. @hopecalypse).
	
	PLAYER	(0,		"player",	"Joueur",			"Joueur",	"§7"),
	BUILDER	(50,	"builder",	"Builder",			"Builder",	"§2"),
	DEV		(80,	"dev",		"Développeur",		"Dev.",		"§3"),
	MOD		(90,	"mod",		"Modérateur",		"Mod.",		"§c"),
	ADMIN	(100,	"admin",	"Administateur",	"Admin.",	"§c");
	
	
	private int power;
	private String databaseName;
	private String chatName;
	private String tablistName;
	private String color;
	
	
	Rank(
		int power,
		String databaseName,
		String chatName,
		String tablistName,
		String color
	)
	{
		this.power = power;
		this.databaseName = databaseName;
		this.chatName = chatName;
		this.tablistName = tablistName;
		this.color = color;
	}
	
	
	/**
	 * From a raw string (which equals to a rank's database name), return a Rank (if it does exists).
	 * 
	 * @param databaseName	Rank's database name
	 */
	public static Rank getRankByDatabaseName(String databaseName)
	{
		for (Rank rank: Rank.values())
			if (rank.getDatabaseName().equals(databaseName))
				return rank;
		return null;
	}
	
	
	public int getPower()
	{
		return this.power;
	}
	
	public String getDatabaseName()
	{
		return this.databaseName;
	}
	
	public String getChatName()
	{
		return this.chatName;
	}
	
	public String getTablistName()
	{
		return this.tablistName;
	}
	
	public String getColor()
	{
		return this.color;
	}

}
