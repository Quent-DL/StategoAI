package core;

import core.utils.Coords;
import core.utils.PlayerId;
import org.jetbrains.annotations.NotNull;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;
import java.util.Observer;

/**
 * Representation of the state of a Stratego board from a global point of view,
 * where all or only some of the information on the board can be accessed.
 * where all the information on the board can be accessed.
 *
 * Each tile of the board is occupied by nothing ({@code EMPTY_TILE}),
 * a lake ({@code LAKE_TILE}), or a piece (known or unknown).
 *
 * This class is meant to be accessed by an engine to run a Stratego game,
 * NOT by the players themselves.
 */


public class BoardGlobal implements IBoardState {
    /** The content of the board, as a form of {@code LAKE_TILE}, {@code EMPTY_TILE},
     * or piece id's. */
    private final int[][] board;

    /** An object that maps a piece's id to its value */
    private @NotNull final IPiecesInfo pieces;

    /** An object that listens to the game events
     * (listener design pattern) such as moves, reveals, end of game, ... */
    private final Observer gameEventObserver;

    private @NotNull PlayerId nowPlaying = PlayerId.PLAYER1;

    /**
     * Creates a new board.
     * @param initBoard the initial board with the tile contents and piece id's
     * @param pieces the content of the pieces, according to their id.
     * @param gameEventObserver an object that listens to the game events
     * (listener design pattern) such as moves, reveals, end of game, ...
     */
    public BoardGlobal(
            int[][] initBoard,
            @NotNull IPiecesInfo pieces,
            Observer gameEventObserver) {
        // TODO: check that initBoard contains exactly one flag per team
        this.board = initBoard;
        this.pieces = pieces;
        this.gameEventObserver = gameEventObserver;
    }

    /**
     * Creates a new board.
     * @param initBoard the initial board with the tile contents and piece id's
     * @param pieces the content of the pieces, according to their id.
     * @param nowPlaying the id of the player whose turn it is
     * @param gameEventObserver an object that listens to the game events
     * (listener design pattern) such as moves, reveals, end of game, ...
     */
    public BoardGlobal(
            int[][] initBoard,
            @NotNull IPiecesInfo pieces,
            Observer gameEventObserver,
            @NotNull PlayerId nowPlaying) {
        // TODO: check that initBoard contains exactly one flag per team
        this(initBoard, pieces, gameEventObserver);
        this.nowPlaying = nowPlaying;
    }

    @Override
    public int[][] getBoard() { return copyBoard(); }

    @Override
    public @NotNull PlayerId nowPlaying() { return nowPlaying; }

    @Override
    public boolean canMove(@NotNull Coords from, @NotNull Coords to) {
        // TODO
        throw new NotImplementedException();
        // 1) "from" and "to" must exist on the board

        // "from" and "to" must be aligned on a grid

        // 2) "from" must point to a piece

        // 3) "from" piece must belong to the player whose turn it is

        // 4) "to" piece must NOT belong to the player whose turn it is

        // 5) "to" must be in range of "from" piece (/!\ special rules for flag, bomb, 2)
    }

    @Override
    public @NotNull IBoardState move(@NotNull Coords from, @NotNull Coords to, boolean copy)
            throws IllegalArgumentException {
        if (!canMove(from, to))
            throw new IllegalArgumentException("Attempt to apply illegal move to current state !");
        BoardGlobal modified = copy ?
                new BoardGlobal(copyBoard(), pieces,
                        gameEventObserver, nowPlaying) :
                this;

        // TODO:  handle classic moves or battles + results (notify ?)
        if (isPiece(to)) {
            // There is a battle: determines who wins
            // TODO
            throw new NotImplementedException();

        } else {
            // No battle
            // TODO
            throw new NotImplementedException();
        }

        return modified;
    }

    @Override
    public boolean hasEnded() {
        /*
        TODO: check for:
        1) Presence of flags
        2) Possible draws if flag unaccessible and no miners
         */
        throw new NotImplementedException();
    }

    /**
     * @return a deep copy of the board's content
     */
    private int[][] copyBoard() {
        final int[][] result = new int[board.length][];
        for (int i = 0; i < board.length; i++) {
            result[i] = Arrays.copyOf(board[i], board[i].length);
        }
        return result;
    }

    /**
     * @param c some coordinates within the board
     * @return whether these coordinates are within the board
     * and point to a piece
     */
    private boolean isPiece(Coords c) {
        // Checks if coordinates are within the board
        if (
                c.y < 0
                || c.y >= board.length
                || c.x < 0
                || c.x >= board[0].length
        ) { return false; }

        // Checks if coordinates point to a piece
        return (
                board[c.y][c.x] != EMPTY_TILE
                && board[c.y][c.x] != LAKE_TILE
                );
    }
}
