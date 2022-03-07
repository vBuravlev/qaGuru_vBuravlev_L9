package guru.qa.zip;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.empty;


public class ZIpMultipleFiles {
    ClassLoader classLoader = getClass().getClassLoader();
    String zipFileName = "multiCompressed.zip";
    String resourcesDirName = "src/test/resources/";

    @Test
    void zipFileCreate() throws IOException {
        //Получение списка файлов в директории
        List<String> srcFiles = FileOperation.filesToList(resourcesDirName);

        //Проверка, что файлы существуют в директории
        assertThat(srcFiles, is(not(empty())));

        //Создание потока для zip файла
        FileOperation.zipFileCreate(resourcesDirName, zipFileName, srcFiles, classLoader);

        //Сравнение содержимого zip файла и списка файлов в директории
        FileOperation.checkZipFile(resourcesDirName, zipFileName, srcFiles);

        //Удаление файла после прохождения теста
        FileOperation.deleteFileInDir(resourcesDirName, zipFileName);
    }


}
