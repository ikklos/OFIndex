package ikklos.ofindexbackend.repository;

import ikklos.ofindexbackend.domain.SubscriptionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<SubscriptionModel,Integer> {
    
    List<SubscriptionModel> findSubscriptionModelsByFollowerId(Integer followerId);

    List<SubscriptionModel> findSubscriptionModelsByFollowingId(Integer followingId);
    
    boolean existsByFollowerIdAndFollowingId(Integer followerId, Integer followingId);
    
    List<SubscriptionModel> findSubscriptionModelsByFollowerIdAndFollowingId(Integer followerId, Integer followingId);

    @Modifying
    @Transactional
    int deleteAllByFollowerIdAndFollowingId(Integer followerId, Integer followingId);
    
}
