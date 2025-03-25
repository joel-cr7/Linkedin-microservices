package com.linkedin.connectionservice.event;

import lombok.Builder;
import lombok.Data;


/**
 * This event is used to notify concerning users for any updates (connection requested, accepted or rejected)
 */
@Data
public class ConnectionEvent {
    private Long senderUserId;
    private Long receiverUserId;
}
