/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vertx.example.vertxci;

import io.vertx.core.Vertx;
import java.util.Set;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import vertx.example.verticle.DatabaseVerticle;
import vertx.example.verticle.HTTPServerVerticle;

/**
 *
 * @author Alyx
 */
public class ContainerContextListener implements ServletContextListener {

    Vertx vertx;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.vertx = Vertx.vertx();
        // deploy verticles here
        vertx.deployVerticle(new HTTPServerVerticle());
        vertx.deployVerticle(new DatabaseVerticle());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (vertx != null) {
            Set<String> deploymentIDs = vertx.deploymentIDs();
            for (String deploymentID : deploymentIDs) {
                System.out.println("undeploying: " + deploymentID);
                vertx.undeploy(deploymentID);
            }
            vertx.close();
        }
    }

}
