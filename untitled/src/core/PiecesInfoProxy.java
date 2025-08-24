package core;

import java.util.Set;

public class PiecesInfoProxy implements IPiecesInfo {

    // Constant to indicate that a piece's value is unknown/unaccessible
    public final Piece UNKNOWN = null;

    private final Set<Integer> accessibleIds;
    private final IPiecesInfo realService;

    public PiecesInfoProxy(Set<Integer> accessibleIds,
                      IPiecesInfo realService) {
        this.accessibleIds = accessibleIds;
        this.realService = realService;
    }

    public Piece getValue(int pieceId) throws ArrayIndexOutOfBoundsException {
        if (accessibleIds.contains(pieceId)) {
            return realService.getValue(pieceId);
        } else {
            return UNKNOWN;
        }
    }
}
