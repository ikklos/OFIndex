package ikklos.ofindexbackend.repository;

import ikklos.ofindexbackend.domain.PostModel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<PostModel,Integer> {

    List<PostModel> findPostModelByPostId(Integer postId);

    List<PostModel> findPostModelsByUserId(Integer userId, Sort sort);

    List<PostModel> findPostModelsByBookId(Integer bookId, Sort sort);
    
}
