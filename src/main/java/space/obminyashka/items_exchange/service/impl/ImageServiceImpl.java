package space.obminyashka.items_exchange.service.impl;

import space.obminyashka.items_exchange.dao.ImageRepository;
import space.obminyashka.items_exchange.dto.ImageDto;
import space.obminyashka.items_exchange.exception.UnsupportedMediaTypeException;
import space.obminyashka.items_exchange.model.Advertisement;
import space.obminyashka.items_exchange.model.Image;
import space.obminyashka.items_exchange.service.ImageService;
import space.obminyashka.items_exchange.service.SupportedMediaTypes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.awt.Image.SCALE_REPLICATE;
import static java.awt.Image.SCALE_SMOOTH;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;
    private final Set<String> supportedTypes = Arrays.stream(SupportedMediaTypes.values())
            .map(SupportedMediaTypes::getMediaType)
            .collect(Collectors.toSet());
    @Value("${app.image.thumbnail.edge.px}")
    private int thumbnailEdge;

    @Override
    public List<byte[]> getImagesResourceByAdvertisementId(long advertisementId) {
        return imageRepository.findByAdvertisementId(advertisementId).stream()
                .map(Image::getResource)
                .collect(Collectors.toList());
    }

    @Override
    public List<ImageDto> getByAdvertisementId(long advertisementId) {
        return mapImagesToDto(imageRepository.findByAdvertisementId(advertisementId));
    }

    private List<ImageDto> mapImagesToDto(Iterable<Image> images) {
        return modelMapper.map(images, new TypeToken<List<ImageDto>>() {}.getType());
    }

    @Override
    public List<byte[]> compress(List<MultipartFile> images) throws IOException, UnsupportedMediaTypeException {
        validateImagesTypes(images);

        List<byte[]> compressedImages = new ArrayList<>();
        for (MultipartFile photo : images) {
            compressedImages.add(compress(photo));
        }
        return compressedImages;
    }

    @Override
    public byte[] compress(MultipartFile image) throws IOException, UnsupportedMediaTypeException {
        validateImagesTypes(List.of(image));

        String contentType = image.getContentType();
        if (contentType == null || contentType.equals(MediaType.IMAGE_GIF_VALUE)) {
            return image.getBytes();
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             BufferedOutputStream bos = new BufferedOutputStream(baos);
             ImageOutputStream ios = ImageIO.createImageOutputStream(bos)) {

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
    public void saveToAdvertisement(Advertisement advertisement, List<byte[]> images) {
        List<Image> imagesToSave = images.stream()
                .map(populateNewImage(advertisement))
                .collect(Collectors.toList());
        imageRepository.saveAll(imagesToSave);
    }

    @Override
    public void saveToAdvertisement(Advertisement advertisement, byte[] image) {
        Image toSave = populateNewImage(advertisement).apply(image);
        imageRepository.save(toSave);
    }

    private Function<byte[], Image> populateNewImage(Advertisement ownerAdvertisement) {
        return bytes -> new Image(0, bytes, ownerAdvertisement);
    }

    @Override
    public boolean isExistsById(long imageId) {
        return imageRepository.existsById(imageId);
    }

    @Override
    public boolean existAllById(List<Long> ids, long advertisementId) {
        return imageRepository.existsAllByIdInAndAdvertisement_Id(ids, advertisementId);
    }

    @Override
    public void removeById(List<Long> imageIdList) {
        imageRepository.deleteAllByIdIn(imageIdList);
    }

    @Override
    public void removeById(long imageId) {
        imageRepository.deleteById(imageId);
    }

    private void validateImagesTypes(List<MultipartFile> images) throws UnsupportedMediaTypeException {
        final Set<String> unsupportedTypes = findUnsupportedType(images);
        if (!unsupportedTypes.isEmpty()) {
            throw new UnsupportedMediaTypeException("Received unsupported image types: " + String.join(" ,", unsupportedTypes));
        }
    }

    private Set<String> findUnsupportedType(List<MultipartFile> images) {
        return images.stream()
                .map(MultipartFile::getContentType)
                .filter(Predicate.not(supportedTypes::contains))
                .collect(Collectors.toSet());
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
                String type = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(bytes)).replace("image/", "");
                ImageIO.write(scaledImage, type, baos);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            log.error("An error occurred while scale an image", e);
            return bytes;
        }
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