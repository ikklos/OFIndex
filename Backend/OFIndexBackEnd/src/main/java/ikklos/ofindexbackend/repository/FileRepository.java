package ikklos.ofindexbackend.repository;

import ikklos.ofindexbackend.domain.FileModel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileModel,Integer> {

    List<FileModel> findFileModelsByUserId(Integer userId, Sort sort);



}
