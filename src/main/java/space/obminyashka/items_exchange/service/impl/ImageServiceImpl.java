package space.obminyashka.items_exchange.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import space.obminyashka.items_exchange.repository.ImageRepository;
import space.obminyashka.items_exchange.rest.exception.ElementsNumberExceedException;
import space.obminyashka.items_exchange.rest.response.ImageView;
import space.obminyashka.items_exchange.rest.exception.UnsupportedMediaTypeException;
import space.obminyashka.items_exchange.rest.mapper.ImageMapper;
import space.obminyashka.items_exchange.repository.model.Image;
import space.obminyashka.items_exchange.service.ImageService;
import space.obminyashka.items_exchange.service.util.SupportedMediaTypes;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import jakarta.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLConnection;
import java.util.List;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.awt.Image.SCALE_REPLICATE;
import static java.awt.Image.SCALE_SMOOTH;
import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getParametrizedMessageSource;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ExceptionMessage.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageMapper imageMapper;
    private final ImageRepository imageRepository;
    private final Set<String> supportedTypes = Arrays.stream(SupportedMediaTypes.values())
            .map(SupportedMediaTypes::getMediaType)
            .collect(Collectors.toSet());
    @Value("${app.image.thumbnail.edge.px}")
    private int thumbnailEdge;
    @Value("${max.images.amount}")
    private int maxImagesAmount;

    @Override
    public List<byte[]> getImagesResourceByAdvertisementId(UUID advertisementId) {
        return imageRepository.findByAdvertisementId(advertisementId).stream()
                .map(Image::getResource)
                .toList();
    }

    @Override
    public List<ImageView> getByAdvertisementId(UUID advertisementId) {
        return imageMapper.toDtoList(imageRepository.findByAdvertisementId(advertisementId));
    }

    @SneakyThrows({IOException.class, UnsupportedMediaTypeException.class})
    @Override
    public byte[] compress(MultipartFile image) {
        validateImagesTypes(List.of(image));

        try (InputStream is = new BufferedInputStream(image.getInputStream());
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             BufferedOutputStream bos = new BufferedOutputStream(baos);
             ImageOutputStream ios = ImageIO.createImageOutputStream(bos)) {

            // Getting original file's type from stream instead of the file's name type
            final var contentType = URLConnection.guessContentTypeFromStream(is);
            if (contentType == null || contentType.equals(MediaType.IMAGE_GIF_VALUE)) {
                return image.getBytes();
            }

            ImageWriter writer = ImageIO.getImageWritersByMIMEType(contentType).next();
            writer.setOutput(ios);
            ImageWriteParam param = writer.getDefaultWriteParam();

            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                float quality = 0.30f; //percents
                param.setCompressionQuality(quality);
            }

            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
            writer.write(null, new IIOImage(bufferedImage, null, null), param);
            writer.dispose();

            return baos.toByteArray();
        }
    }

    @Override
    public void saveToAdvertisement(UUID advertisementId, List<MultipartFile> images)
            throws UnsupportedMediaTypeException, ElementsNumberExceedException {
        validateMaxImagesAmount(advertisementId, images.size());

        images.parallelStream()
                .map(this::compress)
                .forEach(compress -> imageRepository.createImage(UUID.randomUUID(), advertisementId, compress));
    }

    @Override
    public boolean existAllById(List<UUID> ids, UUID advertisementId) {
        return imageRepository.existsAllByIdInAndAdvertisementId(ids, advertisementId);
    }

    @Override
    public void removeById(List<UUID> imageIdList) {
        imageRepository.deleteAllByIdIn(imageIdList);
    }

    @Override
    public void removeById(UUID imageId) {
        imageRepository.deleteById(imageId);
    }

    private void validateImagesTypes(List<MultipartFile> images) throws UnsupportedMediaTypeException {
        final Set<String> unsupportedTypes = findUnsupportedType(images);
        if (!unsupportedTypes.isEmpty()) {
            throw new UnsupportedMediaTypeException("Received unsupported image types: " + String.join(" ,", unsupportedTypes));
        }
    }

    private void validateMaxImagesAmount(UUID advertisementId, int addableImagesAmount) throws ElementsNumberExceedException {
        if (countImagesForAdvertisement(advertisementId) + addableImagesAmount > maxImagesAmount) {
            throw new ElementsNumberExceedException(getParametrizedMessageSource(EXCEED_IMAGES_NUMBER, maxImagesAmount));
        }
    }

    private Set<String> findUnsupportedType(List<MultipartFile> images) {
        return images.stream()
                .map(MultipartFile::getContentType)
                .filter(Predicate.not(supportedTypes::contains))
                .collect(Collectors.toSet());
    }

    @SneakyThrows({UnsupportedMediaTypeException.class, IOException.class})
    @Override
    public byte[] scale(MultipartFile image) {
        validateImagesTypes(List.of(image));
        return scale(image.getBytes());
    }

    @Override
    public byte[] scale(byte[] bytes) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            BufferedImage originImage = ImageIO.read(bais);
            if (originImage != null) {
                Dimension newSize = calculatePreferThumbnailSize(
                        new Dimension(originImage.getWidth(), originImage.getHeight()));
                BufferedImage scaledImage = getScaled(newSize, originImage);
                String type = URLConnection.guessContentTypeFromStream(
                        new ByteArrayInputStream(bytes)).replace("image/", "");
                ImageIO.write(scaledImage, type, baos);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            log.error("[ImageServiceImpl] An error occurred while scale an image", e);
            return bytes;
        }
    }

    @Override
    public int countImagesForAdvertisement(UUID id) {
        return imageRepository.countImageByAdvertisementId(id);
    }

    @Override
    public List<UUID> getImagesIdByAdvertisementId(UUID advertisementId) {
        return imageRepository.getImagesIdByAdvertisementId(advertisementId);
    }

    private BufferedImage getScaled(Dimension d, BufferedImage originImage) {
        java.awt.Image scaledInstance = originImage.getScaledInstance(d.width, d.height, SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(d.width, d.height, SCALE_REPLICATE);
        resizedImage.getGraphics().drawImage(scaledInstance, 0, 0, d.width, d.height, Color.WHITE, null);
        return resizedImage;
    }

    private Dimension calculatePreferThumbnailSize(Dimension origin) {
        double tumbWidth = 1.0 * thumbnailEdge / origin.width;
        double tumbHeight = 1.0 * thumbnailEdge / origin.height;
        double ratio = Math.max(tumbWidth, tumbHeight);
        return new Dimension((int) (origin.width * ratio), (int) (origin.height * ratio));
    }
}