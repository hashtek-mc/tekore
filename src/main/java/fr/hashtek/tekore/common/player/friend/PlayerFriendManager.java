package fr.hashtek.tekore.common.player.friend;

import java.util.ArrayList;
import java.util.List;

public class PlayerFriendManager
{

    private List<PlayerFriendLink> friendLinks;


    public void addFriendLink(PlayerFriendLink friendLink)
    {
        if (this.friendLinks == null)
            this.friendLinks = new ArrayList<PlayerFriendLink>();
        this.friendLinks.add(friendLink);
    }

    public void removeFriendLink(PlayerFriendLink friendLink)
    {
        this.friendLinks.remove(friendLink);
    }

    public PlayerFriendLink getFriendLink(String uuid)
    {
        for (PlayerFriendLink link : friendLinks)
            if (link.getUuid().equals(uuid))
                return link;
        return null;
    }

    public List<PlayerFriendLink> getFriendLinks()
    {
        return this.friendLinks;
    }

}
