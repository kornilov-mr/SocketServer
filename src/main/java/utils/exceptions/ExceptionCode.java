package utils.exceptions;

import java.util.HashMap;
import java.util.Map;

//Enum with Exception's code and explanation
public enum ExceptionCode {
    ALREADY_EXISTED_CHAT(1,"Chat with this port already exist"),
    UNKNOWN_RUNTIME_EXCEPTION(-1,"unknown runtime exception"),
    EVERYTHING_IS_OKAY(0,"");
    private final int code;
    private final String explanation;

    ExceptionCode(int code, String explanation) {
        this.code = code;
        this.explanation = explanation;
    }

    private static final Map<Integer,ExceptionCode> CodeToMessage;
    static {
        CodeToMessage = new HashMap<Integer,ExceptionCode>();
        for (ExceptionCode v : ExceptionCode.values()) {
            CodeToMessage.put(v.code, v);
        }
    }
    public static ExceptionCode findByCode(int i) {
        return CodeToMessage.get(i);
    }

    public String getExplanation() {
        return explanation;
    }
}
