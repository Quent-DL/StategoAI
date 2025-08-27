package core;

import core.exceptions.InvalidCoordinates;
import core.utils.Action;
import core.utils.PlayerPiece;
import core.utils.Coords;
import core.utils.PlayerId;
import org.jetbrains.annotations.NotNull;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collection;
import java.util.List;

public class BoardPlayerProxy implements IBoardState {

    /**
     * The collection of piece identifiers that this proxy has access to
     */
    @NotNull Collection<Integer> accessibleIds;

    /** The board instance with global access to all information */
    private final @NotNull IBoardState realService;


    public BoardPlayerProxy(
            @NotNull Collection<Integer> accessibleIds,
            @NotNull IBoardState realService) {
        this.accessibleIds = accessibleIds;
        this.realService = realService;
    }

    @Override
    public int[][] getBoard() {
        int[][] board =  realService.getBoard();

        // Replacing restricted pieces by a generic value
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                int squareId = board[i][j];
                if (squareId >= 0 && !accessibleIds.contains(squareId))
                    board[i][j] = ENEMY_PIECE;
            }
        }

        return board;
    }

    @Override
    public int getSquare(@NotNull Coords c) { return realService.getSquare(c); }

    @Override
    public @NotNull PlayerPiece getPiece(int pieceId)
            throws IllegalArgumentException {
        if (accessibleIds.contains(pieceId))
            return realService.getPiece(pieceId);
        else throw new IllegalArgumentException("Attempt to access restricted piece");
    }

    @Override
    public @NotNull PlayerId nowPlaying() {
        return realService.nowPlaying();
    }

    @Override
    public boolean isLegal(@NotNull Action move) {
        // TODO ADAPT to give max range to all enemy pieces
        return this.getActions(move.from).contains(move);
    }

    @Override
    public @NotNull List<Action> getActions(@NotNull Coords from)
            throws InvalidCoordinates {
        // TODO - give max range if opponent piece
        throw new NotImplementedException();
    }

    @Override
    public @NotNull IBoardState applyAction(@NotNull Action move, boolean copy) throws IllegalArgumentException {
        throw new RuntimeException("A board cannot be modified from a proxy instance");
    }

    @Override
    public boolean hasEnded() {
        return realService.hasEnded();
    }

    public boolean isValid(@NotNull Coords c) { return realService.isValid(c); }
}
