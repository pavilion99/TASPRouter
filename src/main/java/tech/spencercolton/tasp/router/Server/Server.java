package tech.spencercolton.tasp.router.Server;

import tech.spencercolton.tasp.router.Router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.Random;
import java.util.logging.Logger;

/**
 * @author Spencer Colton
 */
public class Server extends Thread {

    private ServerSocket ss;
    private PrintWriter out;
    private BufferedReader in;

    private Logger l;

    public Server(Logger l) {
        this(Router.getPort(), l);
    }

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

    }

}
