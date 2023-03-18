package mna.thedarkenginej.engine.world.actor.state.visual;

import mna.thedarkenginej.engine.world.common.Range;

public record VisibilityLevel(Range range) {
    public boolean greaterOrEqualTo(VisibilityLevel level) {
        return this.range.greaterOrEqualTo(level.range);
    }
}
