package com.cubans.awssqs.service;

import com.cubans.awssqs.api.UserRegistrationRequestDTO;
import com.cubans.awssqs.service.sqs.messages.UserAdminMessage;
import com.cubans.awssqs.service.sqs.messages.UserClientMessage;
import com.cubans.awssqs.service.sqs.visitor.UserMessageVisitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AsyncUserMessageVisitorService implements UserMessageVisitor<Boolean> {

    @Override
    public Boolean visit(UserAdminMessage userAdminMessage) {
        UserRegistrationRequestDTO userRegistrationRequest = userAdminMessage.getUserRegistrationRequest();
        log.info("Processing user Admin {}", userRegistrationRequest);
        return true;
    }

    @Override
    public Boolean visit(UserClientMessage userClientMessage) {
        UserRegistrationRequestDTO userRegistrationRequest = userClientMessage.getUserRegistrationRequest();
        log.info("Processing user Client {}", userRegistrationRequest);
        return true;
    }
}
