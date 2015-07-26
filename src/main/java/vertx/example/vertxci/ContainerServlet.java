/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vertx.example.vertxci;

import io.vertx.core.Vertx;

import javax.servlet.http.HttpServlet;
import java.util.Set;

/**
 * @author Alyx
 */
public class ContainerServlet extends HttpServlet {

    Vertx vertx;

    @Override
    public void init() {
        this.vertx = Vertx.vertx();
        System.out.println("VERTX!!!!");
        vertx.createHttpServer()
                .requestHandler(req -> req.response()
                        .end("Hello World from Vertx!"))
                .listen(8081, "127.0.0.1");
    }

    @Override
    public void destroy() {
        if (vertx != null) {
            Set<String> deploymentIDs = vertx.deploymentIDs();
            for (String deploymentID : deploymentIDs) {
                System.out.println("undeploying: " + deploymentID);
                vertx.undeploy(deploymentID);
            }
            vertx.close();
        }
        super.destroy();
    }

}
