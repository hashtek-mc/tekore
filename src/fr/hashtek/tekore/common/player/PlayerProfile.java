package fr.hashtek.tekore.common.player;

import fr.hashtek.tekore.common.Rank;

/**
 * PlayerProfile aims to store everything related to the player's profile.
 */
public class PlayerProfile {
	
	Rank rank;
	// TODO: Add friends.
	
	
	/**
	 * Creates a new instance of PlayerProfile.
	 * 
	 * @param	rank	Player's rank
	 */
	PlayerProfile(Rank rank)
	{
		this.rank = rank;
	}
	
	/**
	 * Creates a new instance of PlayerProfile.
	 * All values will be the default ones.
	 */
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
