package ikklos.ofindexbackend.repository;

import ikklos.ofindexbackend.domain.ShelfBookModel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShelfBookRepository extends JpaRepository<ShelfBookModel,Integer> {

    List<ShelfBookModel> findShelfBookModelsByShelfId(Integer shelfId, Sort sort);

    void removeShelfBookModelsByShelfId(Integer shelfId);
    
    List<ShelfBookModel> findShelfBookModelByShelfIdAndBookId(Integer shelfId, Integer bookId);

    void deleteShelfBookModelsByShelfId(Integer shelfId);
}
