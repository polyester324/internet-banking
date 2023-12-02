package com.tms.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Slf4j
@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/file")
public class FileController {

    private final Path ROOT_FILE_PATH = Paths.get("checks");

    @PostMapping("/upload/checks/{user}")
    public ResponseEntity<HttpStatus> upload(@RequestParam("fileKey") MultipartFile file, @PathVariable("user") String user) {
        try {
            Files.copy(file.getInputStream(), this.ROOT_FILE_PATH.resolve(user + "\\" + file.getOriginalFilename()));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IOException e) {
            log.warn("Method upload went wrong. Exception: ", e);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping("/checks/{user}/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable("user") String user, @PathVariable("filename") String filename) {
        Path path = ROOT_FILE_PATH.resolve(user + "\\" + filename);
        try {
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() || resource.isReadable()) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            }
        } catch (MalformedURLException e) {
            log.warn("Method getFile went wrong. Exception: ", e);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("checks/{user}")
    public ResponseEntity<ArrayList<String>> getAllFiles(@PathVariable("user") String user) {
        try {
            ArrayList<String> filenames = (ArrayList<String>) Files.walk(this.ROOT_FILE_PATH.resolve(user), 1)
                    .filter(path -> !path.equals(this.ROOT_FILE_PATH))
                    .map(Path::toString)
                    .map(line -> line.substring(5))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(filenames, HttpStatus.OK);
        } catch (IOException e) {
            log.warn("Method getAllFiles went wrong. Exception: ", e);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @DeleteMapping("checks/{user}/{filename}")
    public ResponseEntity<HttpStatus> deleteFile(@PathVariable("user") String user, @PathVariable String filename) {
        Path path = ROOT_FILE_PATH.resolve(user + "\\" + filename);

        File file = new File(path.toString());
        if (file.delete()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
