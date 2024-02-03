package fr.hashtek.tekore.bungee.commands;

import fr.hashtek.hashlogger.HashLoggable;
import fr.hashtek.hashlogger.HashLogger;
import fr.hashtek.tekore.bungee.Tekord;
import fr.hashtek.tekore.common.Rank;
import fr.hashtek.tekore.common.player.PlayerData;
import fr.hashtek.tekore.common.sql.SQLManager;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CheckCommand extends Command implements HashLoggable {

	private Tekord tekord;
	private HashLogger logger;
	
	
	public CheckCommand()
	{
		super("check");
	}
	
	/**
	 * Command parsing.
	 * 
	 * @param sender	Player who executed the command
	 * @param args		Arguments passed
	 */
	private boolean parseInput(ProxiedPlayer sender, String[] args)
	{
		if (args.length != 1) {
			logger.info(this, "Invalid usage.");
			sender.sendMessage(new TextComponent("§cSyntaxe: /check <joueur>"));
			return false;
		}
		
		if (args[0].length() > 16) {
			logger.info(this, "Invalid username.");
			sender.sendMessage(new TextComponent("§cUn pseudo ne peut contenir plus de 16 caractères."));
			return false;
		}
		
		return true;
	}
	
	/**
	 * Sends a full dump of the targeted player's data to the command sender.
	 * 
	 * @param target			Targeted player
	 * @param targetPlayerData	Targed player's data
	 * @param sender			Player who executed the command
	 */
	private void displayPlayerData(ProxiedPlayer target, PlayerData targetPlayerData, ProxiedPlayer sender)
	{
		String isConnected = "";
		
		if (target != null && target.isConnected()) {
			ServerInfo targetServer = target.getServer().getInfo();
			
			isConnected = "§aOui §7§o⇒ " + targetServer.getName() + " (" + targetServer.getSocketAddress() + ")";
		} else
			isConnected = "§cNon";
		
		Rank targetRank = targetPlayerData.getProfile().getRank();
		
		sender.sendMessage(
			new TextComponent("§bCheck" + "\n"),
			new TextComponent("§3Pseudo : §r" + targetPlayerData.getUsername() + "\n"),
			new TextComponent("§3UUID : §r" + targetPlayerData.getUniqueId() + "\n"),
			new TextComponent("§3Rank : " + targetRank.getColor() + targetRank.getChatName() +
				" §o(" + targetRank.getDatabaseName() + ")" + "\n"),
			new TextComponent("§3Connecté(e) au proxy : §r" + isConnected),
			new TextComponent("")
		);
	}
	
	/**
	 * Called when command is executed.
	 * 
	 * @param sender	Player who executed the command
	 * @param args		Arguments passed
	 */
	@Override
	public void execute(CommandSender sender, String[] args)
	{
		this.tekord = Tekord.getInstance();
		this.logger = tekord.getHashLogger();
		
		SQLManager sqlManager = Tekord.getInstance().getSQLManager();
		
		ProxiedPlayer player = (ProxiedPlayer) sender;
		
		logger.info(this, "Executed by " + player.getName() + " (" + player.getUniqueId() + ")");;
		
		if (!parseInput(player, args))
			return;
		
		ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[0]);
		PlayerData targetPlayerData = new PlayerData(args[0]);
		
		logger.info(this, "Targeted player: " + targetPlayerData.getUsername());
		
		try {
			targetPlayerData.fetchDataFromSql(sqlManager);
		} catch (Exception exception) {
			logger.info(this, targetPlayerData.getUsername() + " is not a valid player.");
			sender.sendMessage(
				new TextComponent("§c" + targetPlayerData.getUsername() + " n'existe pas ou ne s'est jamais connecté à ce serveur.")
			);
			return;
		}
		
		this.displayPlayerData(target, targetPlayerData, player);
		
		logger.info(this, "Command executed successfully.");
	}
	
}
