package ikklos.ofindexbackend.repository;

import ikklos.ofindexbackend.domain.BookModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<BookModel,Integer> {

    List<BookModel> findBookModelsByBookClassAndNameLike(Integer bookClass, String name);

    List<BookModel> findBookModelsByNameLike(String name);

    List<BookModel> findBookModelsByBookClassAndTagsLike(Integer bookClass, String tags);
    List<BookModel> findBookModelsByTagsLike(String tags);

    List<BookModel> findBookModelsByBookClassAndDescriptionLike(Integer bookClass, String description);
    List<BookModel> findBookModelsByDescriptionLike(String description);

}
