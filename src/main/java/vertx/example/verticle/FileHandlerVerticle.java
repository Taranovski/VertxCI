package vertx.example.verticle;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.file.FileSystem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import vertx.example.common.AbstractExtendedVerticle;
import vertx.example.common.FileHandlerStatusCheck;
import vertx.example.common.StatusCheck;
import vertx.example.common.dto.FileDescriptorDto;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;

/**
 * Created by Alyx on 04.08.2015.
 */
public class FileHandlerVerticle extends AbstractExtendedVerticle {

    private static final String FILE_HANDLER_VERTICLE_NAME = "FileHandlerVerticle";

    private String uploadFileAction = "uploadFile";
    private String downloadFileAction = "downloadFile";

    private FileSystem vertxFileSystem;
    private String rootPath;
    private String delimiter;

    private boolean initialized;

    @Override
    public void start() throws Exception {
        super.start();
        initializeFileSystem();
        bus.consumer(uploadFileAction, (Message<FileDescriptorDto> message) -> {
            FileDescriptorDto fileUploadEntryDto = message.body();
            String path = getFilePath(fileUploadEntryDto);
            Buffer fileSourceStream = fileUploadEntryDto.getBuffer();
            Path path1 = Paths.get(path);
            if (Files.notExists(path1)) {
                try {
                    Files.createDirectories(path1.getParent());
                    Files.createFile(path1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            vertxFileSystem.writeFile(path, fileSourceStream, (AsyncResult<Void> event) -> {
            });
        });
        bus.consumer(downloadFileAction, (Message<FileDescriptorDto> message) -> {
            FileDescriptorDto fileUploadEntryDto = message.body();
            String path = getFilePath(fileUploadEntryDto);
            Path path1 = Paths.get(path);
            if (Files.exists(path1)) {
                vertxFileSystem.readFile(path, (AsyncResult<Buffer> event) -> {
                    if (event.failed()) {
                        throw new RuntimeException(event.cause());
                    }

                    Buffer buffer = event.result();
                    fileUploadEntryDto.setBuffer(buffer);
                    message.reply(fileUploadEntryDto);
                });
            }
        });
    }

    private String getFilePath(FileDescriptorDto fileUploadEntryDto) {
        String userName = fileUploadEntryDto.getUserName();
        String fileName = fileUploadEntryDto.getFileNameWithExtension();

        String extensionSubFolder = getFileExtension(fileName);
        String fileNameFirstLetterSubfolder = getFileNameFirstLetter(fileName);

        return StringUtils.join(
                new String[]{rootPath, userName, extensionSubFolder, fileNameFirstLetterSubfolder, fileName},
                delimiter);
    }

    private String getFileNameFirstLetter(String fileName) {
        return fileName.substring(0, 1).toLowerCase();
    }

    private String getFileExtension(String fileName) {
        return FilenameUtils.getExtension(fileName);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    @Override
    protected String getVerticalName() {
        return FILE_HANDLER_VERTICLE_NAME;
    }

    @Override
    protected StatusCheck checkVerticle(int level) {
        switch (level) {
            case 1: {
                return new FileHandlerStatusCheck(true, "fileHandler vertice is up and running", level);
            }
            case 2: {
                FileSystemCheck superficialCheck = FileSystemCheck.makeSuperficialCheck(rootPath);
                return new FileHandlerStatusCheck(superficialCheck.isSuccessful(), superficialCheck.getMessage(), level);
            }
            case 3: {
                FileSystemCheck fullCheck = FileSystemCheck.makeFullCheck(rootPath);
                return new FileHandlerStatusCheck(fullCheck.isSuccessful(), fullCheck.getMessage(), level);
            }
            default: {
                return new FileHandlerStatusCheck(false, "unrecognized status check level", level);
            }
        }
    }

    private void initializeFileSystem() {
        if (initialized) {
            return;
        }

        vertxFileSystem = vertx.fileSystem();

        try {
            PropertyResourceBundle propertyResourceBundle = new PropertyResourceBundle(
                    FileHandlerVerticle.class.getResourceAsStream("/fileHandlerProperties.properties"));
            rootPath = propertyResourceBundle.getString("fileHandler.rootPath");
            delimiter = propertyResourceBundle.getString("fileHandler.delimiter");
        } catch (IOException | MissingResourceException e) {
            throw new RuntimeException(e);
        }

        if (rootPath == null) {
            Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();
            rootPath = rootDirectories.iterator().next().toString();
        }

        if (delimiter == null) {
            delimiter = FileSystems.getDefault().getSeparator();
        }
        initialized = true;
    }
}
