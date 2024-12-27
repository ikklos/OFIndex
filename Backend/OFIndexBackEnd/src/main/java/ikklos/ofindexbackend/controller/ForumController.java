package ikklos.ofindexbackend.controller;

import ikklos.ofindexbackend.domain.ForumMessageModel;
import ikklos.ofindexbackend.domain.SubscriptionModel;
import ikklos.ofindexbackend.domain.UserModel;
import ikklos.ofindexbackend.repository.ForumMessageRepository;
import ikklos.ofindexbackend.repository.SubscriptionRepository;
import ikklos.ofindexbackend.repository.UserRepository;
import ikklos.ofindexbackend.utils.JwtUtils;
import ikklos.ofindexbackend.utils.UniversalBadReqException;
import ikklos.ofindexbackend.utils.UniversalResponse;
import ikklos.ofindexbackend.utils.UserPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping(value = "/forum",produces = "application/json")
public class ForumController {

    public static class MessageListResponse{
        public static class MessageItem{
            public Integer messageId;
            public Integer type;
            public String text;
            public LocalDateTime addTime;
            public Boolean read;
            public Integer senderId;
            public String senderName;
            public String senderAvatar;

            public MessageItem setByModel(ForumMessageModel model){
                messageId=model.getMessageId();
                type=model.getType();
                text=model.getText();
                addTime=model.getTimeStamp();
                read=model.getIsRead()!=0;
                return this;
            }

            public void setByModel(UserModel model){
                senderId=model.getUserid();
                senderName=model.getUsername();
                senderAvatar=model.getAvatar();
            }
        }

        public List<MessageItem> messages;
        public Integer count;
    }

    public static class SubscriptionTestResponse extends UniversalResponse{
        public Boolean subscribed;
        public Boolean notification;
    }

    private final ForumMessageRepository forumMessageRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Autowired
    public ForumController(ForumMessageRepository forumMessageRepository,
                           SubscriptionRepository subscriptionRepository,
                           UserRepository userRepository){
        this.userRepository=userRepository;
        this.forumMessageRepository=forumMessageRepository;
        this.subscriptionRepository=subscriptionRepository;
    }

    @GetMapping("/message")
    public MessageListResponse getAllMessage(@RequestHeader("Authorization") String token){
        Integer userId= JwtUtils.getUserIdJWT(token);
        MessageListResponse response=new MessageListResponse();

        List<ForumMessageModel> messageModels=forumMessageRepository.findForumMessageModelsByReceiverId(userId, Sort.by(Sort.Direction.DESC,"timeStamp"));

        response.messages=messageModels.stream().map(forumMessageModel -> {
            MessageListResponse.MessageItem item=new MessageListResponse.MessageItem().setByModel(forumMessageModel);

            var userO=userRepository.findById(forumMessageModel.getSenderId());
            if(userO.isEmpty())return null;
            item.setByModel(userO.get());
            return item;
        }).filter(Objects::nonNull).toList();

        response.count=response.messages.size();
        return response;
    }

    @DeleteMapping("/message/{messageId}")
    public UniversalResponse deleteMessage(@RequestHeader("Authorization") String token,
                                           @PathVariable Integer messageId) throws UniversalBadReqException {
        Integer userId = JwtUtils.getUserIdJWT(token);

        var messageO=forumMessageRepository.findById(messageId);
        if(messageO.isEmpty())throw new UniversalBadReqException("No such message");
        ForumMessageModel forumMessageModel= messageO.get();

        if(!Objects.equals(forumMessageModel.getReceiverId(), userId)
                && !Objects.equals(forumMessageModel.getSenderId(), userId))
            throw new UniversalBadReqException("message not about you");

        forumMessageRepository.delete(forumMessageModel);

        UniversalResponse response=new UniversalResponse();
        response.message="Message removed";
        return response;
    }

    @GetMapping("/message/read/{messageid}")
    public UniversalResponse setMessageRead(@RequestHeader("Authorization") String token,
                                            @PathVariable Integer messageid) throws UniversalBadReqException {
        Integer userId = JwtUtils.getUserIdJWT(token);

        var messageO=forumMessageRepository.findById(messageid);
        if(messageO.isEmpty())throw new UniversalBadReqException("No such message");
        ForumMessageModel forumMessageModel= messageO.get();

        if(!Objects.equals(forumMessageModel.getReceiverId(), userId)
            && UserPermissions.noPermission(userRepository, userId, 5))
            throw new UniversalBadReqException("Permission denied");

        forumMessageModel.setIsRead(1);
        forumMessageRepository.save(forumMessageModel);

        UniversalResponse response=new UniversalResponse();
        response.message="Message read";
        return response;
    }

    @GetMapping("/subscribe/{userid}")
    public UniversalResponse subscribeUser(@RequestHeader("Authorization") String token,
                                           @PathVariable("userid") Integer followingId) throws UniversalBadReqException {
        Integer followerId = JwtUtils.getUserIdJWT(token);

        if(!userRepository.existsById(followingId))throw new UniversalBadReqException("No such user to follow");
        if(!userRepository.existsById(followerId))throw new UniversalBadReqException("No such user");

        if(subscriptionRepository.existsByFollowerIdAndFollowingId(followerId,followingId))throw new UniversalBadReqException("Already subscribed");

        SubscriptionModel model=new SubscriptionModel();
        model.setFollowerId(followerId);
        model.setFollowingId(followingId);
        model.setNotification(1);
        model.setTimeStamp(LocalDateTime.now());
        subscriptionRepository.save(model);

        ForumMessageModel.addForumMessage(forumMessageRepository,
                followerId,followingId,2,"Subscribed you",false);

        UniversalResponse response=new UniversalResponse();
        response.message="subscribed";
        return response;
    }

    @GetMapping("/subscribe/test/{followingId}")
    public SubscriptionTestResponse testSubscription(@PathVariable Integer followingId,
                                                     @RequestHeader("Authorization") String token) throws UniversalBadReqException{
        Integer followerId = JwtUtils.getUserIdJWT(token);

        if(!userRepository.existsById(followingId))throw new UniversalBadReqException("No such user to follow");
        if(!userRepository.existsById(followerId))throw new UniversalBadReqException("No such user");

        SubscriptionTestResponse response=new SubscriptionTestResponse();
        var subscriptionO=subscriptionRepository.findSubscriptionModelsByFollowerIdAndFollowingId(followerId,followingId);

        response.subscribed= !subscriptionO.isEmpty();
        response.notification=!subscriptionO.isEmpty()&&subscriptionO.get(0).getNotification()!=0;

        return response;
    }

    @GetMapping("/subscribe/notification")
    public UniversalResponse setSubscriptionNotification(@RequestParam Integer userid,
                                                         @RequestParam Boolean notification,
                                                         @RequestHeader("Authorization") String token) throws UniversalBadReqException{
        Integer followerId = JwtUtils.getUserIdJWT(token);

        if(!userRepository.existsById(userid))throw new UniversalBadReqException("No such user to follow");
        if(!userRepository.existsById(followerId))throw new UniversalBadReqException("No such user");

        var subO=subscriptionRepository.findSubscriptionModelsByFollowerIdAndFollowingId(followerId,userid);
        if(subO.isEmpty())throw new UniversalBadReqException("Not subscribed yet");
        SubscriptionModel subscriptionModel=subO.get(0);

        subscriptionModel.setNotification(notification?1:0);

        UniversalResponse response=new UniversalResponse();
        response.message="set to "+notification;
        return response;
    }

    @DeleteMapping("/subscribe/{userid}")
    public UniversalResponse setSubscriptionNotification(@PathVariable Integer userid,
                                                         @RequestHeader("Authorization") String token) throws UniversalBadReqException{
        Integer followerId = JwtUtils.getUserIdJWT(token);

        if(!userRepository.existsById(userid))throw new UniversalBadReqException("No such user to follow");
        if(!userRepository.existsById(followerId))throw new UniversalBadReqException("No such user");

        int count=subscriptionRepository.deleteAllByFollowerIdAndFollowingId(followerId,userid);

        UniversalResponse response=new UniversalResponse();
        response.message="removed: "+count;
        return response;
    }

}
