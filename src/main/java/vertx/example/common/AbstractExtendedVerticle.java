package vertx.example.common;

import com.google.common.primitives.Ints;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.core.shareddata.SharedData;

import java.io.IOException;
import java.util.Properties;


/**
 * Created by darryl on 28/07/2015.
 */
public abstract class AbstractExtendedVerticle extends AbstractVerticle {

    protected AsyncMap<String, JsonObject> dataMap;    // Reference to Global the Async Map.
    protected EventBus bus;                            // Global Vertx Event Bus

    protected int logLevel = 4;                        // The level at which to log at. Defaults to Error
    // 1 = Debug, 2 =  Notice, 3 = Warning, 4 = Error, 5 = Critical.
    // Message to admin queue updates it.
    // TODO refactor to make all these defaults based on the server.properties config.


    //Fuck you java why don't you have getters and setters or null coalescing operators or "as", or good type casting or out params FUCK!  :(
    private Properties properties;

    public Properties getProperties() throws IOException {
        return (properties != null) ? properties : PropertiesManager.getServerProperties();
    }


    /**
     * Starts and registers the verticle on the bus,
     *
     * @param startFuture a future which should be called when verticle start-up is complete.
     * @throws Exception
     */
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        // Setup Event Bus
        bus = vertx.eventBus();

        // Setup Clustered Shared Map
        SharedData sd = vertx.sharedData();
//        sd.<String, JsonObject>getClusterWideMap("cmap", res -> {
//            if (res.succeeded()) {
//                dataMap = res.result();
//            } else {
//                // Something went wrong!
//                startFuture.fail("Unable to Access Shared Map");
//            }
//        });
        // Setup Consumer for Administration
        setupAdminConsumer();

        start();
        startFuture.complete();
    }

    /**
     * Puts a message on the logging queue.
     *
     * @param level   severity, 1 = Debug, 2 =  Notice, 3 = Warning, 4 = Error, 5 = Critical.
     * @param message message to log. You may want to put the current calling message in here.
     */
    public void log(int level, String message) {
        if (level < logLevel) return;   //  If  not logging to this level than return

        DeliveryOptions options = new DeliveryOptions();
        options.addHeader("severity", String.valueOf(level));
        bus.send("log", message, options);
    }

    /**
     * Sets up admin  consumers so we can adjust vertical at runtime.
     */
    private void setupAdminConsumer() {

        final Context context = vertx.getOrCreateContext();

        // MessageConsumer<AdminMessage> consumer = bus.consumer(context.deploymentID());
        // Was going to have statically typed messages but messages will need to be sent from olther langauges ie: javascript
        // keep it simple they have to be serialised anyway, so use plain string.
        // TODO investigate impact and do we need to move to static types at some point?

        bus.consumer(context.deploymentID(), message -> {                    // TODO Blah messy! tidy up.
            Integer actionLevel = Ints.tryParse((String) message.body());     // TODO Does this cast need to check for failure?
            if (actionLevel != null && message.headers().contains("action")) {

                String action = message.headers().get("action");
                if (action.equalsIgnoreCase("setloglevel")) {
                    logLevel = actionLevel;
                    message.reply("sucess");
                } else if (action.equalsIgnoreCase("statuscheck")) {

                    StatusCheck result = checkVerticle(actionLevel);         // TODO if this gets messy create a common list of error codes
                    if (result.succeeded())
                        message.reply(result.message());
                    else
                        message.fail(1, result.message());
                }
            }
            message.fail(1, "Invalid Request");
        });
    }


    /*  All subclasses must define a name, this is used  for the management of verticles. */
    protected abstract String getVerticalName();

    /* All subclasses must define a statusCheck Method.
        This will ensure the verticle is running and functional
        E.g A verticle that writes to a filesystem may check to see */
    protected abstract StatusCheck checkVerticle(int level);


}
