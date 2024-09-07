package jefry.plugin.skyblockCore.commands;

import jefry.plugin.skyblockCore.SkyblockCore;
import jefry.plugin.skyblockCore.UI.UpgradeUI;
import jefry.plugin.skyblockCore.UI.StatsUI;
import jefry.plugin.skyblockCore.VoidWorldGenerator;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class IslandCommandExecutor implements CommandExecutor {

    private final SkyblockCore plugin;

    public IslandCommandExecutor(SkyblockCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("Usage: /island <create|join|upgrade|leave|stats|createworld>");
            return true;
        }

        String subcommand = args[0].toLowerCase();

        switch (subcommand) {
            case "create":
                createIsland(player);
                break;
            case "join":
                joinIsland(player);
                break;
            case "upgrade":
                upgradeIsland(player);
                break;
            case "leave":
                leaveIsland(player);
                break;
            case "stats":
                openStats(player);
                break;
            case "export":
                exportIsland(player);  // Call the export method
                break;
            case "import":
                importIsland(player);  // Call the import method
                break;
            case "createworld":
                createSkyWorld(player);  // Command to create the "sky" world
                break;
            default:
                player.sendMessage("Unknown subcommand. Usage: /island <create|join|upgrade|leave|stats|export|import|createworld>");
                break;
        }
        return true;
    }

    private void createIsland(Player player) {
        plugin.getIslandManager().createIsland(player);
        player.sendMessage("Island created in the 'sky' world!");
    }

    private void joinIsland(Player player) {
        plugin.getIslandManager().loadIsland(player);
        player.sendMessage("You have joined your island!");
    }

    private void upgradeIsland(Player player) {
        UpgradeUI upgradeUI = new UpgradeUI(plugin);
        upgradeUI.openUpgradeMenu(player);
        player.sendMessage("Opening the upgrade menu...");
    }

    private void leaveIsland(Player player) {
        UUID playerId = player.getUniqueId();
        plugin.getIslandManager().saveIsland(playerId);
        player.teleport(player.getWorld().getSpawnLocation());
        player.sendMessage("You have left your island!");
    }

    private void openStats(Player player) {
        StatsUI statsUI = new StatsUI(plugin);
        statsUI.openStatsMenu(player);
        player.sendMessage("Opening the stats menu...");
    }

    // New method to create the "sky" world
    private void createSkyWorld(Player player) {
        if (Bukkit.getWorld("sky") == null) {
            WorldCreator wc = new WorldCreator("sky");
            wc.generator(new VoidWorldGenerator()); // Use the void generator
            wc.createWorld();
            player.sendMessage("The 'sky' world (void) has been created!");
        } else {
            player.sendMessage("The 'sky' world already exists.");
        }
    }

    private void exportIsland(Player player) {
        plugin.getIslandManager().exportIsland(player);
        player.sendMessage("Island exported successfully!");
    }

    // New method to trigger import
    private void importIsland(Player player) {
        plugin.getIslandManager().importIsland(player);
        player.sendMessage("Island imported successfully!");
    }
}
