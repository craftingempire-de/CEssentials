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
package de.craftingempire.essentials.utils;

import de.craftingempire.essentials.CEssentialsPlugin;
import de.craftingempire.essentials.ComponentSerializer;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CEssentials; de.craftingempire.essentials.utils:ChatUtil
 *
 * @license MIT <https://opensource.org/licenses/MIT>
 *
 * @author LuciferMorningstarDev - https://github.com/LuciferMorningstarDev
 * @since 14.03.2022
 */
public class ChatUtil {

    public static final Pattern hexPatternAmpersAnd6 = Pattern.compile("&#([A-Fa-f0-9]{6})"),
            hexPatternAmpersAnd3 = Pattern.compile("&#([A-Fa-f0-9]{3})");

    private ChatUtil() {} // prevent instantiation

    /**
     * Colorize &#38;(colorCode) messages
     * @param message
     * @return messageColorized
     */
    @Deprecated(since = "01.01.2022")
    public static String colorizeCode(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Colorize &#38;#(hexCode) messages
     * @param message
     * @return messageColorized
     */
    @Deprecated(since = "01.01.2022")
    public static String colorizeHex(String message) {
        Matcher matcher = hexPatternAmpersAnd6.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 32);
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, "§x§" + group
                    .charAt(0) + '§' + group.charAt(1) + '§' + group
                    .charAt(2) + '§' + group.charAt(3) + '§' + group
                    .charAt(4) + '§' + group.charAt(5));
        }
        String after6Hex = matcher.appendTail(buffer).toString();
        Matcher matcher2 = hexPatternAmpersAnd3.matcher(after6Hex);
        StringBuffer buffer2 = new StringBuffer(after6Hex.length() + 32);
        while (matcher2.find()) {
            String group2 = matcher2.group(1);
            matcher2.appendReplacement(buffer2, "§x§" + group2
                    .charAt(0) + '§' + group2.charAt(0) + '§' + group2
                    .charAt(1) + '§' + group2.charAt(1) + '§' + group2
                    .charAt(2) + '§' + group2.charAt(2));
        }

        return matcher2.appendTail(buffer2).toString();
    }

    /**
     * Colorize messages with &#38;#(hexCode) and &#38;(colorCode)
     * @param message
     * @return messageColorized
     */
    @Deprecated(since = "01.01.2022")
    public static String colorizeHexAndCode(String message) {
        return colorizeCode(colorizeHex(message));
    }

    /**
     * @param message
     * @return component
     */
    @Deprecated(since = "01.01.2022")
    public static Component toComponent(String message)  {
        return Component.text(message);
    }

    /**
     * @param message
     * @return component
     */
    public static Component toColoredComponent(String message)  {
        return ComponentSerializer.etAndHEX.deserialize(message);
    }

    /**
     * Translate placeholders of placeholder api ( if not enabled placeholders will not be translated )
     * @param player
     * @param message
     * @return messageReplacement
     */
    public static String replacePlaceholders(Player player, String message) {
        return isPlaceholderAPIEnabled() ? PlaceholderAPI.setPlaceholders(player, message) : message;
    }

    /**
     * Check if placeholder api is enabled
     * @return enabled
     */
    public static boolean isPlaceholderAPIEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    /**
     * Reply with a message to a sender
     * @param sender
     * @param message
     * @return success
     */
    @Deprecated(since = "01.01.2022")
    public static boolean replySender(CommandSender sender, String message) {
        sender.sendMessage(
                colorizeHexAndCode(CEssentialsPlugin.CHAT_PREFIX + message)
        );
        return true;
    }

    /**
     * Reply with a message to a sender
     * @param sender
     * @param message
     * @return success
     */
    public static boolean replySenderComponent(CommandSender sender, String message) {
        sender.sendMessage(
                CEssentialsPlugin.CHAT_PREFIX_COMPONENT.append(
                        ComponentSerializer.etAndHEX.deserialize(message)
                )
        );
        return true;
    }

    /**
     * Reply with a message to a sender replyUnPrefixedSenderComponent
     * @param sender
     * @param message
     * @return success
     */
    public static boolean replyUnPrefixedSenderComponent(CommandSender sender, String message) {
        sender.sendMessage(
                ComponentSerializer.etAndHEX.deserialize(message)
        );
        return true;
    }

    /**
     * Reply with a component message to a sender
     * @param sender
     * @param message
     * @return success
     */
    public static boolean replySender(CommandSender sender, Component message) {
        sender.sendMessage(message);
        return true;
    }

    /**
     * Reply with a actionbar popup to a player
     * @param sender
     * @param message
     * @return success
     */
    @Deprecated(since = "01.01.2022")
    public static boolean replyHologram(Player sender, String message) {
        sender.sendActionBar(
                colorizeHexAndCode(message)
        );
        return true;
    }

    /**
     * Reply with a actionbar popup to a player
     * @param sender
     * @param message
     * @return success
     */
    public static boolean replyHologramComponent(Player sender, String message) {
        sender.sendActionBar(
                ComponentSerializer.etAndHEX.deserialize(message)
        );
        return true;
    }

    /**
     * reply with a title
     * @param sender
     * @param message ( The smaller shown text of the title )
     * @return success
     */
    @Deprecated(since = "01.01.2022")
    public static boolean replyTitle(Player sender, String message) {
        sender.sendTitle(
                colorizeHexAndCode(""),
                colorizeHexAndCode(message),
                3,40,3
        );
        return true;
    }

    /**
     * reply with a title
     * @param sender
     * @param message ( The smaller shown text of the title )
     * @return success
     */
    public static boolean replyTitleComponent(Player sender, String message) {
        sender.showTitle(
                Title.title(
                        Component.empty(),
                        ComponentSerializer.etAndHEX.deserialize(message)
                )
        );
        return true;
    }

    /**
     * reply with a title
     * @param sender
     * @param title ( The bigger shown text of the title )
     * @param message ( The smaller shown text of the title )
     * @return success
     */
    @Deprecated(since = "01.01.2022")
    public static boolean replyTitle(Player sender, String title, String message) {
        sender.sendTitle(
                colorizeHexAndCode(title),
                colorizeHexAndCode(message),
                3,40,3
        );
        return true;
    }

    public static boolean replyTitleComponent(Player sender, String title, String message) {
        sender.showTitle(
                Title.title(
                        ComponentSerializer.etAndHEX.deserialize(title),
                        ComponentSerializer.etAndHEX.deserialize(message)
                )
        );
        return true;
    }

    /**
     * reply with a title
     * @param sender
     * @param title ( The bigger shown text of the title )
     * @param message ( The smaller shown text of the title )
     * @param fadeIn the time to show the title in seconds
     * @param stay the time to stay of title in seconds
     * @param fadeOut the time to hide the title in seconds
     * @return success
     */
    @Deprecated(since = "01.01.2022")
    public static boolean replyTitle(Player sender, String title, String message, int fadeIn, int stay, int fadeOut) {
        sender.sendTitle(
                colorizeHexAndCode(title),
                colorizeHexAndCode(message),
                fadeIn*20,stay*20,fadeOut*20
        );
        return true;
    }

    /**
     * reply with a title
     * @param sender
     * @param title ( The bigger shown text of the title )
     * @param message ( The smaller shown text of the title )
     * @param fadeIn the time to show the title in seconds
     * @param stay the time to stay of title in seconds
     * @param fadeOut the time to hide the title in seconds
     * @return success
     */
    public static boolean replyTitleComponent(Player sender, String title, String message, int fadeIn, int stay, int fadeOut) {
        sender.showTitle(
                Title.title(
                        ComponentSerializer.etAndHEX.deserialize(title),
                        ComponentSerializer.etAndHEX.deserialize(message),
                        Title.Times.of(
                                Duration.ofSeconds(fadeIn),
                                Duration.ofSeconds(stay),
                                Duration.ofSeconds(fadeOut)
                        )
                )
        );
        return true;
    }

    /**
     * reply with a title
     * @param sender
     * @param title ( The bigger shown text of the title )
     * @param message ( The smaller shown text of the title )
     * @param stay the time to stay of title in seconds
     * @return success
     */
    @Deprecated(since = "01.01.2022")
    public static boolean replyTitle(Player sender, String title, String message, int stay) {
        sender.sendTitle(
                colorizeHexAndCode(title),
                colorizeHexAndCode(message),
                1,stay*20,1
        );
        return true;
    }

    public static boolean replyTitleComponent(Player sender, String title, String message, int stay) {
        sender.showTitle(
                Title.title(
                        ComponentSerializer.etAndHEX.deserialize(title),
                        ComponentSerializer.etAndHEX.deserialize(message),
                        Title.Times.of(
                                Duration.ofSeconds(1),
                                Duration.ofSeconds(stay),
                                Duration.ofSeconds(1)
                        )
                )
        );
        return true;
    }

}
