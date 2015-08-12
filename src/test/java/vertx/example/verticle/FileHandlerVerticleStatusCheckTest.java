package vertx.example.verticle;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import vertx.example.common.StatusCheck;
import vertx.example.common.dto.FileDescriptorDto;
import vertx.example.common.dto.codec.FileDescriptorDtoCodec;

import static org.junit.Assert.*;

/**
 * @author Alyx
 */
@RunWith(VertxUnitRunner.class)
public class FileHandlerVerticleStatusCheckTest {

    private static Vertx vertx;
    private static EventBus eventBus;

    @BeforeClass
    public static void before(TestContext context) {
        vertx = Vertx.vertx();
        eventBus = vertx.eventBus();
        eventBus.registerDefaultCodec(FileDescriptorDto.class, new FileDescriptorDtoCodec());
    }

    @AfterClass
    public static void after(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test(timeout = 2000L)
    public void fileHandlerVerticleShouldRespondToStatusCheckQueryLevel1() {
        FileHandlerVerticle verticle = new FileHandlerVerticle();
        vertx.deployVerticle(verticle);

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            fail();
        }


        StatusCheck statusCheck = verticle.checkVerticle(1);
        assertTrue(statusCheck.succeeded());
        assertEquals("fileHandler vertice is up and running", statusCheck.message());
        assertEquals(4, statusCheck.logLevel());
    }

    @Test(timeout = 2000L)
    public void fileHandlerVerticleShouldRespondToStatusCheckQueryLevel2() {
        FileHandlerVerticle verticle = new FileHandlerVerticle();
        vertx.deployVerticle(verticle);

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            fail();
        }

        StatusCheck statusCheck = verticle.checkVerticle(2);
        assertTrue(statusCheck.succeeded());
        assertEquals("ok, free space is sufficient", statusCheck.message());
        assertEquals(4, statusCheck.logLevel());

    }

    @Test(timeout = 2000L)
    public void fileHandlerVerticleShouldRespondToStatusCheckQueryLevel3() {
        FileHandlerVerticle verticle = new FileHandlerVerticle();
        vertx.deployVerticle(verticle);

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            fail();
        }

        StatusCheck statusCheck = verticle.checkVerticle(3);
        assertTrue(statusCheck.succeeded());
        assertEquals("ok, free space is sufficient", statusCheck.message());
        assertEquals(4, statusCheck.logLevel());

    }

    @Test(timeout = 2000L)
    public void fileHandlerVerticleShouldRespondToStatusCheckQueryLevel4() {
        FileHandlerVerticle verticle = new FileHandlerVerticle();
        vertx.deployVerticle(verticle);

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            fail();
        }

        StatusCheck statusCheck = verticle.checkVerticle(4);
        assertFalse(statusCheck.succeeded());
        assertEquals("unrecognized status check level", statusCheck.message());
        assertEquals(4, statusCheck.logLevel());

    }

}
