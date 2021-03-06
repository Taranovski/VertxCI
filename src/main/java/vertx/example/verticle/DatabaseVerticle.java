/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vertx.example.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;

/**
 *
 * @author Alyx
 */
public class DatabaseVerticle extends AbstractVerticle {

    EventBus eventBus;
    JDBCClient client;

    @Override
    public void start() throws Exception {
        super.start();
        eventBus = vertx.eventBus();
        client = JDBCClient.createShared(vertx, new JsonObject()
                .put("url", "jdbc:postgresql://localhost:5432/postgres")
                .put("driver_class", "org.postgresql.Driver")
                .put("user", "postgres")
                .put("password", "post")
                .put("max_pool_size", 30));

        eventBus.consumer("database", (Message<String> event) -> {
            String body = event.body();
            if ("get".equals(body)) {
                client.getConnection((AsyncResult<SQLConnection> conn) -> {
                    if (conn.failed()) {
                        System.err.println(conn.cause().getMessage());
                        return;
                    }
                    
                    // query some data with arguments
                    query(conn.result(), "select * from test", (ResultSet rs) -> {
                        event.reply(rs.getResults().toString());
                        
                        // and close the connection
                        conn.result().close((AsyncResult<Void> done) -> {
                            if (done.failed()) {
                                throw new RuntimeException(done.cause());
                            }
                        });
                    });
                });
                
            }
        });

    }

    private void query(SQLConnection conn, String sql, Handler<ResultSet> done) {
        conn.query(sql, (AsyncResult<ResultSet> res) -> {
            if (res.failed()) {
                throw new RuntimeException(res.cause());
            }
            
            done.handle(res.result());
        });
    }

    @Override
    public void stop() throws Exception {
        if (client != null) {
            client.close();
        }
        super.stop();
    }
}
