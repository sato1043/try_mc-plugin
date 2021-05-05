package cc.updater.mc.TestPlugin;

import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class TestPlugin extends JavaPlugin implements Listener {

    @SneakyThrows
    @Override
    public void onEnable() {

        // Plugin startup logic

        try {

            saveDefaultConfig();

            checkUpdate();

            validateConfig();

            getServer().getPluginManager().registerEvents(this, this);

        } catch (Exception exception) {
            exception.printStackTrace();
            getLogger().info("Cannot enable plugin: " + exception.getMessage());
            setEnabled(false);
            throw exception;
        }
    }

    private void checkUpdate() {
        if (!getConfig().getBoolean("checkUpdate")) return;

        try {
            new UpdateChecker(this).getVersion(version -> {
                if (!this.getDescription().getVersion().equalsIgnoreCase(version)) {
                    getLogger().info("There is a new update available.");
                }
            });
        } catch (IOException | AssertionError exception) {
            getLogger().info("Cannot look for updates: " + exception.getMessage());
        }
    }

    private void validateConfig() throws AssertionError, IOException {
        var welcome = getConfig().getString("message");
        assert welcome != null;

        var rules = getConfig().getStringList("team.rules");
        assert rules != null;
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

        var welcome = getConfig().getString("message");
        if (welcome != null) {
            player.sendMessage(welcome);
        }
        var rules = getConfig().getStringList("team.rules");
        for (var s : rules) {
            player.sendMessage(s);
        }
    }
}
