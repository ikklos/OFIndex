package ikklos.ofindexbackend.repository;

import ikklos.ofindexbackend.domain.BookClassModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookClassRepository extends JpaRepository<BookClassModel,Integer> {
}
