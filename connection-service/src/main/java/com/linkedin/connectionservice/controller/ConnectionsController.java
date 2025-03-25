package com.linkedin.connectionservice.controller;


import com.linkedin.connectionservice.entity.Person;
import com.linkedin.connectionservice.services.ConnectionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
@Slf4j
public class ConnectionsController {

    private final ConnectionsService connectionsService;

    /**
     * Get the first degree connection from Neo4J graph DB
     * @param userId
     * @return ResponseEntity<List<Person>>
     */
    @GetMapping("/{userId}/first-degree")
    public ResponseEntity<List<Person>> getFirstDegreeConnections(@PathVariable Long userId){
        List<Person> firstDegreeConnections = connectionsService.getFirstDegreeConnectionsOfUser(userId);
        return ResponseEntity.ok(firstDegreeConnections);
    }


    /**
     * Send connection request to user with id userId
     * @param userId
     * @return ResponseEntity<Void>
     */
    @PostMapping("/request/{userId}")
    public ResponseEntity<Void> sendConnectionRequest(@PathVariable Long userId){
        connectionsService.sendConnectionRequest(userId);
        return ResponseEntity.noContent().build();
    }


    /**
     * Accept connection request to user with id userId
     * @param userId
     * @return ResponseEntity<Void>
     */
    @PostMapping("/accept/{userId}")
    public ResponseEntity<Void> acceptConnectionRequest(@PathVariable Long userId){
        connectionsService.acceptConnectionRequest(userId);
        return ResponseEntity.noContent().build();
    }


    /**
     * Reject connection request to user with id userId
     * @param userId
     * @return ResponseEntity<Void>
     */
    @PostMapping("/reject/{userId}")
    public ResponseEntity<Void> rejectConnectionRequest(@PathVariable Long userId){
        connectionsService.rejectConnectionRequest(userId);
        return ResponseEntity.noContent().build();
    }

}
