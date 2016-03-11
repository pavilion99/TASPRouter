package tech.spencercolton.tasp.router.Server;

import lombok.Getter;
import tech.spencercolton.tasp.router.Router;

import java.util.*;

/**
 * @author Spencer Colton
 */
public class Request {

    @Getter private final UUID uid;
    @Getter private RequestStatus status;

    private static List<Request> requests = new ArrayList<>();

    @Getter private final Message message;

    @Getter private Message response;

    @Getter private final Client source;

    private HashMap<Client, ResponseType> responses = new HashMap<>();

    @Getter
    private String destination;

    private Request(Message message, Client source, UUID uid) {
        this(message, source, uid, "ALL");
    }

    private Request(Message message, Client source, UUID uid, String dest) {
        this.status = RequestStatus.WAITING;
        this.uid = uid;
        this.message = message;
        this.source = source;

        Server.getClients().stream().forEach(x -> responses.put(x, ResponseType.NONE));

        requests.add(this);

        this.destination = dest;

        this.send();
    }

    public Request(List<String> parts, Client source, UUID uid) {
        this(new Message(parts), source, uid);
    }

    public static Request getByUUID(UUID u) {
        for(Request r : requests)
            if(r.getUid().equals(u))
                return r;
        return null;
    }

    public void respond(Message response, Client c) {
        String type = response.getPartsAsList().get(2);

        switch (type) {
            case "RNA": {
                responses.put(c, ResponseType.RNA);

                int i = 0;

                for(ResponseType r : responses.values())
                    if(r == ResponseType.RNA)
                      i++;

                if (i == responses.values().size()) {
                    this.source.sendInterrupt(new Message(Arrays.asList("RES", this.getUid().toString(), "RNA", "TERM")));
                }
                return;
            }
            case "ERR": {
                responses.put(c, ResponseType.FAIL);
                break;
            }
            default: {
                responses.put(c, ResponseType.SUCCESS);
                break;
            }
        }

        this.response = response;
        if(response.getPartsAsList().get(1).equals("ERR"))
            this.status = RequestStatus.FAILURE;
        else
            this.status = RequestStatus.SUCCESS;
        this.source.sendInterrupt(response);

        requests.remove(this);
    }

    private void send() {
        if(this.destination.equals("ALL")) {
            Router.writeAll(this.getMessage());
        } else {

        }
    }

}
