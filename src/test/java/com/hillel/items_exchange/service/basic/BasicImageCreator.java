package com.hillel.items_exchange.service.basic;

import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;

@NoArgsConstructor
public class BasicImageCreator {
    private static final String IMAGE_NAME = "test-image";

    protected MockMultipartFile getImageBytes(MediaType type) throws IOException {
        String imageFileName = IMAGE_NAME + "." + type.getSubtype();
        InputStream file = getClass().getResourceAsStream("/image/" + imageFileName);
        return new MockMultipartFile(imageFileName, imageFileName, type.toString(), file.readAllBytes());
    }
}
