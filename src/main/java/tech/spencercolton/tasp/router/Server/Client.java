package tech.spencercolton.tasp.router.Server;

import lombok.Getter;

import java.net.Socket;
import java.util.logging.Logger;

/**
 * @author Spencer Colton
 */
public class Client {

    @Getter private final RequestHandler requestHandler;
    @Getter private final Socket socket;

    public Client(Socket s, Logger l) {
        this.requestHandler = new RequestHandler(s, l, this);
        this.socket = s;
        this.requestHandler.start();
    }

    public void sendInterrupt(Message message) {
        this.requestHandler.queueMessage(message);
    }

}
