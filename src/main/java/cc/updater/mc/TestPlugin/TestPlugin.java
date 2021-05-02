package cc.updater.mc.TestPlugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class TestPlugin extends JavaPlugin implements Listener {
    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        Logger logger = getLogger();

        // Plugin startup logic

        saveDefaultConfig();

        var welcome = config.getString("message");
        if (welcome != null) {
            logger.info(welcome);
        }
        if (config.getBoolean("youAreAwesome")) {
            logger.info("You are awesome!");
        }
        var rules = config.getStringList("team.rules");
        for (var s : rules){
            logger.info(s);
        }

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("onDisable is called!");
    }

    // This method checks for incoming players and sends them a message
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        var welcome = config.getString("message");
        if (welcome != null) {
            player.sendMessage(welcome);
        }
        if (config.getBoolean("youAreAwesome")) {
            player.sendMessage("You are awesome!");
        }
        var rules = config.getStringList("team.rules");
        for (var s : rules){
            player.sendMessage(s);
        }
    }
}
