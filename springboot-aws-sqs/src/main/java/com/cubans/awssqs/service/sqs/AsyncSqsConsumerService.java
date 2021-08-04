package com.cubans.awssqs.service.sqs;

import com.cubans.awssqs.service.AsyncUserMessageVisitorService;
import com.cubans.awssqs.service.sqs.messages.AbstractUserMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.listener.Acknowledgment;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AsyncSqsConsumerService {

    private final AsyncUserMessageVisitorService asyncUserMessageVisitorService;

    @Autowired
    public AsyncSqsConsumerService(AsyncUserMessageVisitorService asyncUserMessageVisitorService) {
        this.asyncUserMessageVisitorService = asyncUserMessageVisitorService;
    }

    @SqsListener(value = "${aws.sqs.async.queue-name}", deletionPolicy = SqsMessageDeletionPolicy.NEVER)
    public void onMessage(@Payload  AbstractUserMessage userMessage, Acknowledgment acknowledgment) {

        if(userMessage.accept(this.asyncUserMessageVisitorService)) {
            acknowledgment.acknowledge();
        }
    }
}
