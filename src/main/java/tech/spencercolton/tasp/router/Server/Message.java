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

    public Stream<String> getParts() {
        return parts.stream();
    }

    public List<String> getPartsAsList() {
        return parts;
    }

}
