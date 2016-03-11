package tech.spencercolton.tasp.router.Server;

import lombok.Getter;
import tech.spencercolton.tasp.router.Router;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * @author Spencer Colton
 */
public class Server extends Thread {

    private ServerSocket ss;

    private Logger l;

    public Server(Logger l) {
        this(Router.getPort(), l);
    }

    @Getter private static List<Client> clients = new CopyOnWriteArrayList<>();
    @Getter private static HashMap<UUID, Request> requests = new HashMap<>();

    public Server(int port, Logger l) {
        this.l = l;
        try (ServerSocket z = new ServerSocket(port)) {
            this.ss = z;
            l.info("New server instance starting on port " + port + ".");
            this.start();
        } catch (IOException e) {
            Router.toggle();
            l.severe(" ** CANNOT START TASP SERVER ** ");
            l.severe(e.getMessage());
        }
    }

    @Override
    public void run() {
        while (Router.isActive()) {
            try {
                Client c = new Client(ss.accept(), l);
                clients.add(c);
                c.start();
            } catch (IOException e) {
                l.severe("Unable to respond to client connection request.");
            }
        }
    }

}
