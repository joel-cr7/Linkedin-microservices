package com.linkedin.connectionservice.repository;

import com.linkedin.connectionservice.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends Neo4jRepository<Person, Long> {

    Optional<Person> findByUserId(Long userId);


    /**
     * Fetch the first degree connection of a user
     * @param userId
     * @return List<Person>
     */
    @Query("match (p1:Person) -[:CONNECTED_TO]- (p2:Person) " +
            "where p1.userId = $userId " +
            "return p2")
    List<Person> getFirstDegreeConnections(Long userId);


    /**
     * Check if sender has already requested connection to receiver
     * @param senderUserId
     * @param receiverUserId
     * @return boolean
     */
    @Query("match (p1:Person) -[r:REQUESTED_TO]-> (p2:Person) " +
            "where p1.userId = $senderUserId and p2.userId = $receiverUserId " +
            "return count(r) > 0")
    boolean connectionRequestExists(Long senderUserId, Long receiverUserId);


    /**
     * Check if sender is already connected to receiver
     * @param senderUserId
     * @param receiverUserId
     * @return boolean
     */
    @Query("match (p1:Person) -[r:CONNECTED_TO]- (p2:Person) " +
            "where p1.userId = $senderUserId and p2.userId = $receiverUserId " +
            "return count(r) > 0")
    boolean alreadyConnected(Long senderUserId, Long receiverUserId);


    /**
     * Create connection request from sender to receiver
     * @param senderUserId
     * @param receiverUserId
     * @return boolean
     */
    @Query("match (p1:Person), (p2:Person) " +
            "where p1.userId = $senderUserId and p2.userId = $receiverUserId " +
            "create (p1) -[:REQUESTED_TO]-> (p2)")
    void addConnectionRequest(Long senderUserId, Long receiverUserId);


    /**
     * Accept connection request from sender to receiver
     * @param senderUserId
     * @param receiverUserId
     * @return boolean
     */
    @Query("match (p1:Person) -[r:REQUESTED_TO]-> (p2:Person) " +
            "where p1.userId = $senderUserId and p2.userId = $receiverUserId " +
            "delete r " +
            "create (p1) -[:CONNECTED_TO]-> (p2)")
    void acceptConnectionRequest(Long senderUserId, Long receiverUserId);


    /**
     * Reject connection request from sender to receiver
     * @param senderUserId
     * @param receiverUserId
     * @return boolean
     */
    @Query("match (p1:Person) -[r:REQUESTED_TO]-> (p2:Person) " +
            "where p1.userId = $senderUserId and p2.userId = $receiverUserId " +
            "delete r")
    void rejectConnectionRequest(Long senderUserId, Long receiverUserId);


}
