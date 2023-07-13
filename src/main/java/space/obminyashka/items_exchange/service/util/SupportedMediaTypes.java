package space.obminyashka.items_exchange.service.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.springframework.http.MediaType.*;

@AllArgsConstructor
public enum SupportedMediaTypes {
    JPEG(IMAGE_JPEG_VALUE),
    JPG("image/jpg"),
    PNG(IMAGE_PNG_VALUE),
    GIF(IMAGE_GIF_VALUE);

    @Getter
    public final String mediaType;
}
