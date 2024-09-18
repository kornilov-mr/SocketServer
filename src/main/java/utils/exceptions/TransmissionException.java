package utils.exceptions;

public class TransmissionException extends RuntimeException{
    private final int exceptionCode;
    public TransmissionException(int code){
        this(code,"");
    }
    public TransmissionException(int code,String explanation) {
        super(explanation);
        this.exceptionCode= code;
    }

    public int getExceptionCode() {
        return exceptionCode;
    }
}
