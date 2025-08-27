package core.exceptions;

/**
 * A small exception thrown when a state of {@code IBoardState}
 * contains inconsistencies, illegal dispositions
 * or any content-related problem
 */

public class InvalidBoardStateException extends RuntimeException {
    public InvalidBoardStateException(String message) {
        super(message);
    }
}
