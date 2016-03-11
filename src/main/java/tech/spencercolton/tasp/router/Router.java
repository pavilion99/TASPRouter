package tech.spencercolton.tasp.router;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import tech.spencercolton.tasp.router.Server.Message;
import tech.spencercolton.tasp.router.Server.Server;

import java.net.SocketAddress;
import java.util.HashMap;

/**
 * @author Spencer Colton
 */
public class Router extends JavaPlugin {

    private Server server;
    @Getter private static int port;

    @Getter private static boolean active = true;

    private static HashMap<String, String> pairs = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        port = this.getConfig().getInt("port");
        this.server = new Server(this.getLogger());

        // TODO: make config load acceptable IP-pwd pairs
    }

    @Override
    public void onDisable() {

    }

    public static void toggle() {
        active = !active;
    }

    public static boolean isValid(SocketAddress sa, String s) {
        String saa = sa.toString();
        return pairs.get(saa).equals(s);
    }

    public static void writeAll(Message message) {
        Server.getClients().forEach(c -> c.sendInterrupt(message));
    }

}
