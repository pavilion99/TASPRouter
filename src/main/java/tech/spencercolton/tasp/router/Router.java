package tech.spencercolton.tasp.router;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import tech.spencercolton.tasp.router.Server.Server;

/**
 * @author Spencer Colton
 */
public class Router extends JavaPlugin {

    @Getter private Server server;
    @Getter private static int port;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        port = this.getConfig().getInt("port");
        this.server = new Server(this.getLogger());
    }

    @Override
    public void onDisable() {

    }

}
