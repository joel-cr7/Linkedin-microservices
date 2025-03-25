package com.linkedin.connectionservice.services;


import com.linkedin.connectionservice.auth.AuthContextHolder;
import com.linkedin.connectionservice.entity.Person;
import com.linkedin.connectionservice.event.ConnectionEvent;
import com.linkedin.connectionservice.exception.BadRequestException;
import com.linkedin.connectionservice.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionsService {

    private final PersonRepository personRepository;
    private final KafkaTemplate<Long, ConnectionEvent> connectionEventKafkaTemplate;

    public List<Person> getFirstDegreeConnectionsOfUser(Long userId){
        log.info("Getting first degree connections of user with ID: {}", userId);
        return personRepository.getFirstDegreeConnections(userId);
    }


    /**
     * Send connection request to receiver user
     * @param receiverUserId
     */
    public void sendConnectionRequest(Long receiverUserId) {
        Long senderUserId = AuthContextHolder.getCurrentUserId();
        log.info("Sending connection request with senderId: {}, receiverId: {}", senderUserId, receiverUserId);

        if(senderUserId.equals(receiverUserId)){
            throw new BadRequestException("Both sender and receiver are the same !");
        }

        boolean connectionRequestExists = personRepository.connectionRequestExists(senderUserId, receiverUserId);
        if(connectionRequestExists){
            throw new BadRequestException("Connection request already exists, cannot send again !");
        }

        boolean alreadyConnected = personRepository.alreadyConnected(senderUserId, receiverUserId);
        if(alreadyConnected){
            throw new BadRequestException("Already connected users, cannot add connection request !");
        }

        personRepository.addConnectionRequest(senderUserId, receiverUserId);
        log.info("Successfully sent connection request");

        // send notification using kafka to the receiver user
        ConnectionEvent connectionEvent = ConnectionEvent.builder()
                .receiverUserId(receiverUserId)
                .senderUserId(senderUserId)
                .build();

        connectionEventKafkaTemplate.send("connection_request_topic", connectionEvent);
    }


    /**
     * Accept connection request sent from sender
     * @param senderUserId
     */
    public void acceptConnectionRequest(Long senderUserId){
        Long receiverUserId = AuthContextHolder.getCurrentUserId();
        log.info("Accepting connection request with senderId: {}, receiverId: {}", senderUserId, receiverUserId);

        if(senderUserId.equals(receiverUserId)){
            throw new BadRequestException("Both sender and receiver are the same !");
        }

        boolean alreadyConnected = personRepository.alreadyConnected(senderUserId, receiverUserId);
        if(alreadyConnected){
            throw new BadRequestException("Already connected users, cannot add connection request !");
        }

        boolean connectionRequestExists = personRepository.connectionRequestExists(senderUserId, receiverUserId);
        if(!connectionRequestExists){
            throw new BadRequestException("No connection request exists, cannot accept without request !");
        }

        personRepository.acceptConnectionRequest(senderUserId, receiverUserId);
        log.info("Successfully accepted connection request with senderId: {}, receiverId: {}", senderUserId,
                receiverUserId);

        // send notification using kafka to the sender user
        ConnectionEvent connectionEvent = ConnectionEvent.builder()
                .receiverUserId(receiverUserId)
                .senderUserId(senderUserId)
                .build();

        connectionEventKafkaTemplate.send("connection_accepted_topic", connectionEvent);
    }


    /**
     * Reject connection request sent from sender
     * @param senderUserId
     */
    public void rejectConnectionRequest(Long senderUserId){
        Long receiverUserId = AuthContextHolder.getCurrentUserId();
        log.info("Rejecting connection request with senderId: {}, receiverId: {}", senderUserId, receiverUserId);

        if(senderUserId.equals(receiverUserId)){
            throw new BadRequestException("Both sender and receiver are the same !");
        }

        boolean connectionRequestExists = personRepository.connectionRequestExists(senderUserId, receiverUserId);
        if(!connectionRequestExists){
            throw new BadRequestException("No connection request exists, cannot reject it !");
        }

        personRepository.rejectConnectionRequest(senderUserId, receiverUserId);
        log.info("Successfully rejected connection request with senderId: {}, receiverId: {}", senderUserId,
                receiverUserId);

        // send notification using kafka to the sender user
        ConnectionEvent connectionEvent = ConnectionEvent.builder()
                .receiverUserId(receiverUserId)
                .senderUserId(senderUserId)
                .build();

        connectionEventKafkaTemplate.send("connection_rejected_topic", connectionEvent);
    }
}
