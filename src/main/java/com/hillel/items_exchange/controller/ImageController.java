package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.dto.ImageDto;
import com.hillel.items_exchange.exception.UnsupportedMediaTypeException;
import com.hillel.items_exchange.model.BaseEntity;
import com.hillel.items_exchange.model.Product;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.service.ImageService;
import com.hillel.items_exchange.service.ProductService;
import com.hillel.items_exchange.service.UserService;
import io.swagger.annotations.ApiOperation;
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
import java.util.function.Predicate;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ImageController {
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
    @ApiOperation(value = "Save and compress up to 10 images to existed product by it's ID")
    public ResponseEntity<String> saveImages(@PathVariable("product_id")
                                                     @PositiveOrZero(message = "{invalid.id}") long productId,
                                                 @RequestParam(value = "files") @Size(min = 1, max = 10) List<MultipartFile> images) {

        try {
            Product productToSaveImages = productService.findById(productId).orElseThrow(ClassNotFoundException::new);
            List<byte[]> compressedImages = imageService.compress(images);
            imageService.saveToProduct(productToSaveImages, compressedImages);
        } catch (ClassNotFoundException e) {
            final String format = String.format("Product not found for ID=%s", productId);
            log.warn(format, e);
            return new ResponseEntity<>(format, HttpStatus.NOT_ACCEPTABLE);
        } catch (IOException e) {
            final String msg = "There was an error during extraction of gained images!";
            log.error(msg, e);
            return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnsupportedMediaTypeException e) {
            log.warn(e.getLocalizedMessage(), e);
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
        return new ResponseEntity<>("Images saved successfully", HttpStatus.OK);
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