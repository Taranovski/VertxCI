/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vertx.example.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;

/**
 *
 * @author Alyx
 */
public class HTTPServerVerticle extends AbstractVerticle {

    HttpServer httpServer;
    EventBus eventBus;

    @Override
    public void start() throws Exception {
        super.start();
        httpServer = vertx.createHttpServer();
        eventBus = vertx.eventBus();
        httpServer.requestHandler((HttpServerRequest httpServerRequest) -> {
            String path = httpServerRequest.path();
            System.out.println(path);
            if ("/getFromDatabase".equals(path)) {
                eventBus.send("database", "get", (AsyncResult<Message<String>> event) -> {
                    httpServerRequest.response()
                            .end(event.result().body());
                });
            } else {
                httpServerRequest.response()
                        .end("Hello World from Vertx (changed 11th time)!");
            }
        })
                .listen(8081, "127.0.0.1");
    }

    @Override
    public void stop() throws Exception {
        if (httpServer != null) {
            httpServer.close();
        }
        super.stop();
    }

}
