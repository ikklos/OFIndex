package ikklos.ofindexbackend.repository;

import ikklos.ofindexbackend.domain.PackModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackRepository extends JpaRepository<PackModel,Integer> {

    List<PackModel> findAllByBookId(Integer bookId);
}

