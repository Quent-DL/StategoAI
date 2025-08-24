package core;

public enum Piece {
    FLAG (-1, "▷"),
    BOMB (20, "⨂"),
    SPY (1, "1"),
    P2 (2, "2"),
    MINER (3, "3"),
    P4 (4, "4"),
    P5 (5, "5"),
    P6 (6, "6"),
    P7 (7, "7"),
    P8 (8, "8"),
    P9 (9, "9"),
    P10 (10, "X");

    private final int powerValue;
    private final String strFormat;

    Piece(int powerValue, String strFormat) {
        this.powerValue = powerValue;
        this.strFormat = strFormat;
    }

    public String toString() { return strFormat; }


    public PieceInteractionResult attacks(
            Piece defender) {

        // Special interactions
        // 1) Anyone can capture the flag
        if (defender == Piece.FLAG)
            return PieceInteractionResult.WINS;
        // 2) Spy beats the marshall
        else if (this == Piece.SPY
                && defender == Piece.P10)
            return PieceInteractionResult.WINS;
        // 3) Miner beats the bomb
        else if (this == Piece.MINER
                && defender == Piece.BOMB)
            return PieceInteractionResult.WINS;

        // 4) Any other attacker dies to a bomb
        else if (defender == Piece.BOMB)
            return PieceInteractionResult.LOSES;

        // Classic interactions : see who's the strongest
        else if (this.powerValue > defender.powerValue)
            return PieceInteractionResult.WINS;
        else if (this.powerValue == defender.powerValue)
            return PieceInteractionResult.DRAWS;
        else { return PieceInteractionResult.LOSES; }
    }
}
