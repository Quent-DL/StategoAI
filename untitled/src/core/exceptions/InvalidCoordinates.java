package core.exceptions;

import core.utils.Coords;

public class InvalidCoordinates extends ArrayIndexOutOfBoundsException {
    public InvalidCoordinates(Coords c) {
        super(String.format("The given coordinates %s fall outside the board !", c.toString()));
    }
}
