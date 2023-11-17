package com.tms.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
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

@RestController
@RequestMapping("/file")
public class FileController {

    private final Path ROOT_FILE_PATH = Paths.get("checks/2-Oleg-Ivanov");

    @PostMapping("/upload")
    public ResponseEntity<HttpStatus> upload(@RequestParam("fileKey") MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.ROOT_FILE_PATH.resolve(file.getOriginalFilename()));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IOException e) {
            System.out.println(e);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Path path = ROOT_FILE_PATH.resolve(filename);
        try {
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() || resource.isReadable()) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<ArrayList<String>> getFiles() {
        try {
            ArrayList<String> filenames = (ArrayList<String>) Files.walk(this.ROOT_FILE_PATH, 1)
                    .filter(path -> !path.equals(this.ROOT_FILE_PATH))
                    .map(Path::toString)
                    .map(line -> line.substring(5))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(filenames, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @Tag(name = "create-tag")
    @DeleteMapping("/{filename}")
    public ResponseEntity<HttpStatus> deleteFile(@PathVariable String filename) {
        Path path = ROOT_FILE_PATH.resolve(filename);

        File file = new File(path.toString());
        if (file.delete()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
