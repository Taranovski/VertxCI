package vertx.example.verticle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Alyx on 09.08.2015.
 */
public class FileSystemCheck {

    private static final long ONE_THOUTHAND_MEGABYTES = 1024 * 1024 * 1024;

    private String message;
    private boolean successful;

    private FileSystemCheck(boolean checkSuccess, String message) {
        this.successful = checkSuccess;
        this.message = message;
    }

    public static FileSystemCheck makeSuperficialCheck(String rootPath) {
        Path path = Paths.get(rootPath);
        long freeSpace = path.toFile().getFreeSpace();
        long totalSpace = path.toFile().getTotalSpace();

        if (freeSpace < ONE_THOUTHAND_MEGABYTES) {
            return new FileSystemCheck(true, "error, free space is less than 1000 Mb");
        } else {
            long freeSpacePercentage = freeSpace * 100 / totalSpace;

            if (freeSpacePercentage > 10) {
                return new FileSystemCheck(true, "ok, free space is sufficient");
            } else {
                return new FileSystemCheck(true, "warning, free space is less that 10%");
            }
        }
    }

    public static FileSystemCheck makeFullCheck(String rootPath) {

        boolean deepCheckSuccessful = checkFileSystem(rootPath);

        Path path = Paths.get(rootPath);
        long freeSpace = path.toFile().getFreeSpace();
        long totalSpace = path.toFile().getTotalSpace();

        if (freeSpace < ONE_THOUTHAND_MEGABYTES) {
            return new FileSystemCheck(deepCheckSuccessful, "error, free space is less than 1000 Mb");
        } else {
            long freeSpacePercentage = freeSpace * 100 / totalSpace;

            if (freeSpacePercentage > 10) {
                return new FileSystemCheck(deepCheckSuccessful, "ok, free space is sufficient");
            } else {
                return new FileSystemCheck(deepCheckSuccessful, "warning, free space is less that 10%");
            }
        }
    }

    private static boolean checkFileSystem(String rootPath) {
        String path = rootPath + FileSystems.getDefault().getSeparator() + "fileSystemCheckFile.txt";

        int limit = 10000;
        int flushLimit = 100;
        boolean success = true;

        Path realPath = Paths.get(path);
        try {
            if (Files.exists(realPath)) {
                Files.delete(realPath);
            }

            Files.createFile(realPath);
            BufferedWriter bufferedWriter = Files.newBufferedWriter(realPath);

            for (int i = 0; i < limit; i++) {
                bufferedWriter.write(i);
                if (i % flushLimit == 0) {
                    bufferedWriter.flush();
                }
            }
            bufferedWriter.flush();
            bufferedWriter.close();

            BufferedReader bufferedReader = Files.newBufferedReader(realPath);

            for (int i = 0; i < limit; i++) {
                int read = bufferedReader.read();
                if (i != read) {
                    success = false;
                    break;
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            success = false;
        }

        return success;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccessful() {
        return successful;
    }

}
