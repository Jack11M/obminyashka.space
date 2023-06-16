package space.obminyashka.items_exchange.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.*;
import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ValidationMessage.*;

public interface Size {

    @RequiredArgsConstructor
    @Getter
    enum Clothing {
        FORTY_SIX_2_FIFTY("46 - 50"),
        FIFTY_ONE_2_FIFTY_SIX("51 - 56"),
        FIFTY_SEVEN_2_SIXTY_TWO("57 - 62"),
        SIXTY_THREE_2_SIXTY_EIGHT("63 - 68"),
        SIXTY_NINE_2_SEVENTY_FOUR("69 - 74"),
        SEVENTY_FIVE_2_EIGHTY("75 - 80"),
        EIGHTY_ONE_2_EIGHTY_SIX("81 - 86"),
        EIGHTY_SEVEN_2_NINETY_TWO("87 - 92"),
        NINETY_THREE_2_NINETY_EIGHT("93 - 98"),
        NINETY_NINE_2_ONE_OUGHT_FOUR("99 - 104"),
        ONE_OUGHT_FIVE_2_ONE_ONE_OUGHT("105 - 110"),
        TRIPLE_ONE_2_DOUBLE_ONE_SIX("111 - 116"),
        DOUBLE_ONE_SEVEN_2_ONE_DOUBLE_TWO("117 - 122"),
        ONE_TWO_THREE_2_ONE_TWO_EIGHT("123 - 128"),
        ONE_TWO_NINE_2_ONE_THREE_FOUR("129 - 134"),
        ONE_THREE_FIVE_2_ONE_FOUR_OUGHT("135 - 140"),
        ONE_FOUR_ONE_2_ONE_FOUR_SIX("141 - 146"),
        ONE_FOUR_SEVEN_2_ONE_FIVE_TWO("147 - 152"),
        ONE_FIVE_THREE_2_ONE_FIVE_EIGHT("153 - 158"),
        ONE_FIVE_NINE_2_ONE_SIX_FOUR("159 - 164"),
        ONE_SIX_FIVE_2_ONE_SEVEN_OUGHT("165 - 170");

        private final String range;

        public static Clothing fromValue(String value) {
            for (Clothing clothing : Clothing.values()) {
                if (clothing.range.equals(value)) {
                    return clothing;
                }
            }
            throw new IllegalArgumentException(getMessageSource(INVALID_ENUM_VALUE));
        }
    }

    @RequiredArgsConstructor
    @Getter
    enum Shoes {
        NINE_POINT_FIVE(9.5),
        TEN(10),
        TEN_POINT_FIVE(10.5),
        ELEVEN(11),
        ELEVEN_POINT_FIVE(11.5),
        TWELVE(12),
        TWELVE_POINT_FIVE(12.5),
        THIRTEEN(13),
        THIRTEEN_POINT_FIVE(13.5),
        FOURTEEN(14),
        FOURTEEN_POINT_FIVE(14.5),
        FIFTEEN(15),
        FIFTEEN_POINT_FIVE(15.5),
        SIXTEEN(16),
        SIXTEEN_POINT_FIVE(16.5),
        SEVENTEEN(17),
        SEVENTEEN_POINT_FIVE(17.5),
        EIGHTEEN(18),
        EIGHTEEN_POINT_FIVE(18.5),
        NINETEEN(19),
        NINETEEN_POINT_FIVE(19.5),
        TWENTY(20),
        TWENTY_POINT_FIVE(20.5),
        TWENTY_ONE(21),
        TWENTY_ONE_POINT_FIVE(21.5),
        TWENTY_TWO(22),
        TWENTY_TWO_POINT_FIVE(22.5),
        TWENTY_THREE(23),
        TWENTY_THREE_POINT_FIVE(23.5),
        TWENTY_FOUR(24),
        TWENTY_FOUR_POINT_FIVE(24.5),
        TWENTY_FIVE(25),
        TWENTY_FIVE_POINT_FIVE(25.5),
        TWENTY_SIX(26),
        TWENTY_SIX_POINT_FIVE(26.5),
        TWENTY_SEVEN(27),
        TWENTY_SEVEN_POINT_FIVE(27.5),
        TWENTY_EIGHT(28);

        private final double length;

        public static Shoes fromValue(Double value) {
            for (Shoes shoes : Shoes.values()) {
                if (shoes.length == value) {
                    return shoes;
                }
            }
            throw new IllegalArgumentException(getMessageSource(INVALID_ENUM_VALUE));
        }
    }
}
