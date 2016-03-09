package tech.spencercolton.tasp.router.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Spencer Colton
 */
public class Message {

    private final List<String> parts;

    public Message(List<String> components) {
        this.parts = components;
    }

    public Message(String... components) {
        this(Arrays.asList(components));
    }

    public Message(MessageType mt, String... components) {
        if(mt == MessageType.REQUEST) {
            List<String> comp = new ArrayList<>();
            comp.add("REQ");
            comp.addAll(Arrays.asList(components));
            comp.add("TERM");
            this.parts = comp;
        } else if (mt == MessageType.RESPONSE) {
            List<String> comp = new ArrayList<>();
            comp.add("RES");
            comp.addAll(Arrays.asList(components));
            comp.add("TERM");
            this.parts = comp;
        } else {
            this.parts = new ArrayList<>();
        }
    }

    public Stream<String> getParts() {
        return parts.stream();
    }

    public List<String> getPartsAsList() {
        return parts;
    }

}
