package core;

import core.utils.PlayerPiece;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class PiecesInfoProxy implements IPiecesInfo {

    // Constant to indicate that a piece's value is unknown/unaccessible
    public final PlayerPiece UNKNOWN = null;

    private final @NotNull Set<Integer> accessibleIds;
    private final @NotNull IPiecesInfo realService;

    public PiecesInfoProxy(@NotNull Set<Integer> accessibleIds,
                      @NotNull IPiecesInfo realService) {
        this.accessibleIds = accessibleIds;
        this.realService = realService;
    }

    @Override
    public PlayerPiece getPiece(int pieceId)
            throws ArrayIndexOutOfBoundsException {
        if (accessibleIds.contains(pieceId)) {
            return realService.getPiece(pieceId);
        } else {
            return UNKNOWN;
        }
    }
}
