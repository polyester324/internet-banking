package com.tms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.security.filter.JwtAuthenticationFilter;
import com.tms.security.service.SecurityService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = FileController.class)
@AutoConfigureMockMvc(addFilters = false)
class FileControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    JwtAuthenticationFilter jaf;
    @MockBean
    SecurityService securityService;
    static Long clientId = 56L;
    static String user = "56-TESTER-TESTER";
    static String filename = "Test-File-check_43356_20231130_0000-00_1294053b-06f9-4aa6-a729-5812ccb3e072.txt";

    @AfterEach
    void cleanup() throws Exception {
        Files.deleteIfExists(Paths.get("checks", user, filename));
    }
    @Test
    void upload() throws Exception {
        String content = "Hello, World!";
        Path path = Paths.get("checks", user, filename);
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
        }
        MockMultipartFile file = new MockMultipartFile(
                "fileKey",
                filename,
                MediaType.TEXT_PLAIN_VALUE,
                content.getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/file/upload/checks/{user}", user)
                        .file(file))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.get("/file/checks/{user}/{filename}", user, filename))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\""))
                .andExpect(MockMvcResultMatchers.content().string(content));
    }

    @Test
    void getFile() throws Exception {
        Mockito.when(securityService.checkAccessById(clientId)).thenReturn(true);
        Path path = Paths.get("checks", user, filename);

        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        }
        mockMvc.perform(get("/file/checks/{user}/{filename}", user, filename))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\""))
                .andExpect(header().string(HttpHeaders.CONTENT_LENGTH, String.valueOf(Files.size(path))));
    }

    @Test
    void getAllFiles() throws Exception{
        Path filePath1 = Paths.get("checks", user, "fileTest1.txt");
        Path filePath2 = Paths.get("checks", user, "fileTest2.txt");
        Files.createDirectories(filePath1.getParent());
        Files.createDirectories(filePath2.getParent());
        Files.createFile(filePath1);
        Files.createFile(filePath2);

        mockMvc.perform(MockMvcRequestBuilders.get("/file/checks/{user}", user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());

        Files.deleteIfExists(filePath1);
        Files.deleteIfExists(filePath2);
    }

    @Test
    void deleteFile() throws Exception{
        Path filePath = Paths.get("checks", user, filename);
        Files.createDirectories(filePath.getParent());
        Files.createFile(filePath);

        mockMvc.perform(MockMvcRequestBuilders.get("/file/checks/{user}/{filename}", user, filename)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.delete("/file/checks/{user}/{filename}", user, filename)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/file/checks/{user}/{filename}", user, filename)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}