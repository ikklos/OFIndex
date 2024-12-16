package ikklos.ofindexbackend.repository;

import ikklos.ofindexbackend.domain.BookModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<BookModel,Integer> {



}
