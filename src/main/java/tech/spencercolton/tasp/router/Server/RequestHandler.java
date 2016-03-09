package tech.spencercolton.tasp.router.Server;

import lombok.Getter;
import tech.spencercolton.tasp.router.Router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * @author Spencer Colton
 */
public class RequestHandler extends Thread {

    @Getter private final Client clientObj;

    @Getter private final UUID uid = UUID.randomUUID();
    @Getter private final Socket client;
    private PrintWriter out;
    private BufferedReader in;

    private final Logger l;

    private boolean waiting = true;

    private List<Message> queue = new ArrayList<>();

    String input = null;

    public RequestHandler(Socket s, Logger l, Client c) {
        this.l = l;
        this.client = s;
        this.clientObj = c;

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
        if(!check_pwd())
            return;

        try {
            while ((input = in.readLine()) != null) {
                waiting = false;

                switch (input) {
                    case "REQ": {
                        rcv_req();
                        break;
                    }
                    case "RES": {
                        rcv_res();
                        break;
                    }
                }

                queue.stream().forEach(
                        s -> s.getParts().forEach(
                                x -> out.println(x)
                        )
                );

                waiting = true;
            }
        } catch(IOException e) {
            l.severe("Error reading message from client at " + client.getRemoteSocketAddress().toString() + ".");
        }
    }

    private void rcv_req() {
        try {
            if (!input.equals("REQ"))
                return;

            List<String> msg = new ArrayList<>();
            msg.add("REQ");

            String temp;
            while (!(temp = in.readLine()).equals("TERM")) {
                msg.add(temp);
            }

            msg.add("TERM");

            UUID u = UUID.randomUUID();

            Request r = new Request(msg, this.clientObj, u);

            out.println("RID");
            out.println(u.toString());
        } catch (IOException e) {
            l.warning("Error reading client request at host " + client.getRemoteSocketAddress().toString() + ".");
        }
    }

    private void rcv_res() {
        try {
            if (!input.equals("RES"))
                return;

            List<String> msg = new ArrayList<>();
            msg.add("RES");

            UUID u = UUID.fromString(in.readLine());
            msg.add(u.toString());

            String temp;
            while (!(temp = in.readLine()).equals("TERM")) {
                msg.add(temp);
            }

            msg.add("TERM");

            Request r = Request.getByUUID(u);

            if(r == null)
                return;

            r.respond(new Message(msg));
        } catch (IOException e) {
            l.warning("Error reading request response at host " + client.getRemoteSocketAddress().toString() + ".");
        }
    }

    private boolean check_pwd() {
        boolean good = false;
        String inTemp;

        out.println("PWD");

        try {
            int i = 0;

            while (!good && ((inTemp = in.readLine()) != null) && i < 3) {
                if (Router.isValid(client.getRemoteSocketAddress(), inTemp))
                    good = true;
                else
                    out.println("ERR");

                i++;
            }

            if ( i >= 3 )
                out.close();

            return good;
        } catch (IOException e) {
            return false;
        }
    }

    public void queueMessage(Message m) {
        if (waiting)
            m.getParts().forEach(g -> this.out.println(g));
        else
            this.queue.add(m);
    }

}
