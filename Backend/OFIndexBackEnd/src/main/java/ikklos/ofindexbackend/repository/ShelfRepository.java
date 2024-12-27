package ikklos.ofindexbackend.repository;

import ikklos.ofindexbackend.domain.ShelfModel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShelfRepository extends JpaRepository<ShelfModel,Integer> {
    
    List<ShelfModel> findShelfModelsByUserId(Integer userId, Sort sort);

    List<ShelfModel> findShelfModelByShelfId(Integer shelfId);

    List<ShelfModel> findShelfModelByUserIdAndIndex(Integer userId, Integer index);

    int countShelfModelByUserId(Integer userId);

}
