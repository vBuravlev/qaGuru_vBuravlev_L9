package guru.qa.zip;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;


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
        FileOutputStream fos = new FileOutputStream(resourcesDirName + zipFileName);
        ZipOutputStream zipOut = new ZipOutputStream(fos);

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
        fos.close();

        //Получение списка файлов с zip файлом
        List<String> srcFilesZip = FilesToList.filesToList(resourcesDirName);

        //Проверка, что файл zip существуют в дирректории
        assertThat(srcFilesZip, hasItem(zipFileName));

        //Удаление файла после прохождения теста
        Files.delete(Paths.get(resourcesDirName + zipFileName));

        //Получение списка файлов без zip файла
        List<String> srcFilesNotZip = FilesToList.filesToList(resourcesDirName);

        //Проверка, что файл zip отсутствует в дирректории
        assertThat(srcFilesNotZip, hasSize(srcFilesZip.size()-1));
    }


}
