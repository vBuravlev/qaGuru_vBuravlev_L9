package guru.qa.zip;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileOperation {

    public static List<String> filesToList(String resourcesDirName) {
        try {
            return Files.walk(Paths.get(resourcesDirName))
                    .filter(Files::isRegularFile)
                    .map(x -> x.getFileName())
                    .map(x -> x.toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void checkZipFile(String resourcesDirName, String zipFileName, List<String> srcFiles) {
        try {
            ZipFile zf = new ZipFile(resourcesDirName + zipFileName);
            Enumeration en = zf.entries();
            List<String> list = Collections.list(en);
            assertEquals(list.toString(), srcFiles.toString());
            zf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFileInDir(String resourcesDirName, String zipFileName) {
        try {
            Files.delete(Paths.get(resourcesDirName + zipFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void zipFileCreate(String resourcesDirName, String zipFileName, List<String> srcFiles, ClassLoader classLoader) throws IOException {
        try {
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(resourcesDirName + zipFileName));
            zipOut.setLevel(Deflater.BEST_SPEED);
            //Добавление файлов из директории в zip файл
            for (String srcFile : srcFiles) {
                File fileToZip = new File(classLoader.getResource(srcFile).getFile());
                FileInputStream fis = new FileInputStream(fileToZip);
                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                fis.close();
            }
            zipOut.close();
        } finally {
        }
    }

}
