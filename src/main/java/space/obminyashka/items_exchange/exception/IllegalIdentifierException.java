package space.obminyashka.items_exchange.exception;

public class IllegalIdentifierException extends BadRequestException {
    public IllegalIdentifierException(String message) {
        super(message);
    }
}
