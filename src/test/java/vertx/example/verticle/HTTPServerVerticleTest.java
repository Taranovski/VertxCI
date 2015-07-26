/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vertx.example.verticle;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 *
 * @author Alyx
 */
@RunWith(VertxUnitRunner.class)
public class HTTPServerVerticleTest {

    Vertx vertx;

    public HTTPServerVerticleTest() {
    }

    @Before
    public void before(TestContext context) {
        vertx = Vertx.vertx();
    }

    @After
    public void after(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testSomeMethod(TestContext context) {
        // Deploy and undeploy a verticle
        vertx.deployVerticle(new HTTPServerVerticle(), context.asyncAssertSuccess(deploymentID -> {
            vertx.undeploy(deploymentID, context.asyncAssertSuccess());
        }));
    }

    @Test
    public void fail() {
        fail();
    }
}
