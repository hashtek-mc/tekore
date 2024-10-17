package fr.hashtek.tekore.spigot.commands.debug;

import fr.hashtek.tekore.common.constants.Constants;
import fr.hashtek.tekore.common.data.io.AbstractPublisher;
import fr.hashtek.tekore.common.data.redis.RedisAccess;
import fr.hashtek.tekore.common.rank.Rank;
import fr.hashtek.tekore.spigot.Tekore;
import fr.hashtek.tekore.common.commands.AbstractCommand;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class CommandRankPush
    extends AbstractCommand
{

    private class RankPublisher
        extends AbstractPublisher<Rank>
    {

        public RankPublisher(RedisAccess redisAccess)
        {
            super(redisAccess, Constants.RANK_PREFIX_KEY);
        }

    }

    private static final Tekore CORE = Tekore.getInstance();


    public CommandRankPush()
    {
        super("tekore.debug");
    }


    @Override
    public void execute(
        @NotNull Player player,
        @NotNull Command command,
        @NotNull String label,
        @NotNull String[] args
    )
    {
        final List<Rank> ranksToPush = Arrays.asList(
            new Rank(
                "7cdb430d-491f-5c0e-a0f8-2be1cfc7360e",
                "player",
                "§7Joueur",
                "§7Joueur",
                "§7",
                Arrays.asList(
                    ""
                )
            ),
            new Rank(
                "1b89698b-915f-5f6e-9001-966569b5653f",
                "builder",
                "§2Builder",
                "§2Builder",
                "§2",
                Arrays.asList(
                    ""
                )
            ),
            new Rank(
                "9d3dce56-cc7f-3fcf-91ec-466fbcaea3c4",
                "e-keten",
                "§de-keten",
                "§de-keten",
                "§d",
                Arrays.asList(
                    ""
                )
            ),
            new Rank(
                "b76ff45e-08d4-5be6-941f-87893d53d20a",
                "dev",
                "§3Développeur",
                "§3Dev",
                "§3",
                Arrays.asList(
                    "tekore.debug"
                )
            ),
            new Rank(
                "57d8d8f8-3aa0-5228-84cf-3b2a7a841c6e",
                "mod",
                "§cModérateur",
                "§cMod",
                "§c",
                Arrays.asList(
                    ""
                )
            ),
            new Rank(
                "4a20727b-5309-56cf-8145-1e1c24fd2cc5",
                "admin",
                "§4Administrateur",
                "§4Admin",
                "§4",
                Arrays.asList(
                    "tekore.debug"
                )
            )
        );

        final RankPublisher rankPublisher = new RankPublisher(CORE.getRedisAccess());

        for (Rank rank : ranksToPush) {
            rankPublisher.push(rank.getUuid(), rank);
        }

        player.sendMessage(ranksToPush.size() + " ranks pushed to Redis.");
    }

}
