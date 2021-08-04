package com.cubans.awssqs.service;

import static com.cubans.awssqs.configuration.Constant.ACK_MESSAGE;

import com.cubans.awssqs.api.UserRegistrationRequestDTO;
import com.cubans.awssqs.api.UserRegistrationResponseDTO;
import com.cubans.awssqs.service.sqs.SqsQueueSenderService;
import com.cubans.awssqs.service.sqs.messages.AbstractUserMessage;
import com.cubans.awssqs.service.sqs.messages.UserAdminMessage;
import com.cubans.awssqs.service.sqs.messages.UserClientMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Value("${aws.sqs.async.queue-name}")
    private String asyncQueueName;

    private final SqsQueueSenderService sqsQueueSenderService;

    @Autowired
    public UserService(SqsQueueSenderService sqsQueueSenderService) {
        this.sqsQueueSenderService = sqsQueueSenderService;
    }

    public UserRegistrationResponseDTO registerUser(UserRegistrationRequestDTO userRegistrationRequest) {
        AbstractUserMessage userMessage;

        if(userRegistrationRequest.getRol() != null && userRegistrationRequest.getRol().isAdmin()) {
            userMessage = new UserAdminMessage(userRegistrationRequest);
        } else {
            userMessage = new UserClientMessage(userRegistrationRequest);
        }

        this.sqsQueueSenderService.sendMessageToQueue(this.asyncQueueName, userMessage);

        return UserRegistrationResponseDTO.builder()
            .message(ACK_MESSAGE)
            .build();
    }
}
