package tech.spencercolton.tasp.router.Server;

import lombok.Getter;
import tech.spencercolton.tasp.router.Router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * @author Spencer Colton
 */
public class RequestHandler extends Thread {

    @Getter private final UUID uid = UUID.randomUUID();
    @Getter private final Socket client;
    private PrintWriter out;
    private BufferedReader in;

    private final Logger l;

    private ConnectionState cs = null;

    public RequestHandler(Socket s, Logger l) {
        this.l = l;
        this.client = s;

        this.cs = ConnectionState.CONNECTING;

        try (
                PrintWriter x = new PrintWriter(
                        this.client.getOutputStream(),
                        true
                );
                BufferedReader y = new BufferedReader(
                        new InputStreamReader(
                                this.client.getInputStream()
                        )
                )
        ) {
            this.out = x;
            this.in = y;

            this.start();
        } catch (IOException e) {
            l.severe("Error setting up connection to host " + client.getRemoteSocketAddress().toString() + ".");
        }
    }

    @Override
    public void run() {
        this.cs = ConnectionState.WAITING_PWD;

        String input, output = null;

        try {
            while ((input = in.readLine()) != null) {
                switch(cs) {
                    case CONNECTED: {
                        output = "PWD";
                        this.cs = ConnectionState.WAITING_PWD;
                        break;
                    }
                    case WAITING_PWD: {
                        if(Router.isValid(client.getRemoteSocketAddress(), input)) {
                            output = "AOK";
                            this.cs = ConnectionState.WAITING_CMD;
                        } else {
                            output = "PWD";
                        }
                        break;
                    }
                    case WAITING_CMD: {
                        if(!input.equals("CMD"))
                            break;

                        String request = in.readLine();
                        request += "§";
                        String temp;
                        while (!(temp = in.readLine()).equals("TERM")) {
                            request += temp;
                            request += "§";
                        }
                        request = request.substring(0, request.length() - 1);


                    }
                }

                if( output != null ) {
                    out.println(output);
                    output = null;
                }
            }
        } catch(IOException e) {
            l.severe("Error reading message from client at " + client.getRemoteSocketAddress().toString() + ".");
        }
    }

}
