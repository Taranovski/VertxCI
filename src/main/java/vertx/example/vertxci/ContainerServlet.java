/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vertx.example.vertxci;

import io.vertx.core.Vertx;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 *
 * @author Alyx
 */
public class ContainerServlet extends HttpServlet {

    Vertx vertx;

    @Override
    public void init() throws ServletException {
        super.init();
        vertx = Vertx.vertx();
        vertx.createHttpServer().requestHandler(req -> req.response().end("Hello World from Vertx!")).listen(8081);
    }

    @Override
    public void destroy() {
        if (vertx != null) {
            Set<String> deploymentIDs = vertx.deploymentIDs();
            for (String deploymentID : deploymentIDs) {
                vertx.undeploy(deploymentID);
            }
            vertx.close();
        }
        super.destroy();
    }

}
