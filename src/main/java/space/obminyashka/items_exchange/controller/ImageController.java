package space.obminyashka.items_exchange.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import space.obminyashka.items_exchange.dto.ImageDto;
import space.obminyashka.items_exchange.exception.ElementsNumberExceedException;
import space.obminyashka.items_exchange.exception.IllegalIdentifierException;
import space.obminyashka.items_exchange.exception.IllegalOperationException;
import space.obminyashka.items_exchange.exception.UnsupportedMediaTypeException;
import space.obminyashka.items_exchange.model.Advertisement;
import space.obminyashka.items_exchange.model.Image;
import space.obminyashka.items_exchange.service.AdvertisementService;
import space.obminyashka.items_exchange.service.ImageService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getParametrizedMessageSource;

@RestController
@RequestMapping("/image")
@Api(tags = "Image")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ImageController {
    private final ImageService imageService;
    private final AdvertisementService advertisementService;

    @Value("${max.images.amount}")
    private int maxImagesAmount;

    @GetMapping(value = "/{advertisement_id}/resource")
    @ApiOperation(value = "Find all byte representation of images for an existed advertisement by its ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<List<byte[]>> getImagesResource(@PathVariable("advertisement_id")
                                                                 @PositiveOrZero(message = "{invalid.id}") long id) {
        List<byte[]> imagesResource = imageService.getImagesResourceByAdvertisementId(id);
        return imagesResource.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(imagesResource, HttpStatus.OK);
    }

    @GetMapping("/{advertisement_id}")
    @ApiOperation(value = "Find all images for an existed advertisement by its ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<List<ImageDto>> getByAdvertisementId(@PathVariable("advertisement_id")
                                             @PositiveOrZero(message = "{invalid.id}") long id) {
        List<ImageDto> images = imageService.getByAdvertisementId(id);
        return images.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(images, HttpStatus.OK);
    }

    @PostMapping(value = "/{advertisement_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Save and compress up to 10 images to an existed advertisement by its ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN"),
            @ApiResponse(code = 404, message = "Advertisement Not Found with such ID"),
            @ApiResponse(code = 406, message = "NOT ACCEPTABLE"),
            @ApiResponse(code = 415, message = "UNSUPPORTED MEDIA TYPE")})
    public ResponseEntity<String> saveImages(@ApiParam(value = "ID of the Advertisement for adding the image(s)", required = true)
                                             @PathVariable("advertisement_id") @PositiveOrZero(message = "{invalid.id}") long advertisementId,
                                             @ApiParam(value = "Select the image to Upload", required = true)
                                             @RequestPart(value = "image") @Size(min = 1, max = 10) List<MultipartFile> images,
                                             @ApiIgnore Principal principal)
            throws ElementsNumberExceedException, IllegalOperationException, UnsupportedMediaTypeException {

        if (!advertisementService.existById(advertisementId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND);
        }
        try {
            final Advertisement advToSaveImages = advertisementService.findByIdAndOwnerUsername(advertisementId, principal.getName())
                    .orElseThrow(() -> new IllegalOperationException(getMessageSource("user.not-owner")));
            if (advToSaveImages.getImages().size() + images.size() > maxImagesAmount) {
                throw new ElementsNumberExceedException(
                        getParametrizedMessageSource("exception.exceed.images.number", maxImagesAmount));
            }
            List<byte[]> compressedImages = imageService.compress(images);
            imageService.saveToAdvertisement(advToSaveImages, compressedImages);
        } catch (IOException e) {
            final var msg = "There was an error during extraction of gained images!";
            log.error(msg, e);
            return new ResponseEntity<>(msg, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>("Images saved successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{advertisement_id}")
    @ApiOperation(value = "Delete images from an advertisement by its ID and images ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.OK)
    public void deleteImages(@ApiParam(value = "ID of the Advertisement for delete the image(s)", required = true)
                             @PathVariable("advertisement_id") @PositiveOrZero(message = "{invalid.id}") long advertisementId,
                             @ApiParam(value = "Input image(s) ID for delete", required = true)
                             @RequestParam("ids") List<Long> imageIdList,
                             @ApiIgnore Principal principal)
            throws IllegalOperationException, IllegalIdentifierException {

        final var existedAdvertisement = advertisementService.findByIdAndOwnerUsername(advertisementId, principal.getName())
                .orElseThrow(() -> new IllegalOperationException(getMessageSource("user.not-owner")));

        final var isAllImageExistInAdvertisement = imageService.existAllById(imageIdList, advertisementId);
        if (isAllImageExistInAdvertisement) {
            imageService.removeById(imageIdList);
        } else {
            final var existedImageIds = existedAdvertisement.getImages().stream()
                    .map(Image::getId)
                    .toList();
            imageIdList.removeAll(existedImageIds);
            throw new IllegalIdentifierException(getParametrizedMessageSource("exception.image.not-existed-id", imageIdList));
        }
    }
}
