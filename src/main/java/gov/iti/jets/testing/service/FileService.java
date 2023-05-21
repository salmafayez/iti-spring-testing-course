package gov.iti.jets.testing.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.Duration;
import java.util.Collections;

public interface FileService {
    public String saveFile(File file, String URL);
}
