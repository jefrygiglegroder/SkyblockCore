package jefry.plugin.skyblockCore.commands;

import jefry.plugin.skyblockCore.SkyblockCore;
import jefry.plugin.skyblockCore.Managers.CoinManager;
import jefry.plugin.skyblockCore.UI.ShopUI;
import jefry.plugin.skyblockCore.UI.StatsUI;
import jefry.plugin.skyblockCore.UI.UpgradeUI;
import jefry.plugin.skyblockCore.VoidWorldGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class IslandCommandExecutor implements CommandExecutor {

    private final SkyblockCore plugin;
    private final CoinManager coinManager;

    public IslandCommandExecutor(SkyblockCore plugin) {
        this.plugin = plugin;
        this.coinManager = plugin.getCoinManager(); // Assuming SkyblockCore has a method to access CoinManager
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("Usage: /island <create|join|upgrade|leave|stats|createworld|setmoney|addmoney|removemoney>");
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
            case "shop":
                openShop(player);
                break;
            case "leave":
                leaveIsland(player);
                break;
            case "stats":
                openStats(player);
                break;
            case "pos1":
                setPos1(player);
                break;
            case "pos2":
                setPos2(player);
                break;
            case "export":
                exportIsland(player);
                break;
            case "import":
                importIsland(player);
                break;
            case "createworld":
                createSkyWorld(player);
                break;
            case "tp":
                if (args.length > 1) {
                    teleportToWorld(player, args[1]);
                } else {
                    player.sendMessage("Usage: /island tp <world>");
                }
                break;
            case "setmoney":
                if (args.length > 1) {
                    setMoney(player, args[1]);
                } else {
                    player.sendMessage("Usage: /island setmoney <amount>");
                }
                break;
            case "addmoney":
                if (args.length > 1) {
                    addMoney(player, args[1]);
                } else {
                    player.sendMessage("Usage: /island addmoney <amount>");
                }
                break;
            case "removemoney":
                if (args.length > 1) {
                    removeMoney(player, args[1]);
                } else {
                    player.sendMessage("Usage: /island removemoney <amount>");
                }
                break;
            default:
                player.sendMessage("Unknown subcommand. Usage: /island <create|join|upgrade|leave|stats|setmoney|addmoney|removemoney>");
                break;
        }
        return true;
    }

    private void setMoney(Player player, String amountStr) {
        try {
            int amount = Integer.parseInt(amountStr);
            coinManager.addCoins(player.getUniqueId(), -coinManager.getCoins(player.getUniqueId())); // Reset to 0
            coinManager.addCoins(player.getUniqueId(), amount);
            player.sendMessage("Your balance has been set to " + amount + " coins.");
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid amount. Please enter a valid number.");
        }
    }

    private void addMoney(Player player, String amountStr) {
        try {
            int amount = Integer.parseInt(amountStr);
            coinManager.addCoins(player.getUniqueId(), amount);
            player.sendMessage(amount + " coins have been added to your account.");
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid amount. Please enter a valid number.");
        }
    }

    private void removeMoney(Player player, String amountStr) {
        try {
            int amount = Integer.parseInt(amountStr);
            if (coinManager.deductCoins(player.getUniqueId(), amount)) {
                player.sendMessage(amount + " coins have been removed from your account.");
            } else {
                player.sendMessage("Insufficient funds to remove " + amount + " coins.");
            }
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid amount. Please enter a valid number.");
        }
    }

    private void openShop(Player player) {
        ShopUI shopUI = new ShopUI(plugin);
        shopUI.openShopMenu(player);
        player.sendMessage("Opening the shop...");
    }

    private void setPos1(Player player) {
        Location location = player.getLocation();
        plugin.getIslandSelectionManager().setPos1(player, location);
        player.sendMessage("Position 1 set to your current location.");
    }

    private void setPos2(Player player) {
        Location location = player.getLocation();
        plugin.getIslandSelectionManager().setPos2(player, location);
        player.sendMessage("Position 2 set to your current location.");
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
    private void teleportToWorld(Player player, String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            player.sendMessage("World '" + worldName + "' does not exist.");
            return;
        }

        Location spawnLocation = world.getSpawnLocation();
        player.teleport(spawnLocation);
        player.sendMessage("You have been teleported to the world '" + worldName + "'.");
    }
}
