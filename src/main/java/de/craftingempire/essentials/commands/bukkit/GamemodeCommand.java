/**
 * CEssentials | Copyright (c) 2022 LuciferMorningstarDev
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.craftingempire.essentials.commands.bukkit;

import com.google.common.collect.Lists;
import de.craftingempire.essentials.CEssentialsPlugin;
import de.craftingempire.essentials.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CEssentials; de.craftingempire.essentials.commands.bukkit:GamemodeCommand
 *
 * @license MIT <https://opensource.org/licenses/MIT>
 *
 * @author LuciferMorningstarDev - https://github.com/LuciferMorningstarDev
 * @since 14.03.2022
 */
public class GamemodeCommand extends Command {

    public GamemodeCommand() {
        super("gamemode");
        this.setDescription(CEssentialsPlugin.getChatPrefix() + "Default Bukkit Command Overwrite GameMode.");
        this.setUsage("/gamemode <gamemode> [target]");
        this.setPermission("essentials.command." + this.getName().toLowerCase());
        this.setAliases(Lists.newArrayList("gm"));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {

        GameMode targetMode  = null;
        Player target = null;

        if(args.length == 0) {
            return ChatUtil.replySenderComponent(sender, this.getUsage());
        }

        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                ChatUtil.replySenderComponent(sender, "&cYou can only run this command as a player.");
                return true;
            }
            Player player = (Player) sender;
            target = player;
            switch (args[0].toLowerCase()) {
                case "survival": case "s": case "0":
                    if(!sender.hasPermission(this.getPermission() + ".survival")
                            && !sender.hasPermission(this.getPermission() + ".*")) {
                        ChatUtil.replySenderComponent(sender, "&7Dir fehlt die Berechtigung: &3" + this.getPermission() + ".survival");
                        return true;
                    }
                    targetMode = GameMode.SURVIVAL;
                    break;
                case "creative": case "c": case "1":
                    if(!sender.hasPermission(this.getPermission() + ".creative")
                            && !sender.hasPermission(this.getPermission() + ".*")) {
                        ChatUtil.replySenderComponent(sender, "&7Dir fehlt die Berechtigung: &3" + this.getPermission() + ".creative");
                        return true;
                    }
                    targetMode = GameMode.CREATIVE;
                    break;
                case "adventure": case "a": case "2":
                    if(!sender.hasPermission(this.getPermission() + ".adventure")
                            && !sender.hasPermission(this.getPermission() + ".*")) {
                        ChatUtil.replySenderComponent(sender, "&7Dir fehlt die Berechtigung: &3" + this.getPermission() + ".adventure");
                        return true;
                    }
                    targetMode = GameMode.ADVENTURE;
                    break;
                case "spectator": case "spec": case "3":
                    if(!sender.hasPermission(this.getPermission() + ".spectator")
                            && !sender.hasPermission(this.getPermission() + ".*")) {
                        ChatUtil.replySenderComponent(sender, "&7Dir fehlt die Berechtigung: &3" + this.getPermission() + ".spectator");
                        return true;
                    }
                    targetMode = GameMode.SPECTATOR;
                    break;
                default: ChatUtil.replySenderComponent(sender, "&7Der angegebene Gamemode ist nicht verfügbar."); return true;
            }
            target.setGameMode(targetMode);
            ChatUtil.replySenderComponent(target, "&7Dein Spielmodus wurde auf &3" + targetMode.name().substring(0, 1) + targetMode.name().toLowerCase().substring(1) + " &7gesetzt.");
            return true;
        }

        if (args.length == 2) {
            if(!sender.hasPermission(this.getPermission() + ".others")
                    && !sender.hasPermission(this.getPermission() + ".*"))
                return true;
            switch (args[0].toLowerCase()) {
                case "survival": case "s": case "0":
                    if(!sender.hasPermission(this.getPermission() + ".others.survival")
                            && !sender.hasPermission(this.getPermission() + ".others.*")
                            && !sender.hasPermission(this.getPermission() + ".*")) {
                        ChatUtil.replySenderComponent(sender, "&7Dir fehlt die Berechtigung: &3" + this.getPermission() + ".others.survival");
                        return true;
                    }
                    targetMode = GameMode.SURVIVAL;
                    break;
                case "creative": case "c": case "1":
                    if(!sender.hasPermission(this.getPermission() + ".others.creative")
                            && !sender.hasPermission(this.getPermission() + ".others.*")
                            && !sender.hasPermission(this.getPermission() + ".*")) {
                        ChatUtil.replySenderComponent(sender, "&7Dir fehlt die Berechtigung: &3" + this.getPermission() + ".others.creative");
                        return true;
                    }
                    targetMode = GameMode.CREATIVE;
                    break;
                case "adventure": case "a": case "2":
                    if(!sender.hasPermission(this.getPermission() + ".others.adventure")
                            && !sender.hasPermission(this.getPermission() + ".others.*")
                            && !sender.hasPermission(this.getPermission() + ".*")) {
                        ChatUtil.replySenderComponent(sender, "&7Dir fehlt die Berechtigung: &3" + this.getPermission() + ".others.adventure");
                        return true;
                    }
                    targetMode = GameMode.ADVENTURE;
                    break;
                case "spectator": case "spec": case "3":
                    if(!sender.hasPermission(this.getPermission() + ".others.spectator")
                            && !sender.hasPermission(this.getPermission() + ".others.*")
                            && !sender.hasPermission(this.getPermission() + ".*")) {
                        ChatUtil.replySenderComponent(sender, "&7Dir fehlt die Berechtigung: &3" + this.getPermission() + ".others.spectator");
                        return true;
                    }
                    targetMode = GameMode.SPECTATOR;
                    break;
                default: ChatUtil.replySenderComponent(sender, "&7Der angegebene Gamemode ist nicht verfügbar."); return true;
            }
            target = Bukkit.getPlayer(args[1]);
            if(target == null) {
                ChatUtil.replySenderComponent(sender, "&7Der angegebene Spieler ist nicht online.");
                return true;
            }
            target.setGameMode(targetMode);
            ChatUtil.replySenderComponent(target, "&7Dein Spielmodus wurde auf &3" + targetMode.name().substring(0, 1) + targetMode.name().toLowerCase().substring(1) + " &7gesetzt.");
            ChatUtil.replySenderComponent(sender, "&7Du hast den Spielmodus von: &3" + target.getName() + " &7auf &3" + targetMode.name().substring(0, 1) + targetMode.name().toLowerCase().substring(1) + " &7gesetzt.");
            return true;
        }


        return ChatUtil.replySenderComponent(sender, "&7Benutzung: &3" + this.getUsage());
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {

        if(args.length == 1) {
            List<String> available = new ArrayList<>();
            if(sender.hasPermission(this.getPermission() + ".survival")
                    || sender.hasPermission(this.getPermission() + ".*")) {
                available.add("survival");
                available.add("0");
                available.add("s");
            }
            if(sender.hasPermission(this.getPermission() + ".creative")
                    || sender.hasPermission(this.getPermission() + ".*")) {
                available.add("creative");
                available.add("1");
                available.add("c");
            }
            if(sender.hasPermission(this.getPermission() + ".adventure")
                    || sender.hasPermission(this.getPermission() + ".*")) {
                available.add("adventure");
                available.add("2");
                available.add("a");
            }
            if(sender.hasPermission(this.getPermission() + ".spectator")
                    || sender.hasPermission(this.getPermission() + ".*")) {
                available.add("spectator");
                available.add("3");
                available.add("spec");
            }

            final List<String> completions = new ArrayList<>();
            StringUtil.copyPartialMatches(args[0], available, completions);
            Collections.sort(completions);
            return completions;
        }

        if(args.length == 2) {
            if(!sender.hasPermission(this.getPermission() + ".others")) return new ArrayList<>();
            List<String> available = new ArrayList<>();
            for(Player all : Bukkit.getOnlinePlayers()) {
                available.add(all.getName());
            }
            final List<String> completions = new ArrayList<>();
            StringUtil.copyPartialMatches(args[1], available, completions);
            Collections.sort(completions);
            return completions;
        }

        return new ArrayList<>();
    }

}
