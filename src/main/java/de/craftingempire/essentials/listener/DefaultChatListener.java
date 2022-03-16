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
package de.craftingempire.essentials.listener;

import de.craftingempire.essentials.CEssentialsPlugin;
import de.craftingempire.essentials.ComponentSerializer;
import de.craftingempire.essentials.PermissionManager;
import de.craftingempire.essentials.utils.ChatUtil;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;

/**
 * CEssentials; de.craftingempire.essentials.listener:DefaultChatListener
 *
 * @license MIT <https://opensource.org/licenses/MIT>
 *
 * @author LuciferMorningstarDev - https://github.com/LuciferMorningstarDev
 * @since 14.03.2022
 */
public class DefaultChatListener implements Listener {

    private final CEssentialsPlugin plugin;

    public DefaultChatListener(CEssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {

        if(event.getPlayer() == null) return;

        Component originalMessageComponent = event.originalMessage();
        String originalMessage = ComponentSerializer.etAndHEX.serialize(originalMessageComponent);

        if(event.getPlayer().hasPermission("essentials.chat.colorized")) {
            originalMessage = ComponentSerializer.etOnly.serialize(ComponentSerializer.etOnly.deserialize(originalMessage));
        }

        if(event.getPlayer().hasPermission("essentials.chat.colorized.hex")) {
            originalMessage = ComponentSerializer.etAndHEX.serialize(ComponentSerializer.etAndHEX.deserialize(originalMessage));
        }

        Player player = event.getPlayer();
        PermissionManager perms = plugin.getPerms();
        String group = perms.getDefaultPlayerGroupId(player);

        String format = Objects.<String>requireNonNull(
                    plugin.getChatFormatConfig()
                            .getString(
                                    (plugin.getChatFormatConfig().getString("group-formats." + group) != null) ?
                                            ("group-formats." + group) : "chat-format")
                )
                .replace("{world}", player.getWorld().getName())
                .replace("{prefix}", perms.getPrefix(player))
                .replace("{prefixes}", perms.getPrefixes(player))
                .replace("{name}", player.getName())
                .replace("{suffix}", perms.getSuffix(player))
                .replace("{suffixes}", perms.getSuffixes(player))
                .replace("{username-color}", (perms.playerMeta(player).getMetaValue("username-color") != null) ? perms.playerMeta(player).getMetaValue("username-color") : ((perms.groupMeta(group).getMetaValue("username-color") != null) ? perms.groupMeta(group).getMetaValue("username-color") : ""))
                .replace("{message-color}", (perms.playerMeta(player).getMetaValue("message-color") != null) ? perms.playerMeta(player).getMetaValue("message-color") : ((perms.groupMeta(group).getMetaValue("message-color") != null) ? perms.groupMeta(group).getMetaValue("message-color") : ""));

        String prefix = ChatUtil.colorizeHexAndCode(perms.resolvePlayerGroupPrefix(event.getPlayer()));

        format = ChatUtil.colorizeHexAndCode(ChatUtil.isPlaceholderAPIEnabled() ? ChatUtil.replacePlaceholders(player, format) : format);
        format = format.replace("{message}", (player.hasPermission("core.chat.colorcodes") && player.hasPermission("core.chat.hexcodes")) ?
                ChatUtil.colorizeHexAndCode(originalMessage) : (player.hasPermission("core.chat.colorcodes") ? ChatUtil.colorizeCode(originalMessage) : (player.hasPermission("core.chat.hexcodes") ?
                ChatUtil.colorizeHex(originalMessage) : originalMessage))).replace("%", "%%");

        Component messageComponent = ComponentSerializer.sectionOnly.deserialize(format);

        event.message(messageComponent);
        event.renderer(ChatRenderer.viewerUnaware((source, sourceDisplayName, message) -> message));

    }

}
