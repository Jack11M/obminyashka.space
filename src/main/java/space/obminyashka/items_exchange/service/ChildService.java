package space.obminyashka.items_exchange.service;

import space.obminyashka.items_exchange.dto.ChildDto;

import java.util.List;

public interface ChildService {
    /**
     * Get all children from gained user
     * @param username the user which is requested for getting all the children
     * @return all existed children for provided user
     */
    List<ChildDto> getChildrenByUsername(String username);

    /**
     * Update existed children of gained user
     * @param username for children update
     * @param childrenDtoToUpdate updated children data
     * @return children which were updated
     */
    List<ChildDto> updateChildren(String username, List<ChildDto> childrenDtoToUpdate);
}
