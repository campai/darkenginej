package mna.thedarkenginej.engine.world.actor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mna.thedarkenginej.engine.world.actor.state.audio.AudibilityLevel;
import mna.thedarkenginej.engine.world.actor.state.audio.HearingRange;
import mna.thedarkenginej.engine.world.actor.state.visual.VisibilityLevel;
import mna.thedarkenginej.engine.world.actor.state.visual.VisualRange;

@Getter
@RequiredArgsConstructor
@SuppressWarnings("ClassCanBeRecord")
public class Actor {
    private final VisibilityLevel visibilityLevel;
    private final AudibilityLevel audibilityLevel;

    // TODO: how far an actor can see/hear? and how well? (alertness level depends on distance and abilities)
    private final VisualRange visualRange;
    private final HearingRange hearingRange;
}
