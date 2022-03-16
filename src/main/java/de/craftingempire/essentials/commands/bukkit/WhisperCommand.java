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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * CEssentials; de.craftingempire.essentials.commands.bukkit:WhisperCommand
 *
 * @license MIT <https://opensource.org/licenses/MIT>
 *
 * @author LuciferMorningstarDev - https://github.com/LuciferMorningstarDev
 * @since 14.03.2022
 */
public class WhisperCommand extends Command {
    public static UUID consoleUUID = UUID.randomUUID();
    public static HashMap<UUID, UUID> reply = new HashMap<UUID, UUID>();

    private CEssentialsPlugin plugin;

    public WhisperCommand(CEssentialsPlugin plugin) {
        super("whisper");
        this.plugin = plugin;
        this.setDescription(CEssentialsPlugin.getChatPrefix() + "Default Bukkit Command Overwrite GameMode.");
        this.setUsage("/whisper <target> <message>");
        this.setPermission("essentials.command." + this.getName().toLowerCase());
        this.setAliases(Lists.newArrayList("msg", "message"));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if(args.length > 1) {
            UUID senderUUID = (sender instanceof ConsoleCommandSender) ? WhisperCommand.consoleUUID : (sender instanceof Player) ? ((Player)sender).getUniqueId() : null;
            if(senderUUID == null) return false;
            String target = args[0].trim();
            String message = "";
            for (int i = 1; i < args.length; ++i) {
                if(i == 1) message = message + args[i];
                else message = message + " " + args[i];
            }
            if(target.equalsIgnoreCase("console")) {
                reply.put(senderUUID, consoleUUID);
                reply.put(consoleUUID, senderUUID);
                ChatUtil.replyUnPrefixedSenderComponent(sender, plugin.getChatFormatConfig().getString("msg.sender", "&o&7Du schreibst {target}: {message}")
                        .replace("{target}", Bukkit.getConsoleSender().getName())
                        .replace("{target_prefix}", "[System] ")
                        .replace("{sender}", sender.getName())
                        .replace("{sender_prefix}", sender instanceof Player ? plugin.getPerms().getPrefix(((Player)sender)) : "[System] ")
                        .replace("{message}", message));
                ChatUtil.replyUnPrefixedSenderComponent(Bukkit.getConsoleSender(), plugin.getChatFormatConfig().getString("msg.target", "&o&7{sender} schreibt dir: {message}")
                        .replace("{target}", Bukkit.getConsoleSender().getName())
                        .replace("{target_prefix}", "[System] ")
                        .replace("{sender}", sender.getName())
                        .replace("{sender_prefix}", sender instanceof Player ? plugin.getPerms().getPrefix(((Player)sender)) : "[System] ")
                        .replace("{message}", message));
                return true;
            } else {
                Player targetPlayer = Bukkit.getPlayer(target);
                if(targetPlayer == null) return ChatUtil.replySenderComponent(sender, "&7Der Spieler ist nicht online.");
                if(sender instanceof Player) {
                    if(!((Player)sender).canSee(targetPlayer)) return ChatUtil.replySenderComponent(sender, "&7Der Spieler ist nicht online.");
                }
                reply.put(senderUUID, targetPlayer.getUniqueId());
                reply.put(targetPlayer.getUniqueId(), senderUUID);
                ChatUtil.replyUnPrefixedSenderComponent(sender, plugin.getChatFormatConfig().getString("msg.sender", "&7Du schreibst {target}: {message}")
                        .replace("{target}", targetPlayer.getName())
                        .replace("{target_prefix}", plugin.getPerms().getPrefix(targetPlayer))
                        .replace("{sender}", sender.getName())
                        .replace("{sender_prefix}", sender instanceof Player ? plugin.getPerms().getPrefix(((Player)sender)) : "[System] ")
                        .replace("{message}", message));
                ChatUtil.replyUnPrefixedSenderComponent(targetPlayer, plugin.getChatFormatConfig().getString("msg.target", "&7{sender} schreibt dir: {message}")
                        .replace("{target}", targetPlayer.getName())
                        .replace("{target_prefix}", plugin.getPerms().getPrefix(targetPlayer))
                        .replace("{sender}", sender.getName())
                        .replace("{sender_prefix}", sender instanceof Player ? plugin.getPerms().getPrefix(((Player)sender)) : "[System] ")
                        .replace("{message}", message));
                return true;
            }
        }
        return ChatUtil.replySenderComponent(sender, "&7" + this.getUsage());
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if(args.length == 1) {
            List<String> available = new ArrayList<>();
            for(Player all : Bukkit.getOnlinePlayers()) {
                if(sender instanceof Player) {
                    Player pl = (Player)sender;
                    if(pl.canSee(all)) available.add(all.getName());
                } else available.add(all.getName());
            }
            available.add("console");
            final List<String> completions = new ArrayList<>();
            StringUtil.copyPartialMatches(args[0], available, completions);
            Collections.sort(completions);
            return completions;
        }

        return new ArrayList<>();
    }

}
