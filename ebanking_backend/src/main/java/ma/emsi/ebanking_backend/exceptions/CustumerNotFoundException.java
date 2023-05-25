package ma.emsi.ebanking_backend.exceptions;

public class CustumerNotFoundException extends RuntimeException {
    public CustumerNotFoundException(String message) {
         super(message);
    }
}
