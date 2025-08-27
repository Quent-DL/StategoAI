package core.utils;

import org.jetbrains.annotations.NotNull;

/**
 * A small enumeration of valid piece values in Stratego,
 * with supported interactions and basic string representations.
 */

public enum PieceValue {
    FLAG (-1, "▷", 0),
    BOMB (20, "⨂", 0),
    SPY (1, "1", 1),
    SCOUT(2, "2", Integer.MAX_VALUE),
    MINER (3, "3", 1),
    P4 (4, "4", 1),
    P5 (5, "5", 1),
    P6 (6, "6", 1),
    P7 (7, "7", 1),
    P8 (8, "8", 1),
    P9 (9, "9", 1),
    P10 (10, "X", 1);

    private final int powerValue;
    private final String strFormat;
    private final int moveRange;

    PieceValue(int powerValue, @NotNull String strFormat, int moveRange) {
        this.powerValue = powerValue;
        this.strFormat = strFormat;
        this.moveRange = moveRange;
    }

    public @NotNull String toString() { return strFormat; }

    /**
     * Returns the result of an encounter between this piece and another one.
     * @param defender the attacked piece
     * @return the outcome of the battle for this piece
     */
    public @NotNull PieceInteractionResult attacks(
            @NotNull PieceValue defender) {

        // Special interactions
        // 1) Anyone can capture the flag
        if (defender == PieceValue.FLAG)
            return PieceInteractionResult.WINS;
        // 2) Spy beats the marshall
        else if (this == PieceValue.SPY
                && defender == PieceValue.P10)
            return PieceInteractionResult.WINS;
        // 3) Miner beats the bomb
        else if (this == PieceValue.MINER
                && defender == PieceValue.BOMB)
            return PieceInteractionResult.WINS;

        // 4) Any other attacker dies to a bomb
        else if (defender == PieceValue.BOMB)
            return PieceInteractionResult.LOSES;

        // Classic interactions : see who's the strongest
        else if (this.powerValue > defender.powerValue)
            return PieceInteractionResult.WINS;
        else if (this.powerValue == defender.powerValue)
            return PieceInteractionResult.DRAWS;
        else return PieceInteractionResult.LOSES;
    }

    /**
     * @return the maximum number of squares that this piece
     * can traverse in one move.
     */
    public int maxRange() { return moveRange; }
}
