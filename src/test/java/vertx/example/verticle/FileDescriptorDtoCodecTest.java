package vertx.example.verticle;

import io.vertx.core.buffer.Buffer;
import org.junit.Test;
import vertx.example.common.dto.FileDescriptorDto;
import vertx.example.common.dto.codec.FileDescriptorDtoCodec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class FileDescriptorDtoCodecTest {

    FileDescriptorDtoCodec fileDescriptorDtoCodec = new FileDescriptorDtoCodec();

    @Test
    public void testEncodeToWire() throws Exception {

        Buffer buffer = Buffer.buffer();

        FileDescriptorDto fileDescriptorDto = new FileDescriptorDto();
        fileDescriptorDto.setUserName("alex");
        fileDescriptorDto.setFileNameWithExtension("some_text.txt");

        Buffer buffer1 = Buffer.buffer();
        buffer1.appendString("some string appended");
        fileDescriptorDto.setBuffer(buffer1);

        fileDescriptorDtoCodec.encodeToWire(buffer, fileDescriptorDto);

        System.out.println("alex".getBytes().length);
        System.out.println("some_text.txt".getBytes().length);
        System.out.println(buffer1.getBytes().length);


        System.out.println(buffer);

        assertEquals(4, buffer.getInt(0));
        assertEquals(13, buffer.getInt(4));
        assertEquals(20, buffer.getInt(8));

        assertEquals("alex", buffer.getString(12, 16));
        assertEquals("some_text.txt", buffer.getString(16, 16 + 13));
        assertEquals("some string appended", buffer.getString(16 + 13, 16 + 13 + 20));
    }

    @Test
    public void testDecodeFromWire() throws Exception {

        Buffer buffer = Buffer.buffer();
        buffer.appendInt(4);
        buffer.appendInt(13);
        buffer.appendInt(20);
        buffer.appendString("alex");
        buffer.appendString("some_text.txt");

        Buffer buffer1 = Buffer.buffer();
        buffer1.appendString("some string appended");

        buffer.appendBuffer(buffer1);

        FileDescriptorDto fileDescriptorDto = fileDescriptorDtoCodec.decodeFromWire(0, buffer);

        assertEquals("alex", fileDescriptorDto.getUserName());
        assertEquals("some_text.txt", fileDescriptorDto.getFileNameWithExtension());
        assertEquals("some string appended", fileDescriptorDto.getBuffer().toString());


    }

    @Test
    public void testTransform() throws Exception {
        FileDescriptorDto fileDescriptorDto = new FileDescriptorDto();
        fileDescriptorDto.setUserName("alex");
        fileDescriptorDto.setFileNameWithExtension("some_text.txt");

        Buffer buffer1 = Buffer.buffer();
        buffer1.appendString("some string appended");
        fileDescriptorDto.setBuffer(buffer1);

        FileDescriptorDto transform = fileDescriptorDtoCodec.transform(fileDescriptorDto);

        assertSame(fileDescriptorDto, transform);
    }

    @Test
    public void testName() throws Exception {
        assertEquals("fileUploadEntryDtoCodec", fileDescriptorDtoCodec.name());
    }

    @Test
    public void testSystemCodecID() throws Exception {
        assertEquals(-1, fileDescriptorDtoCodec.systemCodecID());
    }
}