package core;

public class PiecesInfo implements IPiecesInfo {
    private final Piece[] values;

    public PiecesInfo(Piece[] values) {
        this.values = values;
    }

    public Piece getValue(int pieceId)
            throws ArrayIndexOutOfBoundsException {
        return values[pieceId];
    }
}
