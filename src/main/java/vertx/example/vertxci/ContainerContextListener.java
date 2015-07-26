/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vertx.example.vertxci;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import java.util.Set;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Alyx
 */
public class ContainerContextListener implements ServletContextListener {

    Vertx vertx;
    HttpServer httpServer;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.vertx = Vertx.vertx();
        System.out.println("VERTX!!!!");
        httpServer = vertx.createHttpServer();
        httpServer
                .requestHandler(req -> req.response()
                        .end("Hello World from Vertx (changed 4th time)!"))
                .listen(8081, "127.0.0.1");
        System.out.println("server started!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        System.out.println("DESTROY!!!!");
        if (httpServer != null) {
            httpServer.close();
        }
        System.out.println("server should be closed");
        if (vertx != null) {
            Set<String> deploymentIDs = vertx.deploymentIDs();
            for (String deploymentID : deploymentIDs) {
                System.out.println("undeploying: " + deploymentID);
                vertx.undeploy(deploymentID);
            }
            vertx.close();
        }
        System.out.println("vertx should be closed");
    }

}
