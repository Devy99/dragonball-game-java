package it.unimol.dragon_ball.app.logic;

public class NotEnoughManaException extends Exception {
    public NotEnoughManaException() {
    }

    public NotEnoughManaException(String message) {
        super(message);
    }
}
