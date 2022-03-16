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

import com.google.common.collect.Lists;
import de.craftingempire.essentials.commands.bukkit.GamemodeCommand;
import de.craftingempire.essentials.commands.bukkit.WhisperCommand;
import de.craftingempire.essentials.commands.essential.ReloadCommand;
import de.craftingempire.essentials.commands.essential.ReplyCommand;
import de.craftingempire.essentials.commands.essential.SpawnCommand;
import de.craftingempire.essentials.commands.essential.SpeedCommand;
import de.craftingempire.essentials.database.MongoDatabaseHandler;
import de.craftingempire.essentials.listener.DefaultChatListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * CEssentials; de.craftingempire.essentials:CEssentialsPlugin
 *
 * @license MIT <https://opensource.org/licenses/MIT>
 *
 * @author LuciferMorningstarDev - https://github.com/LuciferMorningstarDev
 * @since 14.03.2022
 */
public class CEssentialsPlugin extends JavaPlugin {
    private static CEssentialsPlugin instance;
    public static CEssentialsPlugin getInstance() {
        return instance;
    }

    public static String PREFIX;
    public static String CHAT_PREFIX;
    public static Component PREFIX_COMPONENT;
    public static Component CHAT_PREFIX_COMPONENT;

    private MongoDatabaseHandler databaseHandler;

    private PermissionManager perms;

    private FileConfiguration chatFormatConfig;

    @Override
    public void onLoad() {
        instance = this;
        this.getLogger().info("§aEssentials loading...");
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.reloadConfig();

        this.loadConf();

        this.prefixSetup();
        databaseHandler = new MongoDatabaseHandler(
                getConfig().getString("database.connectionString", "mongodb://127.0.0.1:27017"),
                getConfig().getString("database.name", "essentials")
        );
        perms = new PermissionManager(this);

        this.registerEventsAndCommands();

        this.getLogger().info(CHAT_PREFIX.replace("&", "§") + "§aEssentials enabled...");
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);

        try {
            db().closeSession();
        } catch(Exception e) {}

        this.getLogger().info(CHAT_PREFIX.replace("&", "§") + "§cEssentials disabled...");
    }

    private void loadConf() {
        this.saveResource("conf/chat.yml", false);
        File chatFormatConfigFile = new File(this.getDataFolder(), "conf/chat.yml");
        chatFormatConfig = YamlConfiguration.loadConfiguration(chatFormatConfigFile);
    }

    private void prefixSetup() {
        PREFIX = this.getConfig().getString("plugin.prefix", "&6[CraftingEmpire]&r");
        CHAT_PREFIX = PREFIX + " &r";
        PREFIX_COMPONENT = ComponentSerializer.etAndHEX.deserialize(PREFIX).clickEvent(ClickEvent.openUrl("https://craftingempire.de/"));
        CHAT_PREFIX_COMPONENT = ComponentSerializer.etAndHEX.deserialize(CHAT_PREFIX).clickEvent(ClickEvent.openUrl("https://craftingempire.de/"));
    }

    private void registerEventsAndCommands() {

        Bukkit.getPluginManager().registerEvents(new DefaultChatListener(this), this);

        Bukkit.getCommandMap().registerAll("essentials", Lists.newArrayList(
                new ReloadCommand(this),
                new SpeedCommand(),
                new ReplyCommand(this)
        ));
        Bukkit.getCommandMap().registerAll("essentials-bukkit", Lists.newArrayList(
                new GamemodeCommand(),
                new WhisperCommand(this)
        ));

        Document spawnCmdConfig = db().getDocument("commands", "spawn");
        if(spawnCmdConfig == null) {
            spawnCmdConfig = db().buildDocument("spawn", new Object[][]{
                    {"enabled", true},
                    {"teleport_delay_seconds", 5}
            });
            db().insertDocument("commands", spawnCmdConfig);
        }
        if(spawnCmdConfig.getBoolean("enabled")) {
            Bukkit.getCommandMap().register("essentials", new SpawnCommand(this, spawnCmdConfig));
        }

    }

    public static String getChatPrefix() {
        return CHAT_PREFIX.replace("&", "§");
    }

    public MongoDatabaseHandler db() {
        return databaseHandler;
    }

    public PermissionManager getPerms() {
        return perms;
    }

    public FileConfiguration getChatFormatConfig() {
        return chatFormatConfig;
    }

}
