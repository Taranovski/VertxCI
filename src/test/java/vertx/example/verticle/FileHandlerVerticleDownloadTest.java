/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vertx.example.verticle;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import vertx.example.common.dto.FileDescriptorDto;
import vertx.example.common.dto.codec.FileDescriptorDtoCodec;

/**
 *
 * @author Alyx
 */
@RunWith(VertxUnitRunner.class)
public class FileHandlerVerticleDownloadTest {

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

        try {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
            Files.write(path, "this is another string in a file".getBytes());
        } catch (IOException ex) {
            fail();
        }
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

    @Test
    public void fileHandlerVerticleShouldGetFileDownloadMessageAndDownloadFile(TestContext context) {

        vertx.deployVerticle(new FileHandlerVerticle());

        FileDescriptorDto fileDownloadEntryDto = new FileDescriptorDto();
        fileDownloadEntryDto.setUserName("alex");
        fileDownloadEntryDto.setFileNameWithExtension("text.txt");

        eventBus.send("downloadFile", fileDownloadEntryDto,
                (AsyncResult<Message<FileDescriptorDto>> event) -> {
                    Buffer buffer = event.result().body().getBuffer();
                    assertEquals("this is another string in a file", buffer.toString());
                });

    }
}
