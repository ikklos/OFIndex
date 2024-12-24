package ikklos.ofindexbackend.repository;

import ikklos.ofindexbackend.domain.UserCommentLikeModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCommentLikeRepository extends JpaRepository<UserCommentLikeModel,Integer> {

    int countAllByCommentId(Integer commentId);

    boolean existsByUserIdAndCommentId(Integer userId, Integer commentId);

    Optional<UserCommentLikeModel> findUserPostLikeModelByCommentIdAndUserId(Integer commentId, Integer userId);
}
