package ikklos.ofindexbackend.repository;

import ikklos.ofindexbackend.domain.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileModel,Integer> {



}
