package space.obminyashka.items_exchange.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Collections {

    public static <T> Collection<T> extractAll(Collection<T> src, Predicate<T> predicate,
                                               Supplier<Collection<T>> collectionFactory) {
        Collection<T> extracted = src.stream()
                .filter(predicate)
                .collect(Collectors.toCollection(collectionFactory));
        src.removeAll(extracted);
        return extracted;
    }
}
