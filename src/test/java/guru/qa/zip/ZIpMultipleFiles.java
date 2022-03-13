package guru.qa.zip;

import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static guru.qa.zip.FileOperation.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.empty;


public class ZIpMultipleFiles {
    ClassLoader classLoader = getClass().getClassLoader();
    String zipFileName = "multiCompressed.zip";
    String resourcesDirName = "src/test/resources/";
    String expectedTextPdf = "Икс-Панамас";
    String expectedTextXls = "Пример";
    String expectedTextCsv = "Series_reference";
    int xlsSheetNumber = 0;
    int xlsRowNumber = 1;
    int xlsCellNumber = 1;
    int csvRowNumber = 0;

    @Test
    void zipFileCreate() throws Exception {
        //Получение списка файлов в директории
        List<String> srcFiles = FileOperation.filesToList(resourcesDirName);

        //Проверка, что файлы существуют в директории
        assertThat(srcFiles, is(not(empty())));

        //Удаление, если файл с архивом уже существует
        if(Files.exists(Paths.get(resourcesDirName+zipFileName))) {
            FileOperation.deleteFileInDir(resourcesDirName, zipFileName);
        }

        //Создание потока для zip файла
        FileOperation.zipFileCreate(resourcesDirName, zipFileName, srcFiles, classLoader);

        //Сравнение содержимого zip файла и списка файлов в директории
        FileOperation.checkZipFile(resourcesDirName, zipFileName, srcFiles);

        //Проверка содержимого файлов
        try (ZipFile zipFile = new ZipFile(resourcesDirName + zipFileName)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String fileExtension = FilenameUtils.getExtension(entry.getName());
                    if (fileExtension.equals("pdf")) {
                        parsePdf(zipFile.getInputStream(entry), expectedTextPdf);
                    } else if (fileExtension.equals("xls") | fileExtension.equals("xlsx")) {
                        parseXls(zipFile.getInputStream(entry), expectedTextXls, xlsSheetNumber, xlsRowNumber, xlsCellNumber);
                    } else if (fileExtension.equals("csv")) {
                        parseCsv(zipFile.getInputStream(entry), expectedTextCsv, csvRowNumber);
                }

            }
        }

        //Удаление файла после прохождения теста
       FileOperation.deleteFileInDir(resourcesDirName, zipFileName);
    }

}
