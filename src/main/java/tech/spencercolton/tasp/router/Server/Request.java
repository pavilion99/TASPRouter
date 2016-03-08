package tech.spencercolton.tasp.router.Server;

import lombok.Getter;

import java.util.UUID;

/**
 * @author Spencer Colton
 */
public class Request {

    @Getter private UUID uid = UUID.randomUUID();
    @Getter private RequestStatus status;

    public Request(String request) {
        this.status = RequestStatus.WAITING;

    }

}
