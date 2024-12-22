package ikklos.ofindexbackend.repository;

import ikklos.ofindexbackend.domain.BookModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<BookModel,Integer> {

    Page<BookModel> findBookModelsByBookClassAndNameLike(Integer bookClass, String name, Pageable pageable);

    Page<BookModel> findBookModelsByNameLike(String name, Pageable pageable);
    
}
