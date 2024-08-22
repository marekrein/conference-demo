package co.reinhold.testworkconference.exception;

public class ConferenceNotFoundException extends RuntimeException {

    public ConferenceNotFoundException(String message) {
        super(message);
    }

}
