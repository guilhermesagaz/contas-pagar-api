package com.br.contasapagar.shared.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public final class FileUtil {

    private static final String PROP_JAVA_TMP_DIR = System.getProperty("java.io.tmpdir");
    private static final String CSV_TYPE = "text/csv";

    private FileUtil() {
    }

    public static File convertToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(Paths.get(PROP_JAVA_TMP_DIR, multipartFile.getOriginalFilename()).toString());
        multipartFile.transferTo(file);

        return file;
    }

    public static boolean isNotCsv(MultipartFile multipartFile) {
        return !CSV_TYPE.equals(multipartFile.getContentType());
    }

    private static InputStream getInputStreamFromPath(String path) throws IOException {
        Resource resource = new ClassPathResource(path);

        return resource.getInputStream();
    }

    public static String getPathFromTempFile(String path, String fileName) throws IOException {
        InputStream inputStream = getInputStreamFromPath(path);

        Path tempFile = Files.createTempFile(fileName, ".tmp");
        Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

        return tempFile.toAbsolutePath().toString();
    }
}
