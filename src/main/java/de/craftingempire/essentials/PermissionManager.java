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
package de.craftingempire.essentials;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.SortedMap;
import java.util.UUID;

/**
 * CEssentials; de.craftingempire.essentials:PermissionManager
 *
 * @license MIT <https://opensource.org/licenses/MIT>
 *
 * @author LuciferMorningstarDev - https://github.com/LuciferMorningstarDev
 * @since 14.03.2022
 */
public class PermissionManager {

    private final CEssentialsPlugin plugin;

    private final LuckPerms luckPerms;

    private final FileConfiguration chatFormatConfig;

    public PermissionManager(CEssentialsPlugin plugin) {
        this.plugin = plugin;
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        luckPerms = provider.getProvider();
        chatFormatConfig = plugin.getChatFormatConfig();
    }

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }

    public String getDefaultPlayerGroupId(Player player) {
        return getDefaultPlayerGroupId(player.getUniqueId());
    }

    public String getDefaultPlayerGroupId(UUID uuid) {
        try {
            return luckPerms.getUserManager().getUser(uuid).getPrimaryGroup();
        } catch(Exception except) {
            return null;
        }
    }

    public String resolveGroupPrefix(String groupId) {
        return chatFormatConfig.getString(groupId, "");
    }

    public String resolvePlayerGroupPrefix(Player player) {
        return resolvePlayerGroupPrefix(player.getUniqueId());
    }

    public String resolvePlayerGroupPrefix(UUID uuid) {
        return resolveGroupPrefix(getDefaultPlayerGroupId(uuid));
    }

    public boolean isHigherGroup(String group_should_be_higher, String group_should_be_lower) {
        int higher = luckPerms.getGroupManager().getGroup(group_should_be_higher).getWeight() != null
                ? luckPerms.getGroupManager().getGroup(group_should_be_higher).getWeight().getAsInt() : 0;
        int lower = luckPerms.getGroupManager().getGroup(group_should_be_lower).getWeight() != null
                ? luckPerms.getGroupManager().getGroup(group_should_be_lower).getWeight().getAsInt() : 0;
        if(higher <= lower) {
            return false;
        }
        return true;
    }

    public boolean isHigherPlayer(Player player_should_be_higher, Player player_should_be_lower) {
        return isHigherGroup(resolvePlayerGroupPrefix(player_should_be_higher), resolvePlayerGroupPrefix(player_should_be_lower));
    }

    public CachedMetaData playerMeta(Player player) {
        return loadUser(player).getCachedData().getMetaData();
    }

    public CachedMetaData groupMeta(String group) {
        return getLuckPerms().getGroupManager().getGroup(group).getCachedData().getMetaData();
    }

    public User loadUser(Player player) {
        if (!player.isOnline()) return null;
        return getLuckPerms().getUserManager().getUser(player.getUniqueId());
    }

    public Group loadUser(String group) {
        return getLuckPerms().getGroupManager().getGroup(group);
    }

    public String getPrefix(Player player) {
        String prefix = playerMeta(player).getPrefix();
        return (prefix != null) ? prefix : "";
    }

    public String getSuffix(Player player) {
        String suffix = playerMeta(player).getSuffix();
        return (suffix != null) ? suffix : "";
    }

    public String getPrefixes(Player player) {
        SortedMap<Integer, String> map = playerMeta(player).getPrefixes();
        StringBuilder prefixes = new StringBuilder();
        for (String prefix : map.values())
            prefixes.append(prefix);
        return prefixes.toString();
    }

    public String getSuffixes(Player player) {
        SortedMap<Integer, String> map = playerMeta(player).getSuffixes();
        StringBuilder suffixes = new StringBuilder();
        for (String prefix : map.values())
            suffixes.append(prefix);
        return suffixes.toString();
    }

}
