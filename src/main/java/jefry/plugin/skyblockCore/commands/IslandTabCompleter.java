package jefry.plugin.skyblockCore.commands;

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
                suggestions.add("stats"); // Add the new "stats" command suggestion
            }
        }

        return suggestions;
    }
}
