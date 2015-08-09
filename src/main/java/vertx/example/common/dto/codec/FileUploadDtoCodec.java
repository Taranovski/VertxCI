package vertx.example.common.dto.codec;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import vertx.example.common.dto.FileDescriptorDto;

/**
 * Created by Alyx on 09.08.2015.
 */
public class FileUploadDtoCodec implements MessageCodec<FileDescriptorDto, FileDescriptorDto> {
    @Override
    public void encodeToWire(Buffer buffer, FileDescriptorDto fileUploadEntryDto) {

        String userName = fileUploadEntryDto.getUserName();
        String fileNameWithExtension = fileUploadEntryDto.getFileNameWithExtension();
        Buffer readStream = fileUploadEntryDto.getBuffer();

        buffer.appendInt(userName.getBytes().length);
        buffer.appendInt(fileNameWithExtension.getBytes().length);
        buffer.appendInt(readStream.getBytes().length);

        buffer.appendString(userName);
        buffer.appendString(fileNameWithExtension);
        buffer.appendBuffer(readStream);
    }

    @Override
    public FileDescriptorDto decodeFromWire(int pos, Buffer buffer) {

        int nameSize = buffer.getInt(pos);
        int fileNameSize = buffer.getInt(pos + 4);
        int bufferSize = buffer.getInt(pos + 8);

        int start = pos + 12;
        int end = pos + 12 + nameSize;
        String userName = buffer.getString(start, end);

        start = end;
        end = start + fileNameSize;

        String fileName = buffer.getString(start, end);

        start = end;
        end = start + bufferSize;

        Buffer buffer1 = buffer.getBuffer(start, end);

        FileDescriptorDto fileUploadEntryDto = new FileDescriptorDto();
        fileUploadEntryDto.setUserName(userName);
        fileUploadEntryDto.setFileNameWithExtension(fileName);
        fileUploadEntryDto.setBuffer(buffer1);

        return fileUploadEntryDto;
    }

    @Override
    public FileDescriptorDto transform(FileDescriptorDto fileUploadEntryDto) {
        return fileUploadEntryDto;
    }

    @Override
    public String name() {
        return "fileUploadEntryDtoCodec";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
