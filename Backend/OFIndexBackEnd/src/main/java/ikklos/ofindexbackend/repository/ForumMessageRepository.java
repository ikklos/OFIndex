package ikklos.ofindexbackend.repository;

import ikklos.ofindexbackend.domain.ForumMessageModel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForumMessageRepository extends JpaRepository<ForumMessageModel,Integer> {

    List<ForumMessageModel> findForumMessageModelsByReceiverId(Integer receiverId, Sort sort);

    List<ForumMessageModel> findForumMessageModelsBySenderId(Integer senderId);
    
}
