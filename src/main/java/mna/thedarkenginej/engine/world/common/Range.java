package mna.thedarkenginej.engine.world.common;

public record Range(int distance) {
    public boolean greaterOrEqualTo(Range range) {
        return this.distance >= range.distance();
    }
}
