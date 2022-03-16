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
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * CEssentials; de.craftingempire.essentials.commands.essential:SpawnCommand
 *
 * @license MIT <https://opensource.org/licenses/MIT>
 *
 * @author LuciferMorningstarDev - https://github.com/LuciferMorningstarDev
 * @since 14.03.2022
 */
public class SpawnCommand extends Command implements Listener {

    private Map<UUID, TeleportQueuedPlayer> playerQueue;

    private final CEssentialsPlugin plugin;
    private final Document conf;

    private final int teleportDelay;

    public SpawnCommand(CEssentialsPlugin plugin, Document conf) {
        super("spawn");
        this.setDescription(CEssentialsPlugin.getChatPrefix() + "Default Essential Command Spawn.");
        this.setPermission("essentials.command." + this.getName().toLowerCase());
        this.plugin = plugin;
        this.conf = conf;
        teleportDelay = conf.getInteger("teleport_delay_seconds", 3);
        playerQueue = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            try {
                if(playerQueue.isEmpty()) return;
                playerQueue.values().forEach(queuedPlayer -> {
                    try {
                        if(queuedPlayer != null && queuedPlayer.isShouldTeleport()) {
                            queuedPlayer.timer();
                        }
                    } catch(Exception e) {}
                });
            } catch(Exception e) {}
        }, 20, 20);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            ChatUtil.replySenderComponent(sender, "&cYou can only run this command as a player.");
            return true;
        }
        Player player = (Player) sender;
        if(playerQueue.containsKey(player.getUniqueId())) return ChatUtil.replySenderComponent(sender, "&7Du wirst bereits teleportiert.");
        playerQueue.put(player.getUniqueId(), new TeleportQueuedPlayer(teleportDelay, player));
        ChatUtil.replySenderComponent(player, "&7Du wurdest zur &3TeleportQueue &7hinzugefügt.");
        return false;
    }

    class TeleportQueuedPlayer {
        private boolean shouldTeleport = true;
        private final int delay;
        private int second = 0;
        private final Player toTeleport;
        public TeleportQueuedPlayer(int delay, Player toTeleport) {
            this.delay = delay;
            this.toTeleport = toTeleport;
        }
        public void addToSpawn() {
            if(shouldTeleport) {
                toTeleport.playSound(toTeleport.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                toTeleport.teleport(toTeleport.getWorld().getSpawnLocation());
            }
            shouldTeleport = false;
            playerQueue.remove(toTeleport.getUniqueId());
        }
        public void remove() {
            shouldTeleport = false;
            playerQueue.remove(toTeleport.getUniqueId());
            ChatUtil.replySenderComponent(toTeleport, "&7Teleportation abgebrochen.");
        }
        public void stat(int s) {
            if(shouldTeleport) {
                ChatUtil.replyTitleComponent(toTeleport, "&6Teleport",
                        "&7Du wirst in &3" + (delay - s) + " &7Sekunden Teleportiert.",
                        1, 1, 1);
                toTeleport.playSound(toTeleport.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_PLACE, 1f, 1f);
            }
            else remove();
        }
        public void moveEvent(PlayerMoveEvent event) {
            if(!shouldTeleport) return;
            if(event.getPlayer() == null) return;
            if(!event.getPlayer().getUniqueId().equals(toTeleport.getUniqueId())) return;
            if(event.getFrom().getX() != event.getTo().getX()
                    || event.getFrom().getY() != event.getTo().getY()
                    || event.getFrom().getZ() != event.getTo().getZ()) {
                remove();
            }
        }
        public void interactEvent(PlayerInteractEvent event) {
            if(!shouldTeleport) return;
            if(event.getPlayer() == null) return;
            if(!event.getPlayer().getUniqueId().equals(toTeleport.getUniqueId())) return;
            if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)
                || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                remove();
            }
        }
        public void timer() {
            if(second == 0) {
                ChatUtil.replySenderComponent(toTeleport, "&7Du wirst in &3" + teleportDelay + " &7Sekunden zum Spawn teleportiert.");
            }
            if(second < delay) {
                stat(second);
            } else {
                addToSpawn();
            }
            second++;
        }

        public boolean isShouldTeleport() { return shouldTeleport; }

    }

    @EventHandler
    public void onMovedPlayer(PlayerMoveEvent event) {
        if(event.getPlayer() == null) return;
        if(playerQueue.get(event.getPlayer().getUniqueId()) != null) {
            playerQueue.get(event.getPlayer().getUniqueId()).moveEvent(event);
        }
    }

    @EventHandler
    public void onMovedPlayer(PlayerInteractEvent event) {
        if(event.getPlayer() == null) return;
        if(playerQueue.get(event.getPlayer().getUniqueId()) != null) {
            playerQueue.get(event.getPlayer().getUniqueId()).interactEvent(event);
        }
    }

}
