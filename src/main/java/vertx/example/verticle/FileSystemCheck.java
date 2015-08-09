package vertx.example.verticle;

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
        return false;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccessful() {
        return successful;
    }

}
