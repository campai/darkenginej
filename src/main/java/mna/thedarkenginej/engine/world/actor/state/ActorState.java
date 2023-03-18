package mna.thedarkenginej.engine.world.actor.state;

import lombok.RequiredArgsConstructor;
import mna.thedarkenginej.engine.world.actor.Actor;
import mna.thedarkenginej.engine.world.actor.state.audio.AudibilityLevel;
import mna.thedarkenginej.engine.world.actor.state.visual.VisibilityLevel;

@RequiredArgsConstructor
@SuppressWarnings("ClassCanBeRecord")
public class ActorState {
    private final VisibilityLevel visibilityLevel;
    private final AudibilityLevel audibilityLevel;

    public boolean isVisibleTo(Actor actor) {
        return visibilityLevel.greaterOrEqualTo(actor.getVisualRange());
    }
}
