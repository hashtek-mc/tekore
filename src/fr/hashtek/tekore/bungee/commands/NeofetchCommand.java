package fr.hashtek.tekore.bungee.commands;

import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.tekore.bungee.Tekord;
import fr.hashtek.tekore.common.Rank;
import fr.hashtek.tekore.common.player.PlayerData;
import fr.hashtek.tekore.common.sql.account.AccountManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class NeofetchCommand extends Command implements HashLoggable {

	private Tekord cord;
	private HashLogger logger;
	
	
	/**
	 * Creates a new instance of NeofetchCommand.
	 * Used to register the command.
	 */
	public NeofetchCommand()
	{
		super("neofetch");
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
			sender.sendMessage(new TextComponent("§cSyntaxe: /neofetch <joueur>"));
			return false;
		}
		
		if (args[0].length() > 16) {
			logger.info(this, "Invalid username.");
			sender.sendMessage(new TextComponent("§cUn pseudo ne peut pas contenir plus de 16 caractères."));
			return false;
		}
		
		return true;
	}
	
	/**
	 * Sends a full dump of the targeted player's data to the command sender.
	 * 
	 * @param	target				Targeted player
	 * @param	targetPlayerData	Targed player's data
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
			
		sender.sendMessage(new TextComponent(
			"\n" +
			(isConnected ? ChatColor.GREEN : ChatColor.RED) + "● " + nameField + targetServer + "\n" +
			ChatColor.WHITE + ChatColor.STRIKETHROUGH + separator + "\n" +
			ChatColor.DARK_AQUA + "UUID: " + ChatColor.WHITE + targetPlayerData.getUniqueId() + "\n" +
			ChatColor.DARK_AQUA + "First login: " + ChatColor.WHITE + targetPlayerData.getCreatedAt() + "\n" +
			ChatColor.DARK_AQUA + "Last seen: " + ChatColor.WHITE + targetPlayerData.getLastUpdate() +
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
		if (!(sender instanceof ProxiedPlayer))
			return;

		this.cord = Tekord.getInstance();
		this.logger = cord.getHashLogger();
		AccountManager accountManager = cord.getAccountManager();
		
		ProxiedPlayer player = (ProxiedPlayer) sender;
		
		logger.info(this, "Executed by " + player.getName() + " (" + player.getUniqueId() + ")");;
		
		if (!parseInput(player, args))
			return;
		
		ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[0]);
		PlayerData targetPlayerData = new PlayerData(args[0]);
		
		logger.info(this, "Targeted player: " + targetPlayerData.getUsername());
		
		try {
			accountManager.getFullPlayerAccount(targetPlayerData);
		} catch (Exception exception) {
			logger.info(this, targetPlayerData.getUsername() + " is not a valid player.");
			sender.sendMessage(new TextComponent(
				ChatColor.RED + targetPlayerData.getUsername() + " n'existe pas ou ne s'est jamais connecté à ce serveur."
			));
			return;
		}
		
		this.displayPlayerData(target, targetPlayerData, player);
		
		logger.info(this, "Command executed successfully.");
	}
	
}
