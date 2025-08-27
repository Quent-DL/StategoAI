package core;

import core.exceptions.InvalidBoardStateException;
import core.exceptions.InvalidCoordinates;
import core.utils.*;
import org.jetbrains.annotations.NotNull;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

/**
 * Representation of the state of a Stratego board from a global point of view,
 * where all or only some of the information on the board can be accessed.
 * where all the information on the board can be accessed.
 * <p>
 * Each tile of the board is occupied by nothing ({@code EMPTY_TILE}),
 * a lake ({@code LAKE_TILE}), or a piece (known).
 * <p>
 * This class is meant to be accessed by an engine to run a Stratego game,
 * NOT by the players themselves.
 */


public final class BoardGlobal implements IBoardState {
    /** The content of the board */
    private final int[][] board;

    /** The information about all pieces within the game */
    private final @NotNull PlayerPiece[] pieces;

    /** An object that listens to the game events
     * (listener design pattern) such as moves, reveals, end of game, ... */
    private final Observer gameEventObserver;

    private @NotNull PlayerId nowPlaying = PlayerId.RED;

    /** The unit directions in which pieces can move */
    private final Coords[] directions = new Coords[]{
            new Coords(1, 0),
            new Coords(-1, 0),
            new Coords(0, 1),
            new Coords(0, -1)
    };

    /**
     * Creates a new board.
     * @param initBoard the initial board with the tile contents and piece id's
     * @param pieces the content of the pieces, according to their id.
     * @param gameEventObserver an object that listens to the game events
     * (listener design pattern) such as moves, reveals, end of game, ...
     */
    public BoardGlobal(
            int[][] initBoard,
            @NotNull PlayerPiece[] pieces,
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
            @NotNull PlayerPiece[] pieces,
            Observer gameEventObserver,
            @NotNull PlayerId nowPlaying) {
        this(initBoard, pieces, gameEventObserver);
        this.nowPlaying = nowPlaying;
    }

    /**
     * @return a copy of the board's current state,
     * as a matrix of identifier id's for
     * {@code EMPTY_SQUARE}, {@code LAKE_SQUARE}, {@code ENEMY_PIECE},
     * or the identifier of an ally piece on the board.
     *
     */
    @Override
    public int[][] getBoard() { return copyBoard(); }

    @Override
    public int getSquare(Coords c) throws InvalidCoordinates {
        if (isValid(c)) return board[c.y][c.x];
        else throw new InvalidCoordinates(c);
    }

    /**
     * Assigns the specified value to a square of the board
     * @param c the coordinates of the square
     * @param value the new value to assign to that square
     */
    private void setSquare(Coords c, int value)
             throws InvalidCoordinates {
        if (isValid(c)) board[c.y][c.x] = value;
        else throw new InvalidCoordinates(c);
    }

    @Override
    public @NotNull PlayerPiece getPiece(int pieceId)
            throws IllegalArgumentException {
        try { return pieces[pieceId]; }
        catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(String.format("Invalid piece id: %d", pieceId));
        }
    }

    @Override
    public @NotNull PlayerId nowPlaying() { return nowPlaying; }

    @Override
    public @NotNull List<Action> getActions(@NotNull Coords from)
            throws InvalidCoordinates, InvalidBoardStateException {
        if (!isValid(from)) throw new InvalidCoordinates(from);

        int fromSquareId = getSquare(from);

        // Non-pieces cannot move
        if (fromSquareId == EMPTY_SQUARE
                || fromSquareId == LAKE_SQUARE)
            return Collections.emptyList();

        PlayerPiece pieceFrom = getPiece(fromSquareId);

        // Checking if it's the player's turn
        if (pieceFrom.ownerId != nowPlaying())
            return Collections.emptyList();

        // Checking all directions until unreachable square encountered
        ArrayList<Action> legalActions = new ArrayList<>();
        for (Coords dir: directions) {
            // Target coordinates of a move, incremented gradually
            Coords to = from;
            for (int i = 0; i < pieceFrom.value.maxRange(); i++) {
                // Target coordinates of a move
                to = to.add(dir);

                // Target coordinates must be on the board
                if (!isValid(to)) break;

                // Whether piece can move to/further than this square {@code to}
                // depends on this square's content
                int toSquareId = getSquare(to);
                if (toSquareId == EMPTY_SQUARE) {
                    // Piece can move to/beyond empty square
                    legalActions.add(new Action(from, to));
                } else if (toSquareId == LAKE_SQUARE) {
                    // Piece cannot move to/beyond lake
                    break;
                } else if (0 <= toSquareId && toSquareId < pieces.length) {
                    PlayerPiece pieceTo = getPiece(getSquare(to));
                    // Piece can move to another enemy piece,
                    // but not to a piece of the same team
                    if (pieceFrom.ownerId != pieceTo.ownerId)
                        legalActions.add(new Action(from, to));
                    // No piece can move beyond another piece
                    break;
                } else if (toSquareId == ENEMY_PIECE) {
                    // Should not happen since this class
                    // takes an omniscient point of view,
                    // with all information accessible
                    throw new InvalidBoardStateException(
                            "BUG - The global board must have access to all pieces");
                } else {
                    throw new InvalidBoardStateException(
                            String.format("BUG - Invalid square id encountered: %d", toSquareId));
                }
            }
        }

        return legalActions;
    }

    @Override
    public boolean isLegal(@NotNull Action move) {
        return getActions(move.from).contains(move);
    }

    @Override
    public @NotNull IBoardState applyAction(@NotNull Action move, boolean copy)
            throws IllegalArgumentException {
        if (!isLegal(move))
            throw new IllegalArgumentException("Attempt to apply illegal move to current state !");
        BoardGlobal modified = (copy) ?
                new BoardGlobal(copyBoard(), pieces,
                        gameEventObserver, nowPlaying) :
                this;

        // TODO:  handle classic moves or battles + results (notify ?)
        if (isPiece(move.to)) {
            // There is a battle: determines who wins
            // TODO - apply to "modified" the correct instructions
            throw new NotImplementedException();

        } else {
            // No battle, just move the piece
            int displacedPieceId = modified.getSquare(move.from);
            modified.setSquare(move.from, EMPTY_SQUARE);
            modified.setSquare(move.to, displacedPieceId);
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

    @Override
    public boolean isValid(@NotNull Coords c) {
        return (c.y <= 0
                && c.y < board.length
                && c.x >= 0
                && c.x < board[0].length);
    }

    /**
     * @param c some coordinates within the board
     * @return whether these coordinates are within the board
     * and point to a piece
     */
    private boolean isPiece(Coords c) {
        // Checks if coordinates are within the board
        if (!isValid(c)) return false;

        // Checks if coordinates point to a piece
        return (
                board[c.y][c.x] != EMPTY_SQUARE
                && board[c.y][c.x] != LAKE_SQUARE
                );
    }
}
