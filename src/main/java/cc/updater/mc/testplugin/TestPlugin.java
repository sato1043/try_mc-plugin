package cc.updater.mc.testplugin;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

/**
 * Starts TestPlugin.
 */
public final class TestPlugin extends JavaPlugin implements Listener {

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private static TestPlugin instance = null;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private StorageReader storage = null;

    @Override
    public void onEnable() {

        // Plugin startup logic

        try {

            assert getInstance() == null;

            setInstance(this);

            saveDefaultConfig();

            checkUpdate();

            validateConfig();

            setStorage(StorageReader.createStorage(this));

            getServer().getPluginManager().registerEvents(this, this);

            getLogger().info("plugin is enabled ");

        } catch (Exception exception) {
            exception.printStackTrace();
            getLogger().info("Cannot enable plugin: " + exception.getMessage());
            setEnabled(false);
        }
    }

    private void checkUpdate() throws IOException, AssertionError {
        if (!getConfig().getBoolean("checkUpdate")) {
            return;
        }
        try {
            new UpdateChecker(this).getVersion(version -> {
                if (!this.getDescription().getVersion().equalsIgnoreCase(version)) {
                    getLogger().info("There is a new update available.");
                }
            });
        } catch (IOException | AssertionError exception) {
            getLogger().info("Cannot look for updates: " + exception.getMessage());
            throw exception;
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

        HandlerList.unregisterAll((Plugin) this);

        setInstance(null);

        getLogger().info("plugin is disabled.");
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
