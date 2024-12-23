package ikklos.ofindexbackend.repository;

import ikklos.ofindexbackend.domain.CommentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentModel,Integer> {
    
    List<CommentModel> findCommentModelsByPostId(Integer postId, Sort sort);

    List<CommentModel> findCommentModelsByUserId(Integer userId);

    List<CommentModel> findCommentModelsByPostIdAndParentCommentOrderByTimeStampDesc(Integer postId, Integer parentComment, Sort sort);

}
