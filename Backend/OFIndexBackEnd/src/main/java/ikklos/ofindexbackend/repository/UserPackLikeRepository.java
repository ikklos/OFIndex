package ikklos.ofindexbackend.repository;

import ikklos.ofindexbackend.domain.UserPackLikeModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPackLikeRepository extends JpaRepository<UserPackLikeModel,Integer> {
    
    long countAllByPackId(Integer packId);

    boolean existsUserPackLikeModelByUserIdAndPackId(Integer userId, Integer packId);
    
}
