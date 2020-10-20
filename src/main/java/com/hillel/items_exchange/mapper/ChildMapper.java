package com.hillel.items_exchange.mapper;

import com.hillel.items_exchange.dto.ChildDto;
import com.hillel.items_exchange.model.Child;
import com.hillel.items_exchange.model.ChildGender;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChildMapper {

    @Autowired
    private ModelMapper mapper;

    public Child convertChildDto(ChildDto childDto) {
        Converter<ChildGender, String> childGenderStringConverter = context -> context.getSource().getGender();

        mapper.typeMap(ChildDto.class, Child.class)
                .addMappings(mapping -> mapping.using(childGenderStringConverter)
                        .map(ChildDto::getSex, Child::setSex));

        return mapper.map(childDto, Child.class);
    }

    public ChildDto convertChild(Child child) {
        Converter<String, ChildGender> stringChildGenderConverter = context ->
                getChildGender(context.getSource());

        mapper.typeMap(Child.class, ChildDto.class)
                .addMappings(mapping -> mapping.using(stringChildGenderConverter)
                        .map(Child::getSex, ChildDto::setSex));

        return mapper.map(child, ChildDto.class);
    }

    private ChildGender getChildGender(String gender) {
        ChildGender childGender = ChildGender.UNSELECTED;

        for (ChildGender childGen : ChildGender.values()) {
            if (gender.equalsIgnoreCase(childGen.getGender())) {
                childGender = childGen;
            }
        }
        return childGender;
    }
}
