package space.obminyashka.items_exchange.rest.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;
import space.obminyashka.items_exchange.repository.enums.AgeRange;
import space.obminyashka.items_exchange.repository.enums.Gender;
import space.obminyashka.items_exchange.repository.enums.Season;
import space.obminyashka.items_exchange.repository.enums.Size;
import space.obminyashka.items_exchange.repository.model.QAdvertisement;
import space.obminyashka.items_exchange.rest.request.predicate.QPredicate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ValidationMessage.INVALID_SIZE_COMBINATION;

@Data
@Accessors(chain = true)
public class AdvertisementFilterRequest {
    @PositiveOrZero
    @Schema(description = "Results page you want to retrieve (0..N)", defaultValue = "0")
    private int page = 0;

    @PositiveOrZero
    @Schema(description = "Number of records per page", defaultValue = "12")
    private int size = 12;

    @Schema(description = "Set true if you need random advertisement", defaultValue = "false")
    private boolean enableRandom = false;

    @Schema(description = "Keyword for search", minLength = 3)
    private String keyword;

    @Schema(description = "Category ID for advertisements filtering. See the full list of Categories in: /api/v1/category/all", example = "1")
    private Long categoryId;

    @ArraySchema(arraySchema = @Schema(description = "Subcategories ID for advertisements filtering. Should belong to passed Category ID", example = "[1, 17]"))
    private List<Long> subcategoriesIdValues = new ArrayList<>();

    @Schema(description = "ID of excluded advertisement", example = "f587e494-ace2-44f6-b20d-38d43e9bc060")
    private UUID excludeAdvertisementId;

    @Schema(description = "Location ID for advertisements filtering", example = "842f9ab1-95e8-4c81-a49b-fa4f6d0c3a10")
    private UUID locationId;

    @Schema(description = "Gender of a child for advertisements filtering")
    private Gender gender;

    @ArraySchema(arraySchema = @Schema(description = "Multiple ages of a child for advertisements filtering"))
    private Set<AgeRange> age = new HashSet<>();

    @ArraySchema(arraySchema = @Schema(description = "Multiple clothes sizes for advertisements filtering. Applicable ONLY when the Category ID is 1 (clothing)"))
    private Set<Size.Clothing> clothingSizes = new HashSet<>();

    @ArraySchema(arraySchema = @Schema(description = "Multiple shoe sizes for advertisements filtering. Applicable ONLY when the Category ID is 2 (shoes)"))
    private Set<Size.Shoes> shoesSizes = new HashSet<>();

    @ArraySchema(arraySchema = @Schema(description = "Clothing or Shoes season for advertisements filtering"))
    private Set<Season> season = new HashSet<>();

    @JsonCreator
    public void setShoesSizes(Set<Double> value) {
        shoesSizes = value.stream().map(Size.Shoes::fromValue).collect(Collectors.toSet());
    }

    @JsonCreator
    public void setClothingSizes(Set<String> value) {
        clothingSizes = value.stream().map(Size.Clothing::fromValue).collect(Collectors.toSet());
    }

    public void setAge(Set<String> value) {
        age = value.stream().map(AgeRange::fromValue).collect(Collectors.toSet());
    }

    @SuppressWarnings("unused")
    @AssertFalse(message = "{" + INVALID_SIZE_COMBINATION + "}")
    private boolean isValidCategorySizes() {
        return CollectionUtils.isNotEmpty(clothingSizes) &&
                CollectionUtils.isNotEmpty(shoesSizes);
    }

    public Predicate toPredicate() {

        return QPredicate.builder()
                .add(gender, QAdvertisement.advertisement.gender::eq)
                .add(excludeAdvertisementId, QAdvertisement.advertisement.id::ne)
                .add(locationId, QAdvertisement.advertisement.location.id::eq)
                .add(season, QAdvertisement.advertisement.season::in)
                .add(extractClothingSizeRanges(clothingSizes), QAdvertisement.advertisement.size::in)
                .add(extractShoesSizeLengths(shoesSizes), QAdvertisement.advertisement.size::in)
                .add(age, QAdvertisement.advertisement.age::in)
                .add(subcategoriesIdValues, QAdvertisement.advertisement.subcategory.id::in)
                .add(categoryId, QAdvertisement.advertisement.subcategory.category.id::eq)
                .add(keyword, this::createKeywordCondition)
                .buildAnd();
    }

    private BooleanExpression createKeywordCondition(String keyword) {
        keyword = keyword.trim();
        String[] keywords = keyword.split("\\s+");

        BooleanExpression keywordCondition = Expressions.asBoolean(false).isTrue();
        for (String kw : keywords) {
            keywordCondition = keywordCondition.or(QAdvertisement.advertisement.topic.likeIgnoreCase("%" + kw + "%"));
            keywordCondition = keywordCondition.or(QAdvertisement.advertisement.description.likeIgnoreCase("%" + kw + "%"));
        }

        return keywordCondition;
    }

    private Set<String> extractClothingSizeRanges(Set<Size.Clothing> clothingSizes) {
        return clothingSizes.stream()
                .map(Size.Clothing::getRange)
                .collect(Collectors.toSet());
    }

    private Set<String> extractShoesSizeLengths(Set<Size.Shoes> shoesSizes) {
        return shoesSizes.stream().map(Size.Shoes::getLength)
                .map(String::valueOf)
                .collect(Collectors.toSet());
    }
}
