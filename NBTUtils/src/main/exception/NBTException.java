package main.exception;

public class NBTException extends RuntimeException {
    public NBTException(String message) {
        super(message);
        System.out.println(message);
        throw new RuntimeException();
    }


    @Override
    public synchronized Throwable fillInStackTrace() {
        // fast valid
        return null;
    }
}
