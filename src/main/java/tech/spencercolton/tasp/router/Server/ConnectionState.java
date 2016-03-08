package tech.spencercolton.tasp.router.Server;

/**
 * @author Spencer Colton
 */
public enum ConnectionState {

    CONNECTING,
    CONNECTED,
    WAITING_PWD,
    WAITING_CMD,
    WAITING_RES

}
