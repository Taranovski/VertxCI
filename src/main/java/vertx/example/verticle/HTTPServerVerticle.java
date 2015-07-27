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
        httpServer.requestHandler(new Handler<HttpServerRequest>() {

            public void handle(HttpServerRequest httpServerRequest) {

                String absoluteURI = httpServerRequest.absoluteURI();
                System.out.println(absoluteURI);
                if ("getFromDatabase".equals(absoluteURI)) {
                    eventBus.send("database", "get", new Handler<AsyncResult<Message<String>>>() {

                        @Override
                        public void handle(AsyncResult<Message<String>> event) {
                            httpServerRequest.response()
                                    .end(event.result().body());
                        }
                    });
                } else {
                    httpServerRequest.response()
                            .end("Hello World from Vertx (changed 8th time)!");
                }
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
