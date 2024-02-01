package fr.hashtek.tekore.common.player;

import fr.hashtek.tekore.common.Rank;

public class PlayerProfile {
	
	Rank rank;
	// TODO: Add friends.
	
	PlayerProfile(Rank rank)
	{
		this.rank = rank;
	}
	
	PlayerProfile()
	{
		this.rank = Rank.PLAYER;
	}
	
	public Rank getRank()
	{
		return this.rank;
	}
	
	public void setRank(Rank rank)
	{
		this.rank = rank;
	}

}
