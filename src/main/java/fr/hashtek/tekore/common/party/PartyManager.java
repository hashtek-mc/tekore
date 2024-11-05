package fr.hashtek.tekore.common.party;

import fr.hashtek.tekore.common.exception.EntryNotFoundException;
import fr.hashtek.tekore.common.party.io.PartyProvider;
import fr.hashtek.tekore.spigot.Tekore;

public class PartyManager
{

    private Party currentParty;


    public PartyManager(String partyUuid)
    {
        this.currentParty = new Party(partyUuid);
    }


    public PartyManager updateParty()
    {
        if (this.currentParty != null) {
            this.updateParty(this.currentParty.getUuid());
        }
        return this;
    }

    public PartyManager updateParty(String partyUuid)
    {
        try {
            this.currentParty = new PartyProvider(Tekore.getInstance().getRedisAccess())
                .get(partyUuid);
        }
        catch (EntryNotFoundException exception) {
            this.currentParty = null;
        }
        return this;
    }


    public Party getCurrentParty()
    {
        return this.currentParty;
    }

    public Party setCurrentParty(Party party)
    {
        this.currentParty = party;
        return this.currentParty;
    }

}