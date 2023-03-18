package mna.thedarkenginej.engine.world.actor.state;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AlertLevel {
    FIRST("Noticed something. Not sure what, will ignore after a short moment."),
    SECOND("Noticed suspicious visual or audio activity. Will do a quick search/check. Then settle down."),
    THIRD("Suspicious visual or audio activity, probably intruder, but not sure. Entering search mode. Will settle only after area is checked."),
    FOURTH("Noticed intruder or his dangerous activity. Will set alarm and actively search area."),
    FIFTH("Fight otherwise interacted with the intruder. Set alarm, actively search area. Will not settle lower than level 3.");

    @Getter
    private final String description;
}
