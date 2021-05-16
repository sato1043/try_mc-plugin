package cc.updater.mc.testplugin;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Consumer;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

/**
 * Checks plugin update.
 */
public class UpdateChecker {

    @Getter
    @Setter
    private JavaPlugin plugin;

    public UpdateChecker(JavaPlugin plugin) throws IOException, AssertionError {
        assert plugin != null;
        setPlugin(plugin);
    }

    /**
     * Gets version string from spigot-mc.
     *
     * @param consumer
     */
    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            try {
                try (var is = new URL(D.UPDATE_CHECKER_URL).openStream();
                     var scanner = new Scanner(is)) {
                    if (scanner.hasNext()) {
                        consumer.accept(scanner.next());
                    }
                }
            } catch (IOException | AssertionError exception) {
                getPlugin().getLogger().info("Cannot look for updates: " + exception.getMessage());
            }
        });
    }
}
