package tech.spencercolton.tasp.router.Server;

import lombok.Getter;
import tech.spencercolton.tasp.router.Router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

    @Getter private static List<RequestHandler> clients = new CopyOnWriteArrayList<>();

    public Server(int port, Logger l) {
        this.l = l;
        try (ServerSocket z = new ServerSocket(port)) {
            this.ss = z;
            this.start();
        } catch (IOException e) {
            l.severe(" ** CANNOT START TASP SERVER ** ");
            l.severe(e.getMessage());
        }
    }

    @Override
    public void run() {
        while (Router.isActive()) {
            try {
                RequestHandler r = new RequestHandler(ss.accept(), l);
                clients.add(r);
                r.start();
            } catch (IOException e) {
                l.severe("Unable to respond to client connection request.");
            }
        }
    }

}
