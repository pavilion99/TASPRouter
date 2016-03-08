package tech.spencercolton.tasp.router.Server;

/**
 * @author Spencer Colton
 */
public enum RequestStatus {

    WAITING,
    SENT,
    RESPONSE;

    public enum Response {

        SUCCESS,
        FAILURE

    }


}
