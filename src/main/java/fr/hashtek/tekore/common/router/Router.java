package fr.hashtek.tekore.common.router;

import com.google.common.io.ByteArrayDataInput;

public abstract class Router
{

    /**
     * <p>
     *     <u>Route:</u>
     *     {@code UpdateFriends}
     * </p>
     * <p>
     * <u>Arguments:</u>
     * <ul>
     *     <li>Target's name</li>
     * </ul>
     * </p>
     * <p>
     *     <u>Description:</u>
     *     Sends a friend update signal to a player's server.
     * </p>
     *
     * @param   in  Raw data input
     */
    protected abstract void updateFriends(ByteArrayDataInput in);

    /**
     * <p>
     *     <u>Route:</u>
     *     {@code UpdateParty}
     * </p>
     * <p>
     * <u>Arguments:</u>
     * <ul>
     *     <li>Target's tag (name or UUID)</li>
     * </ul>
     * </p>
     * <p>
     *     <u>Description:</u>
     *     Sends a party update signal to a player's server.
     * </p>
     *
     * @param   in  Raw data input
     */
    protected abstract void updateParty(ByteArrayDataInput in);

    /**
     * <p>
     *     <u>Route:</u>
     *     {@code UpdateAccount}
     * </p>
     * <p>
     * <u>Arguments:</u>
     * <ul>
     *     <li>Target's name</li>
     * </ul>
     * </p>
     * <p>
     *     <u>Description:</u>
     *     Sends a party update signal to a player's server.
     * </p>
     *
     * @param   in  Raw data input
     */
    protected abstract void updateAccount(ByteArrayDataInput in);


    /**
     * Dispatches the routes to custom functions.
     *
     * @param   subchannel  Sub-channel
     * @param   in          Raw data input
     */
    public abstract void dispatch(String subchannel, ByteArrayDataInput in);

}
