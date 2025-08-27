package core;

import core.exceptions.InvalidBoardStateException;
import core.exceptions.InvalidCoordinates;
import core.utils.Action;
import core.utils.PlayerPiece;
import core.utils.Coords;
import core.utils.PlayerId;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Representation of the state of a Stratego board from a certain point of view,
 * where all or only some of the information on the board can be accessed.
 */

public interface IBoardState {

    // Constant values to describe special square contents
    int EMPTY_SQUARE = -1;
    int LAKE_SQUARE = -2;
    int ENEMY_PIECE = -3;

    /**
     *
     * @return a copy of the board's current state,
     * as a matrix of identifier id's for
     * {@code EMPTY_SQUARE}, {@code LAKE_SQUARE}, {@code ENEMY_PIECE},
     * or the identifier of a piece on the board.
     *
     */
    int[][] getBoard();

    /**
     * Returns the id of the square at the specified position
     * @param c the position to fetch
     * @return the id on the square
     */
    int getSquare(Coords c) throws InvalidCoordinates;

    /**
     * Returns the piece instance with the given id, if access is permitted
     * @param pieceId the id of the piece on the board. See {@code getBoard()}
     * @return the piece with the given id, if access to its information is allowed
     * @throws IllegalArgumentException if {@code pieceId} does not point to a piece
     * or points to a piece with restricted information.
     */
    @NotNull PlayerPiece getPiece(int pieceId)
            throws IllegalArgumentException;

    /** @return the id of the player whose turn it is */
    @NotNull PlayerId nowPlaying();

    /**
     * Checks whether the specified move is legal,
     * moving a piece from {@code from} to {@code to}.
     * This method also accounts for the player turns.
     * @param move the move that is legal-checked
     * @return whether the move is legal according to the rules of Stratego
     * from the point of view of the instance.
     */
    boolean isLegal(@NotNull Action move);

    /**
     * Returns all the available destinations from a piece at the
     * specified coordinates on the board.
     * @param from the coordinates of the square from which to move
     * @return the list of actions that can be performed from a square.
     * If the coordinates point to an accessible piece, use its
     * actual permitted actions
     * If the coordinates point to a restricted piece (e.g. an enemy),
     * treat it as a scout,
     * i.e. the piece with the highest mobility in the game.
     * If the coordinates point to an empty square,
     * a lake, returns an empty list.
     * @throws InvalidCoordinates if the coordinates fall outside the board
     */
    @NotNull List<Action> getActions(@NotNull Coords from)
        throws InvalidCoordinates, InvalidBoardStateException;

    /**
     * Applies the specified move, if legal. If {@code copy} is set to {@code true},
     * the move is applied to the instance's state, and {@code self} is returned.
     * Otherwise, the instance remains unmodified, and the method returns
     * a copy of the board's state where the move has been applied
     * This method also accounts for the player turns.
     * @param move the move to apply
     * @param copy whether to apply the move to the instance itself ({@code false})
     *             or to a new instance ({@code true})
     * @return {@code self} if {@code copy == false}. Otherwise, returns a copy of this instance
     * where the move has been applied.
     * @throws IllegalArgumentException if the move is illegal
     */
    @NotNull IBoardState applyAction(@NotNull Action move, boolean copy)
            throws IllegalArgumentException;

    // TODO change method to account for draws/winner
    boolean hasEnded();

    /**
     * Returns whether coordinates lie within the board
     * @param c the coordinates to check
     * @return {@code true} if the given coordinates are valid,
     * i.e. point to a square within the board. Returns {@code false} otherwise.
     */
    boolean isValid(@NotNull Coords c);
}
