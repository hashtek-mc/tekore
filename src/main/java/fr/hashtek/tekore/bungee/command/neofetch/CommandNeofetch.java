package fr.hashtek.tekore.bungee.command.neofetch;

import com.google.common.collect.ImmutableSet;
import fr.hashtek.hashdate.HashDate;
import fr.hashtek.hashdate.HashDateType;
import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.tekore.bungee.Tekord;
import fr.hashtek.tekore.common.Rank;
import fr.hashtek.tekore.common.player.PlayerData;
import fr.hashtek.tekore.common.player.PlayerManager;
import fr.hashtek.tekore.common.sql.account.AccountManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.HashSet;
import java.util.Set;

public class CommandNeofetch extends Command implements HashLoggable, TabExecutor
{

	private final Tekord cord;
    private final HashLogger logger;
	
	
	/**
	 * Creates a new instance of CommandNeofetch.
	 * Used to register the command.
	 */
	public CommandNeofetch(Tekord cord)
	{
		super("neofetch");
		this.cord = cord;
		this.logger = this.cord.getHashLogger();
	}
	
	
	/**
	 * Command parsing.
	 * 
	 * @param	sender	Player who executed the command
	 * @param	args	Arguments passed
	 */
	private boolean parseInput(ProxiedPlayer sender, String[] args)
	{
		if (args.length != 1) {
			logger.info(this, "Invalid usage.");
			sender.sendMessage(new TextComponent(ChatColor.RED + "Syntaxe: /neofetch <joueur>"));
			return false;
		}
		
		if (args[0].length() > 16) {
			logger.info(this, "Invalid username.");
			sender.sendMessage(new TextComponent(ChatColor.RED + "Un pseudo ne peut pas contenir plus de 16 caractères."));
			return false;
		}
		
		return true;
	}
	
	/**
	 * Sends a full dump of the targeted player's data to the command sender.
	 * 
	 * @param	target				Targeted player
	 * @param	targetPlayerData	Targeted player's data
	 * @param	sender				Player who executed the command
	 */
	private void displayPlayerData(ProxiedPlayer target, PlayerData targetPlayerData, ProxiedPlayer sender)
	{
		boolean isConnected = target != null && target.isConnected();

		String targetServer = "";
		if (isConnected) {
			ServerInfo server = target.getServer().getInfo();
			targetServer = ChatColor.GRAY + " " + ChatColor.ITALIC + "(" + server.getName() + ")";
		}
		
		Rank targetRank = targetPlayerData.getRank();
		
		String nameField = targetRank.getColor() + targetPlayerData.getUsername() + "@" + targetRank.getName();
		
		StringBuilder separator = new StringBuilder();
		for (int k = 0; k < nameField.length() + targetServer.length(); k++)
			separator.append("-");

		String targetPing = "";
		if (isConnected)
			targetPing = ChatColor.DARK_AQUA + "Ping: " + ChatColor.WHITE + target.getPing() + " ms";

		sender.sendMessage(new TextComponent(
			"\n" +
			(isConnected ? ChatColor.GREEN : ChatColor.RED) + "● " + nameField + targetServer + "\n" +
			ChatColor.WHITE + ChatColor.STRIKETHROUGH + separator + "\n" +
			ChatColor.DARK_AQUA + "UUID: " + ChatColor.WHITE + targetPlayerData.getUniqueId() + "\n" +
			ChatColor.DARK_AQUA + "First login: " + ChatColor.WHITE + HashDate.format(HashDateType.FANCY_DATE_TIME, targetPlayerData.getCreatedAt()) + "\n" +
			ChatColor.DARK_AQUA + "Last seen: " + ChatColor.WHITE + HashDate.format(HashDateType.FANCY_DATE_TIME, targetPlayerData.getLastUpdate()) + "\n" +
			targetPing +
			"\n"
		));
	}
	
	/**
	 * Called when command is executed.
	 * 
	 * @param	sender	Player who executed the command
	 * @param	args	Arguments passed
	 */
	@Override
	public void execute(CommandSender sender, String[] args)
	{
		if (!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage(new TextComponent("You must be a player to execute this command."));
			return;
		}

		final AccountManager accountManager = cord.getAccountManager();
		
		final ProxiedPlayer player = (ProxiedPlayer) sender;

		logger.info(this, "Executed by " + player.getName() + " (" + player.getUniqueId() + ")");
		
		if (!parseInput(player, args))
			return;
		
		final ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[0]);
		final PlayerManager targetPlayerManager = new PlayerManager(args[0]);
		final PlayerData targetPlayerData = targetPlayerManager.getData();

		try {
			accountManager.getFullPlayerAccount(targetPlayerManager);
		} catch (Exception exception) {
			logger.error(this, targetPlayerData.getUsername() + " is not a valid player.", exception);
			sender.sendMessage(new TextComponent(ChatColor.RED + targetPlayerData.getUsername() + " n'existe pas ou ne s'est jamais connecté à ce serveur."));
			return;
		}
		
		this.displayPlayerData(target, targetPlayerData, player);
		
		logger.info(this, "Command executed successfully.");
	}

	/**
	 * Called when player presses the <TAB> key.
	 *
	 * @param	sender	Player who executed the command
	 * @param	args	Arguments passed
	 * @return	List of predictions
	 */
	@Override
	public Iterable<String> onTabComplete(CommandSender sender, String[] args)
	{
		if (args.length != 1)
			return ImmutableSet.of();

		final Set<String> matches = new HashSet<String>();
		final String search = args[0].toLowerCase();

		for (ProxiedPlayer player : this.cord.getProxy().getPlayers())
			if (player.getName().toLowerCase().startsWith(search))
				matches.add(player.getName());

		return matches;
	}
	
}
