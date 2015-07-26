/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vertx.example.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;

/**
 *
 * @author Alyx
 */
public class HTTPServerVerticle extends AbstractVerticle {

    HttpServer httpServer;

    @Override
    public void start() throws Exception {
        super.start();
        httpServer = vertx.createHttpServer();
        httpServer
                .requestHandler(req -> req.response()
                        .end("Hello World from Vertx (changed 5th time)!"))
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
