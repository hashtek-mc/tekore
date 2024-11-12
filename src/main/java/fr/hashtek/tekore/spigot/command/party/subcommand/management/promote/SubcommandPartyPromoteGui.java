package fr.hashtek.tekore.spigot.command.party.subcommand.management.promote;

import fr.hashtek.spigot.hashgui.HashGui;
import fr.hashtek.spigot.hashgui.handler.click.ClickHandler;
import fr.hashtek.spigot.hashgui.manager.HashGuiManager;
import fr.hashtek.spigot.hashgui.mask.Mask;
import fr.hashtek.spigot.hashitem.HashItem;
import fr.hashtek.spigot.hashitem.HashSkull;
import fr.hashtek.tekore.common.account.Account;
import fr.hashtek.tekore.common.account.io.AccountProvider;
import fr.hashtek.tekore.common.exception.EntryNotFoundException;
import fr.hashtek.tekore.common.party.Party;
import fr.hashtek.tekore.spigot.Tekore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

public class SubcommandPartyPromoteGui
    extends HashGui
{

    private static final Tekore CORE = Tekore.getInstance();
    private static final HashGuiManager GUI_MANAGER = CORE.getGuiManager();

    private static final Component GUI_NAME = Component.text("Select a new owner");


    public SubcommandPartyPromoteGui(Party party)
    {
        super(GUI_NAME, getGuiSizeFromPartySize(party));

        this.createGui(party);
    }


    private void createGui(Party party)
    {
        for (String memberUuid : party.getMembersUuid()) {
            if (memberUuid.equals(party.getOwnerUuid())) {
                continue;
            }

            try {
                final Account memberAccount = new AccountProvider(CORE.getRedisAccess())
                    .get(memberUuid);

                super.addItem(this.createMemberItem(memberUuid, memberAccount.getUsername()));
            }
            catch (EntryNotFoundException exception) {
                // TODO: wdo i do
            }
        }
    }


    private HashItem createMemberItem(String memberUuid, String memberName)
    {
        return new HashSkull()
            .setOwner(memberName, memberUuid)
            .setName(Component.text(memberName))
            .setLore(Arrays.asList(
                Component.text("Click 4 promoting !! ! !")
            ))
            .addClickHandler(
                new ClickHandler()
                    .addAllClickTypes()
                    .setClickAction((Player player, HashGui hashGui, ItemStack item, int slot) -> {
                        if (!(hashGui instanceof SubcommandPartyPromoteGui gui)) {
                            return;
                        }

                        if (item.getType() != Material.PLAYER_HEAD ||
                            !item.hasItemMeta() ||
                            !item.getItemMeta().hasDisplayName()
                        ) {
                            return;
                        }

                        final String targetName = ChatColor.stripColor(((TextComponent) item.getItemMeta().displayName()).content());

                        gui.close(player);
                        player.performCommand("party promote " + targetName);
                    })
            )
            .build(this, GUI_MANAGER);
    }

    /**
     * When closing the GUI, heads item's handlers are unregistered.
     * <p>
     * This is for better memory optimization.
     * </p>
     *
     * @param   player  Player
     */
    @Override
    public void onClose(Player player, HashGui gui)
    {
        Arrays.stream(gui.getInventory().getContents())
            .filter(item ->
                item != null &&
                item.getType() == Material.PLAYER_HEAD &&
                item.hasItemMeta() &&
                item.getItemMeta().hasDisplayName()
            )
            .forEach(item -> GUI_MANAGER.unregisterItem(item.getItemMeta().displayName()));
    }

    /**
     * @param   party   Party
     * @return  Size of GUI according to given party size
     */
    private static int getGuiSizeFromPartySize(Party party)
    {
        /*                                     ↓ Omitting party owner */
        return ((party.getMembersUuid().size() - 1) / 9) + 1; // ← + 1 because x / 9 with x < 9 equals to 0.
        /*                                            ^ Gui line size (1 line is 9 slots) */
    }

}
