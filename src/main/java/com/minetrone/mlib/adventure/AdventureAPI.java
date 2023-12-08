package com.minetrone.mlib.adventure;

import com.minetrone.mlib.MLib;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;

/**
 * Use PaperMC's Adventure API
 * to use special strings like <red> or <#ff0000>
 *
 * @author amownyy
 * @since v1.0.0
 */
public class AdventureAPI{

    protected static JavaPlugin instance;

    public AdventureAPI(JavaPlugin plugin) {
        instance = plugin;
    }

    /**
     * Get component from text
     * @param text text
     * @return component
     */
    public Component getComponentFromMiniMessage(String text) {
        return MiniMessage.miniMessage().deserialize(replaceLegacy(text));
    }

    /**
     * Send a message to a command sender
     * @param sender sender
     * @param s message
     */
    public void sendMessage(CommandSender sender, String s) {
        if (s == null) return;
        if (sender instanceof Player player) playerMessage(player, s);
        else consoleMessage(s);
    }

    /**
     * Send a message to console
     * @param s message
     */
    public void consoleMessage(String s) {
        if (s == null) return;
        Audience au = MLib.getAdventure().sender(Bukkit.getConsoleSender());
        au.sendMessage(getComponentFromMiniMessage(s));
    }

    /**
     * Send a message to a player
     * @param player player
     * @param s message
     */
    public void playerMessage(Player player, String s) {
        if (s == null) return;
        Audience au = MLib.getAdventure().player(player);
        au.sendMessage(getComponentFromMiniMessage(s));
    }

    /**
     * Send a title to a player
     * @param player player
     * @param s1 title
     * @param s2 subtitle
     * @param in in (ms)
     * @param duration duration (ms)
     * @param out out (ms)
     */
    public void playerTitle(Player player, String s1, String s2, int in, int duration, int out) {
        Audience au = MLib.getAdventure().player(player);
        Title.Times times = Title.Times.times(Duration.ofMillis(in), Duration.ofMillis(duration), Duration.ofMillis(out));
        Title title = Title.title(getComponentFromMiniMessage(s1), getComponentFromMiniMessage(s2), times);
        au.showTitle(title);
    }

    /**
     * Send a title to a player
     * @param player player
     * @param s1 title
     * @param s2 subtitle
     * @param in in (ms)
     * @param duration duration (ms)
     * @param out out (ms)
     */
    public void playerTitle(Player player, Component s1, Component s2, int in, int duration, int out) {
        Audience au = MLib.getAdventure().player(player);
        Title.Times times = Title.Times.times(Duration.ofMillis(in), Duration.ofMillis(duration), Duration.ofMillis(out));
        Title title = Title.title(s1, s2, times);
        au.showTitle(title);
    }

    /**
     * Send an actionbar to a player
     * @param player player
     * @param s actionbar
     */
    public void playerActionbar(Player player, String s) {
        Audience au = MLib.getAdventure().player(player);
        au.sendActionBar(getComponentFromMiniMessage(s));
    }

    /**
     * Play a sound to a player
     * @param player player
     * @param source sound source
     * @param key sound key
     * @param volume volume
     * @param pitch pitch
     */
    public void playerSound(Player player, Sound.Source source, Key key, float volume, float pitch) {
        Sound sound = Sound.sound(key, source, volume, pitch);
        Audience au = MLib.getAdventure().player(player);
        au.playSound(sound);
    }

    /**
     * Play a sound to a player
     * @param player player
     * @param sound sound
     */
    public void playerSound(Player player, Sound sound) {
        Audience au = MLib.getAdventure().player(player);
        au.playSound(sound);
    }

    /**
     * Replace legacy color codes
     * @param legacy legacy
     * @return string
     */
    public String replaceLegacy(String legacy) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = legacy.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (isColorCode(chars[i])) {
                if (i + 1 < chars.length) {
                    switch (chars[i+1]) {
                        case '0' -> stringBuilder.append("<black>");
                        case '1' -> stringBuilder.append("<dark_blue>");
                        case '2' -> stringBuilder.append("<dark_green>");
                        case '3' -> stringBuilder.append("<dark_aqua>");
                        case '4' -> stringBuilder.append("<dark_red>");
                        case '5' -> stringBuilder.append("<dark_purple>");
                        case '6' -> stringBuilder.append("<gold>");
                        case '7' -> stringBuilder.append("<gray>");
                        case '8' -> stringBuilder.append("<dark_gray>");
                        case '9' -> stringBuilder.append("<blue>");
                        case 'a' -> stringBuilder.append("<green>");
                        case 'b' -> stringBuilder.append("<aqua>");
                        case 'c' -> stringBuilder.append("<red>");
                        case 'd' -> stringBuilder.append("<light_purple>");
                        case 'e' -> stringBuilder.append("<yellow>");
                        case 'f' -> stringBuilder.append("<white>");
                        case 'r' -> stringBuilder.append("<reset><!italic>");
                        case 'l' -> stringBuilder.append("<bold>");
                        case 'm' -> stringBuilder.append("<strikethrough>");
                        case 'o' -> stringBuilder.append("<italic>");
                        case 'n' -> stringBuilder.append("<underlined>");
                        case 'k' -> stringBuilder.append("<obfuscated>");
                        case 'x' -> {
                            if (i + 13 >= chars.length
                                    || !isColorCode(chars[i+2])
                                    || !isColorCode(chars[i+4])
                                    || !isColorCode(chars[i+6])
                                    || !isColorCode(chars[i+8])
                                    || !isColorCode(chars[i+10])
                                    || !isColorCode(chars[i+12])) {
                                stringBuilder.append(chars[i]);
                                continue;
                            }
                            stringBuilder
                                    .append("<#")
                                    .append(chars[i+3])
                                    .append(chars[i+5])
                                    .append(chars[i+7])
                                    .append(chars[i+9])
                                    .append(chars[i+11])
                                    .append(chars[i+13])
                                    .append(">");
                            i += 13;
                        }
                        default -> {
                            stringBuilder.append(chars[i]);
                            continue;
                        }
                    }
                    i++;
                } else {
                    stringBuilder.append(chars[i]);
                }
            }
            else {
                stringBuilder.append(chars[i]);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Check if char is a color code
     * @param c char
     * @return boolean
     */
    private static boolean isColorCode(char c) {
        return c == '\u00a7' || c == '&';
    }

}