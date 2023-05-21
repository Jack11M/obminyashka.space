package space.obminyashka.items_exchange.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.obminyashka.items_exchange.dao.ChildRepository;
import space.obminyashka.items_exchange.dto.ChildDto;
import space.obminyashka.items_exchange.mapper.ChildMapper;
import space.obminyashka.items_exchange.service.ChildService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChildServiceImpl implements ChildService {
    private final ChildMapper childMapper;
    private final ChildRepository childRepository;

    @Override
    public List<ChildDto> getChildrenByUsername(String username) {
        return childMapper.toDtoList(childRepository.findByUser_Username(username));
    }

    @Override
    public List<ChildDto> updateChildren(String username, List<ChildDto> childrenDtoToUpdate) {
        final var childrenToSave = childMapper.toModelList(childrenDtoToUpdate);
        childRepository.deleteByUser_Username(username);
        childrenToSave.forEach(child -> childRepository.createChildrenByUsername(child, username));
        return childrenDtoToUpdate;
    }
}
