package core.utils;

import org.jetbrains.annotations.NotNull;

public final class Action {
    public final @NotNull Coords from;
    public final @NotNull Coords to;

    public Action(@NotNull Coords from, @NotNull Coords to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public @NotNull String toString() {
        return String.format("%s->%s", from, to);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action other = (Action) o;
        return from.equals(other.from) && to.equals(other.to);
    }
}
