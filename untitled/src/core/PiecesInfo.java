package core;


import core.utils.PlayerPiece;
import org.jetbrains.annotations.NotNull;

public class PiecesInfo implements IPiecesInfo {
    private final @NotNull PlayerPiece[] pieces;

    public PiecesInfo(@NotNull PlayerPiece[] pieces) {
        this.pieces = pieces;
    }

    @Override
    public @NotNull PlayerPiece getPiece(int pieceId)
            throws ArrayIndexOutOfBoundsException {
        return pieces[pieceId];
    }
}
