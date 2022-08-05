package space.obminyashka.items_exchange.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UtilMapper {

    private static ModelMapper mapper;

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        mapper = modelMapper;
    }

    public static <T, K> List<K> convertAllTo(Collection<T> tList, Class<K> kClass) {
        return tList.stream()
                .map(t -> mapper.map(t, kClass))
                .toList();
    }

    public static <T, K> K convertTo(T src, Class<K> kClass) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return mapper.map(src, kClass);
    }

    public static <T, K> List<K> convertToDto(Collection<T> tCollection, Class<K> kClass) {
        return tCollection.stream()
                .map(t -> mapper.map(t, kClass))
                .toList();
    }

    public static <T, E> List<E> mapBy(Collection<T> collection, Function<T, E> mapper) {
        return collection.stream().map(mapper)
                .toList();
    }
}