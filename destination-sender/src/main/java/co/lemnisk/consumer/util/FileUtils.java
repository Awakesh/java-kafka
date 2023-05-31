package co.lemnisk.consumer.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

public  class FileUtils {


    public static void listf(String directoryName, List<File> files) {
        File directory = new File(directoryName);

        File[] fList = directory.listFiles();
        if(fList != null)
            for (File file : fList) {
                if (file.isFile()) {
                    files.add(file);
                } else if (file.isDirectory()) {
                    listf(file.getAbsolutePath(), files);
                }
            }
    }

    public static boolean checkIfFileModifiedInXMinutes(File file,int minuteThreshold){

       long createdTimestamp = file.lastModified();
       long currentTimestamp = System.currentTimeMillis();

       long diff = currentTimestamp - createdTimestamp ;
       long minutesPassed = TimeUnit.MILLISECONDS.toMinutes(diff);

        return minutesPassed < minuteThreshold;

    }

    public static String getCompletedFileNameFromOriginalFile(File file) throws IOException {

        String fileName = file.getName();
        String[] fileNameParts = fileName.split("-");
        String destinationId = fileNameParts[2];
        String sourceId = fileNameParts[3];
        String hostname = fileNameParts[4];

        return "completed" +
                "-" +
                getCreateTime(file) +
                "-" +
                getLastModified(file) +
                "-" +
                destinationId +
                "-" +
                sourceId +
                "-" +
                hostname;

    }

    public static String getCreateTime(File file) throws IOException {
        Path path = Paths.get(file.getPath());
        BasicFileAttributeView basicfile = Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
        BasicFileAttributes attr = basicfile.readAttributes();
        long date = attr.creationTime().toMillis();
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(date);
    }

    public static String getLastModified(File file) throws IOException{

        Path path = Paths.get(file.getPath());
        BasicFileAttributeView basicfile = Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
        BasicFileAttributes attr = basicfile.readAttributes();
        long date = attr.lastModifiedTime().toMillis();
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(date);

    }

    public static void createDirectoryIfNotExists(String directory){
        File dir = new File(directory);
        if (!dir.exists()) dir.mkdirs();
    }


    public static void deleteEmptyDirectories(String directory) throws IOException {
        Files.walk(Paths.get(directory))
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .filter(File::isDirectory)
                .forEach(File::delete);
    }


    public static void moveFile(File file, String directory){
        FileUtils.createDirectoryIfNotExists(directory);
        File completedFile = new File(directory + "/" + file.getName());
        file.renameTo(completedFile);
    }


    public static void copyFile(File file, String to){

        createDirectoryIfNotExists(to);

        try {
            Path src = Paths.get(file.toString());
            Path dest = Paths.get(to);
            Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public static boolean compressGzip(File source, String target) throws IOException {

        File output = new File(target);
        output.createNewFile();

        try (GZIPOutputStream gzipOutputStream =
                     new GZIPOutputStream(Files.newOutputStream(Paths.get(target)))) {
            byte[] allBytes = Files.readAllBytes(Paths.get(source.toString()));
            gzipOutputStream.write(allBytes);
            return true;
        }
        catch (Exception e){

            return false;
        }
    }

}
