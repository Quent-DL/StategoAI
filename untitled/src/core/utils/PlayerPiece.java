package core.utils;

import org.jetbrains.annotations.NotNull;

/**
 * This class represents a Stratego piece on the board.
 * It is associated with a value
 * and belongs to a player.
 */

public class PlayerPiece {
    public final @NotNull PlayerId ownerId;
    public final @NotNull PieceValue value;

    /**
     * Creates a new Stratego piece, with a given value and an owner player
     * @param ownerId the id of the player that possesses the piece
     * @param value the piece's value
     */
    public PlayerPiece(@NotNull PlayerId ownerId,
                       @NotNull PieceValue value) {
        this.ownerId = ownerId;
        this.value = value;
    }

    /**
     * Returns the result of an encounter between this piece and another one.
     * @param defender the attacked piece
     * @return the outcome of the battle for this piece
     * @throws IllegalArgumentException if both pieces belong to the same team
     */
    public @NotNull PieceInteractionResult attacks(@NotNull PlayerPiece defender)
            throws IllegalArgumentException {
        if (this.ownerId == defender.ownerId)
            return this.value.attacks(defender.value);
        else
            throw new IllegalArgumentException(
                    "A piece cannot attack another piece from the same team.");
    }

}
