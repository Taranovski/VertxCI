package vertx.example.common;

/**
 * Created by Alyx on 09.08.2015.
 */
public class FileHandlerStatusCheck implements StatusCheck {

    private boolean success;
    private String message;
    private int loglevel;

    public FileHandlerStatusCheck(boolean success, String message, int loglevel) {
        this.success = success;
        this.message = message;
        this.loglevel = loglevel;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public boolean succeeded() {
        return success;
    }

    @Override
    public int logLevel() {
        return loglevel;
    }
}
