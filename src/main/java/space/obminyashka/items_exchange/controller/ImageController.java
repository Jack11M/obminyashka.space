package space.obminyashka.items_exchange.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import space.obminyashka.items_exchange.api.ApiKey;
import space.obminyashka.items_exchange.dto.ImageDto;
import space.obminyashka.items_exchange.exception.ElementsNumberExceedException;
import space.obminyashka.items_exchange.exception.IllegalIdentifierException;
import space.obminyashka.items_exchange.exception.IllegalOperationException;
import space.obminyashka.items_exchange.model.Advertisement;
import space.obminyashka.items_exchange.model.Image;
import space.obminyashka.items_exchange.service.AdvertisementService;
import space.obminyashka.items_exchange.service.ImageService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getParametrizedMessageSource;

@RestController
@Api(tags = "Image")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ImageController {
    private final ImageService imageService;
    private final AdvertisementService advertisementService;

    @Value("${max.images.amount}")
    private int maxImagesAmount;

    @GetMapping(value = ApiKey.IMAGE_RESOURCE)
    @ApiOperation(value = "Find all byte representation of images for an existed advertisement by its ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    @ResponseStatus(HttpStatus.OK)
    public List<byte[]> getImagesResource(
            @ApiParam(value = "ID of the Advertisement for getting all the images", required = true)
            @PathVariable("advertisement_id") UUID id) {
        return imageService.getImagesResourceByAdvertisementId(id);
    }

    @GetMapping(ApiKey.IMAGE_BY_ADV_ID)
    @ApiOperation(value = "Find all images for an existed advertisement by its ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    @ResponseStatus(HttpStatus.OK)
    public List<ImageDto> getByAdvertisementId(
            @ApiParam(value = "ID of the Advertisement for getting all the images representation", required = true)
            @PathVariable("advertisement_id") UUID id) {
        return imageService.getByAdvertisementId(id);
    }

    @GetMapping(ApiKey.IMAGE_IN_ADV_COUNT)
    @ApiOperation(value = "Count all images for an existed advertisement by its ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 404, message = "NOT FOUND")})
    public ResponseEntity<Integer> countImagesInAdvertisement(
            @ApiParam(value = "ID of the Advertisement for counting the images", required = true)
            @PathVariable("advertisement_id") UUID id) {
        if (!advertisementService.existById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(imageService.countImagesForAdvertisement(id));
    }

    @PostMapping(value = ApiKey.IMAGE_BY_ADV_ID, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Compress up to 10 images and add to an existed advertisement by its ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN"),
            @ApiResponse(code = 404, message = "Advertisement Not Found with such ID"),
            @ApiResponse(code = 406, message = "NOT ACCEPTABLE"),
            @ApiResponse(code = 415, message = "UNSUPPORTED MEDIA TYPE")})
    public ResponseEntity<String> addImagesToAdvertisement(
            @ApiParam(value = "ID of the Advertisement for adding the image(s)", required = true)
            @PathVariable("advertisement_id") UUID advertisementId,
            @ApiParam(value = "Select the image to Upload", required = true)
            @RequestPart(value = "image") @Size(min = 1, max = 10) List<MultipartFile> images,
            @ApiIgnore Authentication authentication)
            throws ElementsNumberExceedException, IllegalOperationException {

        if (!advertisementService.existById(advertisementId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND);
        }
        final Advertisement advToSaveImages = advertisementService.findByIdAndOwnerUsername(advertisementId, authentication.getName())
                .orElseThrow(() -> new IllegalOperationException(getMessageSource("user.not-owner")));
        if (advToSaveImages.getImages().size() + images.size() > maxImagesAmount) {
            throw new ElementsNumberExceedException(
                    getParametrizedMessageSource("exception.exceed.images.number", maxImagesAmount));
        }
        List<byte[]> compressedImages = images.parallelStream()
                .map(imageService::compress)
                .toList();
        imageService.saveToAdvertisement(advToSaveImages, compressedImages);
        return ResponseEntity.ok("Images added successfully");
    }

    @DeleteMapping(ApiKey.IMAGE_BY_ADV_ID)
    @ApiOperation(value = "Delete images from an advertisement by its ID and images ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "BAD REQUEST"),
            @ApiResponse(code = 403, message = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.OK)
    public void deleteImages(@ApiParam(value = "ID of the Advertisement for delete the image(s)", required = true)
                             @PathVariable("advertisement_id") UUID advertisementId,
                             @ApiParam(value = "Input image(s) ID for delete", required = true)
                             @RequestParam("ids") List<UUID> imageIdList,
                             @ApiIgnore Authentication authentication)
            throws IllegalOperationException, IllegalIdentifierException {

        final var existedAdvertisement = advertisementService.findByIdAndOwnerUsername(advertisementId, authentication.getName())
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
