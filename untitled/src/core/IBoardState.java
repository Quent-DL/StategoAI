package core;

import core.utils.Coords;
import core.utils.PlayerId;
import org.jetbrains.annotations.NotNull;

/**
 * Representation of the state of a Stratego board from a certain point of view,
 * where all or only some of the information on the board can be accessed.
 *
 * Each tile of the board is occupied by nothing ({@code EMPTY_TILE}),
 * a lake ({@code LAKE_TILE}), or a piece.
 */

public interface IBoardState {

    int EMPTY_TILE = -1;
    int LAKE_TILE = -2;

    /**
     * Returns
     * @return a copy of the board's current state, as the form of tile content
     * (EMPTY_TILE, LAKE_TILE, or piece id)
     */
    int[][] getBoard();

    /**
     * @return the id of the player whose turn it is
     */
    @NotNull PlayerId nowPlaying();

    /**
     * Checks whether the specified move is legal,
     * moving a piece from {@code from} to {@code to}.
     * This method also accounts for the player turns.
     * @param from the origin on the board of the piece to move
     * @param to the destination
     * @return whether the move is legal according to the rules of Stratego
     * from the point of view of the instance
     */
    boolean canMove(@NotNull Coords from, @NotNull Coords to);

    /**
     * Applies the specified move, if legal. If {@code copy} is set to {@code true},
     * the move is applied to the instance's state, and {@code self} is returned.
     * Otherwise, the instance remains unmodified, and the method returns
     * a copy of the board's state where the move has been applied
     * This method also accounts for the player turns.
     * @param from the origin on the board of the piece to move
     * @param to the destination
     * @param copy whether to apply the move to the instance itself ({@code false})
     *             or to a new instance ({@code true})
     * @return {@code self} if {@code copy == false}. Otherwise, returns a copy of this instance
     * where the move has been applied.
     * @throws IllegalArgumentException if the move is illegal
     */
    @NotNull IBoardState move(@NotNull Coords from, @NotNull Coords to, boolean copy) throws IllegalArgumentException;

    // TODO change method to account for draws/winner
    boolean hasEnded();
}
