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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import vertx.example.common.dto.FileDescriptorDto;
import vertx.example.common.dto.codec.FileUploadDtoCodec;

/**
 * @author Alyx
 */
@RunWith(VertxUnitRunner.class)
public class FileHandlerVerticleTest {

    Vertx vertx;

    @Before
    public void before(TestContext context) {
        vertx = Vertx.vertx();
    }

    @After
    public void after(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void fileHandlerVerticleShouldDeploysAndUndeploySuccessfully(TestContext context) {
        // Deploy and undeploy a verticle
        vertx.deployVerticle(new FileHandlerVerticle(), context.asyncAssertSuccess((String deploymentID) -> {
            vertx.undeploy(deploymentID, context.asyncAssertSuccess());
        }));
    }

    @Test
    public void fileHandlerVerticleShouldGetFileUploadMessageAndCreateUploadedFile(TestContext context) {
        vertx.deployVerticle(new FileHandlerVerticle());

        EventBus eventBus = vertx.eventBus();

        FileDescriptorDto fileUploadEntryDto = new FileDescriptorDto();
        fileUploadEntryDto.setUserName("alex");
        fileUploadEntryDto.setFileNameWithExtension("text.txt");

        Buffer buffer = Buffer.buffer();
        buffer.appendString("this is a new String in the file");

        System.out.println(buffer);
        fileUploadEntryDto.setBuffer(buffer);

        eventBus.registerDefaultCodec(FileDescriptorDto.class, new FileUploadDtoCodec());
        eventBus.send("uploadFile", fileUploadEntryDto);
    }

}
