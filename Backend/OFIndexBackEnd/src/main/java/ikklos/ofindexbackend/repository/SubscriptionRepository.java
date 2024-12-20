package ikklos.ofindexbackend.repository;

import ikklos.ofindexbackend.domain.SubscriptionModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<SubscriptionModel,Integer> {
    
    List<SubscriptionModel> findSubscriptionModelsByFollowerId(Integer followerId);

    List<SubscriptionModel> findSubscriptionModelsByFollowingId(Integer followingId);

}
