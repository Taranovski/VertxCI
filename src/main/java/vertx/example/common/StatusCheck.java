package vertx.example.common;

/**
 * Created by darryl on 28/07/2015.
 */
public interface StatusCheck {
    /**
     * The result of the operation.
     */
    String message();

    /**
     * Did it succeed?
     *
     * @return true if it succeded or false otherwise
     */
    boolean succeeded();

    int logLevel();

}
