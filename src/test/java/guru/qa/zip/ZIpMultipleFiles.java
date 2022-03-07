package guru.qa.zip;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ZIpMultipleFiles {
    ClassLoader classLoader = getClass().getClassLoader();
    String zipFileName = "multiCompressed.zip";
    String resourcesDirName = "src/test/resources/";

    @Test
    void zipFileCreate() throws IOException {
        //Создание списка файлов в директории
        List<String> srcFiles = FilesToList.filesToList(resourcesDirName);

        //Проверка, что файлы существуют в дирректории
        assertThat(srcFiles, is(not(empty())));

        //Создание потока для zip файла

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

            //Сравнение содержимого zip файла и списка файлов в каталоге
            ZipFile zf = new ZipFile(resourcesDirName + zipFileName);
            Enumeration en = zf.entries();
            List<String> list = Collections.list(en);
            assertEquals(list.toString(), srcFiles.toString());
            zf.close();

            //Удаление файла после прохождения теста
            Files.delete(Paths.get(resourcesDirName + zipFileName));

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

    }


}
