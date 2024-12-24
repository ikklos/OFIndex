package ikklos.ofindexbackend.repository;

import ikklos.ofindexbackend.domain.UserPostLikeModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPostLikeRepository extends JpaRepository<UserPostLikeModel,Integer> {

    int countAllByPostId(Integer postId);
    
    boolean existsByUserIdAndPostId(Integer userId, Integer postId);

    Optional<UserPostLikeModel> findUserPostLikeModelByPostIdAndUserId(Integer postId, Integer userId);
}
