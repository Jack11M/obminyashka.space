package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.ImageDto;
import com.hillel.items_exchange.model.BaseEntity;
import com.hillel.items_exchange.model.Product;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.service.ImageService;
import com.hillel.items_exchange.service.ProductService;
import com.hillel.items_exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ImageController {
    private static final Set<String> SUPPORTED_TYPES = Set.of(IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE);
    private final ImageService imageService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping(value = "/{product_id}/resource")
    public ResponseEntity<List<byte[]>> getImagesResource(@PathVariable("product_id")
                                                                 @PositiveOrZero(message = "{invalid.id}") long id) {
        List<byte[]> imagesResource = imageService.getImagesResourceByProductId(id);
        return imagesResource.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(imagesResource, HttpStatus.OK);
    }

    @GetMapping("/{product_id}")
    public ResponseEntity<List<ImageDto>> getByProductId(@PathVariable("product_id")
                                             @PositiveOrZero(message = "{invalid.id}") long id) {
        List<ImageDto> images = imageService.getByProductId(id);
        return images.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(images, HttpStatus.OK);
    }

    @PostMapping(value = "/{product_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpStatus> saveImages(@PathVariable("product_id")
                                                     @PositiveOrZero(message = "{invalid.id}") long productId,
                                                 @RequestParam(value = "files") @Size(min = 1, max = 10) List<MultipartFile> images) {

        boolean isAllSupportedTypesReceived = images.parallelStream()
                .map(MultipartFile::getContentType)
                .allMatch(SUPPORTED_TYPES::contains);
        if (!isAllSupportedTypesReceived) {
            return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }

        try {
            Product productToSaveImages = productService.findById(productId).orElseThrow(ClassNotFoundException::new);
            List<byte[]> compressedImages = imageService.compressImages(images);
            imageService.saveToProduct(productToSaveImages, compressedImages);
        } catch (ClassNotFoundException e) {
            log.warn("Product not found for id {}", productId);
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } catch (IOException e) {
            log.error("There was an error during extraction of gained images!");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{advertisement_id}")
    public ResponseEntity<HttpStatus> deleteImages(@PathVariable("advertisement_id")
                                                       @PositiveOrZero(message = "{invalid.id}") long advertisementId,
                                                   @RequestParam("ids") List<Long> imageIdList, Principal principal) {

        if (!isUserOwnsSelectedAdvertisement(advertisementId, principal)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        imageService.removeById(imageIdList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean isUserOwnsSelectedAdvertisement(long advertisementId, Principal principal) {
        return userService.findByUsernameOrEmail(principal.getName())
                .map(User::getAdvertisements).stream()
                .flatMap(Collection::parallelStream)
                .map(BaseEntity::getId)
                .anyMatch(Predicate.isEqual(advertisementId));
    }
}