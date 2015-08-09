package vertx.example.common.dto;

import io.vertx.core.buffer.Buffer;

import java.io.Serializable;

/**
 * Created by Alyx on 09.08.2015.
 */
public class FileDescriptorDto implements Serializable {

    private String userName;
    private String fileNameWithExtension;
    private Buffer buffer;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFileNameWithExtension() {
        return fileNameWithExtension;
    }

    public void setFileNameWithExtension(String fileNameWithExtension) {
        this.fileNameWithExtension = fileNameWithExtension;
    }


    public Buffer getBuffer() {
        return buffer;
    }

    public void setBuffer(Buffer buffer) {
        this.buffer = buffer;
    }

}
