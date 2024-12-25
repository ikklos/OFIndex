package ikklos.ofindexbackend.repository;

import ikklos.ofindexbackend.domain.UserPackLikeModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPackLikeRepository extends JpaRepository<UserPackLikeModel,Integer> {
    
    int countAllByPackId(Integer packId);

    boolean existsUserPackLikeModelByUserIdAndPackId(Integer userId, Integer packId);

    Optional<UserPackLikeModel> findUserPackLikeModelByUserIdAndPackId(Integer userId, Integer packId);
}
