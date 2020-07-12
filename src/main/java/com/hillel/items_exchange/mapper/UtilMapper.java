package com.hillel.items_exchange.mapper;

import com.hillel.items_exchange.dto.PhoneDto;
import com.hillel.items_exchange.model.Phone;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UtilMapper {

    private static ModelMapper mapper;

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        mapper = modelMapper;
    }

    public static  <T, K> Collection<K> convertToModel(List<T> tList, Class<K> kClass,
                                                       Supplier<Collection<K>> collectionFactory) {
        return tList.stream()
                .map(t -> mapper.map(t, kClass))
                .collect(Collectors.toCollection(collectionFactory));
    }

    public static Phone mapPhones(PhoneDto phoneDto) {
        Converter<String, Long> stringLongConverter = context ->
                Long.parseLong(context.getSource().replaceAll("[^\\d]", ""));
        mapper.typeMap(PhoneDto.class, Phone.class)
                .addMappings(mapper -> mapper.using(stringLongConverter)
                        .map(PhoneDto::getPhoneNumber, Phone::setPhoneNumber));
        return mapper.map(phoneDto, Phone.class);
    }

    public static <T, K> List<K> convertToDto(Set<T> tSet, Class<K> kClass) {
        return tSet.stream()
                .map(t -> mapper.map(t, kClass))
                .collect(Collectors.toList());
    }
}