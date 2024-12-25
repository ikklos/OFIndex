package ikklos.ofindexbackend.repository;

import ikklos.ofindexbackend.domain.PackModel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackRepository extends JpaRepository<PackModel,Integer> {

    List<PackModel> findAllByBookId(Integer bookId);

    List<PackModel> findAllByAuthorId(Integer authorId, Sort sort);

    List<PackModel> findAllByOwnerId(Integer ownerId, Sort sort);

    List<PackModel> findAllByOwnerIdAndBookId(Integer ownerId, Integer bookId);

}

