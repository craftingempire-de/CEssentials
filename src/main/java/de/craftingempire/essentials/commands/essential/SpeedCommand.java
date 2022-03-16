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
package de.craftingempire.essentials.commands.essential;

import de.craftingempire.essentials.CEssentialsPlugin;
import de.craftingempire.essentials.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * CEssentials; de.craftingempire.essentials.commands.essential:SpeedCommand
 *
 * @license MIT <https://opensource.org/licenses/MIT>
 *
 * @author LuciferMorningstarDev - https://github.com/LuciferMorningstarDev
 * @since 14.03.2022
 */
public class SpeedCommand extends Command {

    public SpeedCommand() {
        super("speed");
        this.setDescription(CEssentialsPlugin.getChatPrefix() + "Default Essential Command Speed.");
        this.setUsage("/speed <speed> [target (fly/walk) default: walk]");
        this.setPermission("essentials.command." + this.getName().toLowerCase());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            ChatUtil.replySenderComponent(sender, "&cYou can only run this command as a player.");
            return true;
        }

        int speed = 1;
        String target = "walk";

        if(args.length == 0) {
            return ChatUtil.replySenderComponent(sender, this.getUsage());
        }

        if (args.length >= 1) {
            Player player = (Player) sender;
            try {
                speed = Integer.valueOf(args[0]);
                if(speed < 1) speed = 1;
                if(speed > 10) speed = 10;
            } catch (Exception e) {
                speed = 1;
            }

            if(args.length >= 2) target = args[1].toLowerCase();

            if(target.trim().equals("walk")) {
                if(!sender.hasPermission(this.getPermission() + ".walk")
                        && !sender.hasPermission(this.getPermission() + ".*")) {
                    return ChatUtil.replySenderComponent(sender, "&7Dir fehlt die Berechtigung: &3" + this.getPermission() + ".walk");
                }
                player.setWalkSpeed(Math.max(0.2f, 0.1f * speed));
                return ChatUtil.replySenderComponent(sender, "&7Dein &3Walking &7Speed wurde auf &3" + speed + " &7gesetzt.");
            }
            if(target.trim().equals("fly")) {
                if(!sender.hasPermission(this.getPermission() + ".fly")
                        && !sender.hasPermission(this.getPermission() + ".*")) {
                    return ChatUtil.replySenderComponent(sender, "&7Dir fehlt die Berechtigung: &3" + this.getPermission() + ".fly");
                }
                player.setFlySpeed(0.1f * speed);
                return ChatUtil.replySenderComponent(sender, "&7Dein &3Flying &7Speed wurde auf &3" + speed + " &7gesetzt.");
            }

        }

        return ChatUtil.replySenderComponent(sender, this.getUsage());
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {

        if(args.length == 1) {
            List<String> available = new ArrayList<>();
            for(int i = 1; i < 11; i++) {
                available.add(Integer.toString(i));
            }
            final List<String> completions = new ArrayList<>();
            StringUtil.copyPartialMatches(args[0], available, completions);
            Collections.sort(completions);
            return completions;
        }

        if(args.length == 2) {
            List<String> available = new ArrayList<>();
            if(sender.hasPermission(this.getPermission() + ".fly")
                    || sender.hasPermission(this.getPermission() + ".*")) {
                available.add("fly");
            }
            if(sender.hasPermission(this.getPermission() + ".walk")
                    || sender.hasPermission(this.getPermission() + ".*")) {
                available.add("walk");
            }
            final List<String> completions = new ArrayList<>();
            StringUtil.copyPartialMatches(args[1], available, completions);
            Collections.sort(completions);
            return completions;
        }

        return new ArrayList<>();
    }

}
