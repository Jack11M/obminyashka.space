package space.obminyashka.items_exchange.rest;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import space.obminyashka.items_exchange.rest.api.ApiKey;
import space.obminyashka.items_exchange.rest.response.ImageView;
import space.obminyashka.items_exchange.rest.exception.ElementsNumberExceedException;
import space.obminyashka.items_exchange.rest.exception.bad_request.IllegalIdentifierException;
import space.obminyashka.items_exchange.rest.exception.IllegalOperationException;
import space.obminyashka.items_exchange.repository.model.Advertisement;
import space.obminyashka.items_exchange.service.AdvertisementService;
import space.obminyashka.items_exchange.service.ImageService;
import space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler;

import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getMessageSource;
import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getParametrizedMessageSource;

@RestController
@Tag(name = "Image")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ImageController {
    private final ImageService imageService;
    private final AdvertisementService advertisementService;

    @Value("${max.images.amount}")
    private int maxImagesAmount;

    @GetMapping(value = ApiKey.IMAGE_RESOURCE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find all byte representation of images for an existed advertisement by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    @ResponseStatus(HttpStatus.OK)
    public List<byte[]> getImagesResource(
            @Parameter(name = "advertisement_id", description = "ID of the Advertisement for getting all the images", required = true)
            @PathVariable("advertisement_id") UUID id) {
        return imageService.getImagesResourceByAdvertisementId(id);
    }

    @GetMapping(value = ApiKey.IMAGE_BY_ADV_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find all images for an existed advertisement by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    @ResponseStatus(HttpStatus.OK)
    public List<ImageView> getByAdvertisementId(
            @Parameter(name = "advertisement_id", description = "ID of the Advertisement for getting all the images representation", required = true)
            @PathVariable("advertisement_id") UUID id) throws EntityNotFoundException {
        List<ImageView> listImagesByAdvertisement = imageService.getByAdvertisementId(id);
        if (listImagesByAdvertisement.isEmpty()) {
            throw new EntityNotFoundException(getMessageSource(ResponseMessagesHandler.ExceptionMessage.ADVERTISEMENT_NOT_EXISTED_ID));
        }
        return listImagesByAdvertisement;
    }

    @GetMapping(value = ApiKey.IMAGE_IN_ADV_COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Count all images for an existed advertisement by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")})
    public ResponseEntity<Integer> countImagesInAdvertisement(
            @Parameter(name = "advertisement_id", description = "ID of the Advertisement for counting the images", required = true)
            @PathVariable("advertisement_id") UUID id) {
        if (!advertisementService.existById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(imageService.countImagesForAdvertisement(id));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @PostMapping(value = ApiKey.IMAGE_BY_ADV_ID, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @Operation(summary = "Compress up to 10 images and add to an existed advertisement by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN"),
            @ApiResponse(responseCode = "404", description = "Advertisement Not Found with such ID"),
            @ApiResponse(responseCode = "406", description = "NOT ACCEPTABLE"),
            @ApiResponse(responseCode = "415", description = "UNSUPPORTED MEDIA TYPE")})
    public ResponseEntity<String> addImagesToAdvertisement(
            @Parameter(name = "advertisement_id", description = "ID of the Advertisement for adding the image(s)", required = true)
            @PathVariable("advertisement_id") UUID advertisementId,
            @Parameter(name = "image", description = "Select the image to Upload", required = true)
            @RequestPart(value = "image") @Size(min = 1, max = 10) List<MultipartFile> images,
            @Parameter(hidden = true) Authentication authentication)
            throws ElementsNumberExceedException, IllegalOperationException {

        if (!advertisementService.existById(advertisementId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND);
        }
        final Advertisement advToSaveImages = advertisementService.findByIdAndOwnerUsername(advertisementId, authentication.getName())
                .orElseThrow(() -> new IllegalOperationException(getMessageSource(ResponseMessagesHandler.ValidationMessage.USER_NOT_OWNER)));
        if (advToSaveImages.getImages().size() + images.size() > maxImagesAmount) {
            throw new ElementsNumberExceedException(
                    getParametrizedMessageSource(ResponseMessagesHandler.ExceptionMessage.EXCEED_IMAGES_NUMBER, maxImagesAmount));
        }
        List<byte[]> compressedImages = images.parallelStream()
                .map(imageService::compress)
                .toList();
        imageService.saveToAdvertisement(advToSaveImages, compressedImages);
        return ResponseEntity.ok("Images added successfully");
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'MODERATOR')")
    @DeleteMapping(ApiKey.IMAGE_BY_ADV_ID)
    @Operation(summary = "Delete images from an advertisement by its ID and images ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN")})
    @ResponseStatus(HttpStatus.OK)
    public void deleteImages(@Parameter(name = "advertisement_id", description = "ID of the Advertisement for delete the image(s)", required = true)
                             @PathVariable("advertisement_id") UUID advertisementId,
                             @Parameter(name = "ids", description = "Input image(s) ID for delete", required = true)
                             @RequestParam("ids") List<UUID> imageIdList,
                             @Parameter(hidden = true) Authentication authentication)
            throws IllegalOperationException, IllegalIdentifierException {

        if (!advertisementService.isUserHasAdvertisementWithId(advertisementId, authentication.getName())) {
            throw new IllegalOperationException(getMessageSource(ResponseMessagesHandler.ValidationMessage.USER_NOT_OWNER));
        }

        if (imageService.existAllById(imageIdList, advertisementId)) {
            imageService.removeById(imageIdList);
        } else {
            imageIdList.removeAll(imageService.getImagesIdByAdvertisementId(advertisementId));
            throw new IllegalIdentifierException(getParametrizedMessageSource(
                    ResponseMessagesHandler.ExceptionMessage.IMAGE_NOT_EXISTED_ID, imageIdList));
        }
    }
}