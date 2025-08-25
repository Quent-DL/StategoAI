package core;

import core.utils.PlayerPiece;

public interface IPiecesInfo {
    PlayerPiece getPiece(int pieceId) throws ArrayIndexOutOfBoundsException;
}
