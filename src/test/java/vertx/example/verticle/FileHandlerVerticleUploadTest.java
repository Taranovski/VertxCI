/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vertx.example.verticle;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.*;
import org.junit.runner.RunWith;
import vertx.example.common.dto.FileDescriptorDto;
import vertx.example.common.dto.codec.FileDescriptorDtoCodec;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Alyx
 */
@RunWith(VertxUnitRunner.class)
public class FileHandlerVerticleUploadTest {

    private static Vertx vertx;
    private static EventBus eventBus;
    Path path = Paths.get("c:\\\\alex\\txt\\t\\text.txt");

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

    @Before
    public void before() {

        try {
            Files.deleteIfExists(path);
            Files.deleteIfExists(path.getParent());
            Files.deleteIfExists(path.getParent().getParent());
            Files.deleteIfExists(path.getParent().getParent().getParent());
        } catch (IOException e) {
            fail();
        }

        assertFalse(Files.exists(path));

    }

    @After
    public void after() {

        try {
            Files.deleteIfExists(path);
            Files.deleteIfExists(path.getParent());
            Files.deleteIfExists(path.getParent().getParent());
            Files.deleteIfExists(path.getParent().getParent().getParent());
        } catch (IOException e) {
            fail();
        }
        assertFalse(Files.exists(path));
    }

    @Test(timeout = 3000L)
    public void fileHandlerVerticleShouldDeploysAndUndeploySuccessfully(TestContext context) {
        // Deploy and undeploy a verticle
        vertx.deployVerticle(new FileHandlerVerticle(), context.asyncAssertSuccess((String deploymentID) -> {
            vertx.undeploy(deploymentID, context.asyncAssertSuccess());
        }));
    }

    @Test
    public void fileHandlerVerticleShouldGetFileUploadMessageAndCreateUploadedFile(TestContext context) {

        vertx.deployVerticle(new FileHandlerVerticle());

        FileDescriptorDto fileUploadEntryDto = new FileDescriptorDto();
        fileUploadEntryDto.setUserName("alex");
        fileUploadEntryDto.setFileNameWithExtension("text.txt");

        Buffer buffer = Buffer.buffer();
        buffer.appendString("this is a new String in the file");

        System.out.println(buffer);
        fileUploadEntryDto.setBuffer(buffer);

        eventBus.send("uploadFile", fileUploadEntryDto);

        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            fail();
        }
        assertTrue(Files.exists(path));

        try {
            List<String> strings = Files.readAllLines(path);
            assertEquals(1, strings.size());
            assertEquals("this is a new String in the file", strings.get(0));
        } catch (IOException e) {
            fail();
        }

    }

}
