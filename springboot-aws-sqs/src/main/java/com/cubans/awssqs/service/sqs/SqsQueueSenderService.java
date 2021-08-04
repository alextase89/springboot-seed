package com.cubans.awssqs.service.sqs;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SqsQueueSenderService {

    private final QueueMessagingTemplate queueMessagingTemplate;

    @Autowired
    public SqsQueueSenderService(AmazonSQSAsync amazonSQSAsync, ObjectMapper objectMapper) {
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setObjectMapper(objectMapper);
        messageConverter.setStrictContentTypeMatch(false);
        messageConverter.setSerializedPayloadClass(String.class);
        queueMessagingTemplate = new QueueMessagingTemplate(amazonSQSAsync);
        queueMessagingTemplate.setMessageConverter(messageConverter);
    }

    public <T> void sendMessageToQueue(String queueName, T payload) {
        log.info("Sending payload to queue: {}, payload: {}", queueName, payload);
        queueMessagingTemplate.convertAndSend(queueName, payload);
        log.info("Payload sent to queue: {}, payload: {}", queueName, payload);
    }
}
