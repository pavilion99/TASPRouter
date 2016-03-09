package tech.spencercolton.tasp.router.Server;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    private Request(Message message, Client source, UUID uid) {
        this.status = RequestStatus.WAITING;
        this.uid = uid;
        this.message = message;
        this.source = source;
        requests.add(this);
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

    public void respond(Message response) {
        this.response = response;
        if(response.getPartsAsList().get(1).equals("ERR"))
            this.status = RequestStatus.FAILURE;
        else
            this.status = RequestStatus.SUCCESS;
        this.source.sendInterrupt(response);
    }

    private void send() {

    }

}
