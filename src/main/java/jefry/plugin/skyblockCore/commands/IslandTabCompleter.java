package jefry.plugin.skyblockCore.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class IslandTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("island")) {
            if (args.length == 1) {
                suggestions.add("create");
                suggestions.add("join");
                suggestions.add("upgrade");
                suggestions.add("leave");
                suggestions.add("stats");
                suggestions.add("pos1");
                suggestions.add("pos2");
                suggestions.add("export");
                suggestions.add("import");
                suggestions.add("shop");
                suggestions.add("createworld");
                suggestions.add("tp");
            } else if (args.length == 2 && args[0].equalsIgnoreCase("tp")) {
                // Suggest available worlds for the tp command
                for (World world : Bukkit.getWorlds()) {
                    suggestions.add(world.getName());
                }
            }
        }

        return suggestions;
    }
}
