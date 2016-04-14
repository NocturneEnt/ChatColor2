package com.sulphate.chatcolor2.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sulphate.chatcolor2.schedulers.ConfirmScheduler;
import com.sulphate.chatcolor2.commands.ChatColorCommand;
import com.sulphate.chatcolor2.commands.ConfirmCommand;
import com.sulphate.chatcolor2.listeners.ChatListener;
import com.sulphate.chatcolor2.listeners.PlayerJoinListener;
import com.sulphate.chatcolor2.utils.CCStrings;

public class MainClass extends JavaPlugin {

    private static MainClass plugin;
    private HashMap<Player,ConfirmScheduler> toconfirm = new HashMap<Player,ConfirmScheduler>();

    @Override
    public void onEnable() {
        plugin = this;
        //Checking if first time setup needed (Config reload)
        if (getConfig().getString("loaded") == null) {
            reload();
        }
        //Setting msg list and checking config for errors!
        List<String> messages = Arrays.asList("help", "players-only", "player-not-online", "no-permissions", "no-color-perms", "no-col-mod-perms", "invalid-color", "invalid-command", "invalid-setting", "needs-boolean", "needs-number", "current-color", "set-own-color", "set-others-color", "player-set-your-color", "this", "confirm", "did-not-confirm", "already-confirming", "nothing-to-confirm", "reloaded-config", "already-set", "set-description");
        getConfig().set("message-list", messages);
        checkConfig();
        //Console startup messages
        Bukkit.getConsoleSender().sendMessage("§b------------------------------------------------------------");
        Bukkit.getConsoleSender().sendMessage(CCStrings.prefix + "ChatColor 2 Version §b" + Bukkit.getPluginManager().getPlugin("ChatColor2").getDescription().getVersion() + " §ehas been §aLoaded§e!");
        Bukkit.getConsoleSender().sendMessage(CCStrings.prefix + "Current update: Custom Rainbow + No Config Resets on Upgrade!");
        Bukkit.getConsoleSender().sendMessage("§b------------------------------------------------------------");
        //Commands & Listeners
        getCommand("chatcolor").setExecutor(new ChatColorCommand());
        getCommand("confirm").setExecutor(new ConfirmCommand());
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
    }

    @Override
    public void onDisable() {
        plugin = null;
        Bukkit.getConsoleSender().sendMessage(CCStrings.prefix + "ChatColor 2 Version §b" + Bukkit.getPluginManager().getPlugin("ChatColor2").getDescription().getVersion() + " §ehas been §cDisabled§e!");
    }

    public static MainClass get() {
        return plugin;
    }

    public HashMap<Player,ConfirmScheduler> getConfirmees() {
        return toconfirm;
    }

    public void addConfirmee(Player p, ConfirmScheduler s) {
        toconfirm.put(p, s);
    }

    public void removeConfirmee(Player p) {
        toconfirm.remove(p);
    }


    public void reload() {
        getConfig().set("loaded", "no");
        getConfig().set("version", this.getDescription().getVersion());
        getConfig().set("settings.color-override", false);
        getConfig().set("settings.notify-others", true);
        getConfig().set("settings.join-message", true);
        getConfig().set("settings.confirm-timeout", 10);
        getConfig().set("settings.default-color", "&f");
        getConfig().set("settings.rainbow-sequence", "abcde");
        getConfig().set("messages.help", "&eType &c/chatcolor help &eto see valid colors, modifiers and settings!");
        getConfig().set("messages.players-only", "&cThis command can only be run by players.");
        getConfig().set("messages.player-not-online", "&cThat player is not online!");
        getConfig().set("messages.no-permissions", "&cYou do not have permission to use that command.");
        getConfig().set("messages.no-color-perms", "&cYou do not have permission to use that color.");
        getConfig().set("messages.no-col-mod-perms", "&cYou do not have permission to use that color or modifier!");
        getConfig().set("messages.invalid-color", "&cThat is an invalid color! &eType &d/chatcolor help &eto see valid colors and commands.");
        getConfig().set("messages.invalid-command", "&cThat is an invalid command! &eType &d/chatcolor help &eto see valid colors and commands.");
        getConfig().set("messages.invalid-modifier", "&cThat is an invalid modifier! &eType &d/chatcolor help &eto see valid colors and commands.");
        getConfig().set("messages.invalid-setting", "&cThat is an invalid setting!");
        getConfig().set("messages.needs-boolean", "&cThat setting requires a boolean! &eUse either &aTRUE &eor &cFALSE");
        getConfig().set("messages.needs-number", "&cThat setting requires a number!");
        getConfig().set("messages.current-color", "Your color is currently: ");
        getConfig().set("messages.set-own-color", "Successfully set your color to: ");
        getConfig().set("messages.set-others-color", "Successfully set &c[player]'s &ecolor to: ");
        getConfig().set("messages.player-set-your-color", "&c[player] &eset your color to: ");
        getConfig().set("messages.this", "this");
        getConfig().set("messages.confirm", "Are you sure you want to do that? Type &c/confirm &eif you are sure.");
        getConfig().set("messages.did-not-confirm", "&cYou did not confirm in time. &eNothing has been changed.");
        getConfig().set("messages.already-confirming", "&cYou cannot do that until you have confirmed or waited.");
        getConfig().set("messages.nothing-to-confirm", "&cYou have nothing to confirm!");
        getConfig().set("messages.reloaded-config", "Reloaded the config!");
        getConfig().set("messages.already-set", "&cThat value is already set!");
        getConfig().set("messages.set-description", "This command changes settings within the plugin.");
        List<String> messages = Arrays.asList("help", "players-only", "player-not-online", "no-permissions", "no-color-perms", "no-col-mod-perms", "invalid-color", "invalid-command", "invalid-setting", "needs-boolean", "needs-number", "current-color", "set-own-color", "set-others-color", "player-set-your-color", "this", "confirm", "did-not-confirm", "already-confirming", "nothing-to-confirm", "reloaded-config", "already-set", "set-description");
        getConfig().set("messages.message-list", messages);
        getConfig().set("loaded", "yes");
        saveConfig();
        reloadConfig();
    }

    public void checkConfig() {
        //TODO: Update any new values + set defaults for ones that are null / non-existant
        //Method: Check firstly if the key is in the config in a try/catch so that if it errors it sets it as well.
        
        //HashMap with all the defaults in it
        HashMap<String, Object> hmp = new HashMap<String, Object>();
        hmp.put("loaded", "yes");
        hmp.put("version", this.getDescription().getVersion());
        hmp.put("settings.color-override", false);
        hmp.put("settings.notify-others", true);
        hmp.put("settings.join-message", true);
        hmp.put("settings.confirm-timeout", 10);
        hmp.put("settings.default-color", "&f");
        hmp.put("settings.rainbow-sequence", "abcde");
        hmp.put("messages.help", "&eType &c/chatcolor help &eto see valid colors, modifiers and settings!");
        hmp.put("messages.players-only", "&cThis command can only be run by players.");
        hmp.put("messages.player-not-online", "&cThat player is not online!");
        hmp.put("messages.no-permissions", "&cYou do not have permission to use that command.");
        hmp.put("messages.no-color-perms", "&cYou do not have permission to use that color.");
        hmp.put("messages.no-col-mod-perms", "&cYou do not have permission to use that color or modifier!");
        hmp.put("messages.invalid-color", "&cThat is an invalid color! &eType &d/chatcolor help &eto see valid colors and commands.");
        hmp.put("messages.invalid-command", "&cThat is an invalid command! &eType &d/chatcolor help &eto see valid colors and commands.");
        hmp.put("messages.invalid-modifier", "&cThat is an invalid modifier! &eType &d/chatcolor help &eto see valid colors and commands.");
        hmp.put("messages.invalid-setting", "&cThat is an invalid setting!");
        hmp.put("messages.needs-boolean", "&cThat setting requires a boolean! &eUse either &aTRUE &eor &cFALSE");
        hmp.put("messages.needs-number", "&cThat setting requires a number!");
        hmp.put("messages.current-color", "Your color is currently: ");
        hmp.put("messages.set-own-color", "Successfully set your color to: ");
        hmp.put("messages.set-others-color", "Successfully set &c[player]'s &ecolor to: ");
        hmp.put("messages.player-set-your-color", "&c[player] &eset your color to: ");
        hmp.put("messages.this", "this");
        hmp.put("messages.confirm", "Are you sure you want to do that? Type &c/confirm &eif you are sure.");
        hmp.put("messages.did-not-confirm", "&cYou did not confirm in time. &eNothing has been changed.");
        hmp.put("messages.already-confirming", "&cYou cannot do that until you have confirmed or waited.");
        hmp.put("messages.nothing-to-confirm", "&cYou have nothing to confirm!");
        hmp.put("messages.reloaded-config", "Reloaded the config!");
        hmp.put("messages.already-set", "&cThat value is already set!");
        hmp.put("messages.set-description", "This command changes settings within the plugin.");
        
        //ArrayList with all keys
        List<String> keys = new ArrayList<String>();
        keys.add("loaded");
        keys.add("version");
        keys.add("settings.color-override");
        keys.add("settings.notify-others");
        keys.add("settings.join-message");
        keys.add("settings.confirm-timeout");
        keys.add("settings.default-color");
        keys.add("settings.rainbow-sequence");
        keys.add("messages.help");
        keys.add("messages.players-only");
        keys.add("messages.player-not-online");
        keys.add("messages.no-permissions");
        keys.add("messages.no-color-perms");
        keys.add("messages.no-col-mod-perms");
        keys.add("messages.invalid-color");
        keys.add("messages.invalid-command");
        keys.add("messages.invalid-modifier");
        keys.add("messages.invalid-setting");
        keys.add("messages.needs-boolean");
        keys.add("messages.needs-number");
        keys.add("messages.current-color");
        keys.add("messages.set-own-color");
        keys.add("messages.set-others-color");
        keys.add("messages.player-set-your-color");
        keys.add("messages.this");
        keys.add("messages.confirm");
        keys.add("messages.did-not-confirm");
        keys.add("messages.already-confirming");
        keys.add("messages.nothing-to-confirm");
        keys.add("messages.reloaded-config");
        keys.add("messages.already-set");
        keys.add("messages.set-description");

        for (String st : keys) {
            try {
                Object o = getConfig().get(st);
            }
            catch(Exception e) {
                getConfig().set(st, hmp.get(st));
                saveConfig();
            }
        }

        reloadConfig();
        
    }

    public String getMessage(String message) {
        return getConfig().getString("messages." + message);
    }

}
