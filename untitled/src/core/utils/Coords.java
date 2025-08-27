package core.utils;

import org.jetbrains.annotations.NotNull;

/**
 * A small class for describing coordinates within the game board.
 */

public final class Coords {
    public final int x;
    public final int y;

    public Coords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coords coords = (Coords) o;
        return x == coords.x && y == coords.y;
    }

    public @NotNull Coords add(@NotNull Coords coords) {
        return new Coords(x + coords.x, y + coords.y);
    }

    public @NotNull String toString() {
        return String.format("(%d, %d)", x, y);
    }
}
