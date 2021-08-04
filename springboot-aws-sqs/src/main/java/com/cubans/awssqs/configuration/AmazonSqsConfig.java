package com.cubans.awssqs.configuration;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.cubans.library.config.AmazonBaseServiceConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.QueueMessageHandler;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.cloud.aws.messaging.listener.support.AcknowledgmentHandlerMethodArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.support.HeadersMethodArgumentResolver;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;

@Slf4j
@Configuration
public class AmazonSqsConfig extends AmazonBaseServiceConfig {

    @Value("${aws.sqs.connection.keep-alive:true}")
    protected boolean connectionKeepAlive;
    @Value("${aws.sqs.connection.ttl-ms:60000}")
    protected long connectionTTL;

    private final ObjectMapper objectMapper;

    @Autowired
    public AmazonSqsConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean(destroyMethod = "shutdown")
    public AmazonSQSAsync amazonSQSAsync() {
        return AmazonSQSAsyncClientBuilder
            .standard()
            .withCredentials(this.buildCredentials())
            .withEndpointConfiguration(this.buildEndpointConfiguration())
            .withClientConfiguration(clientConfiguration())
            .build();
    }

    @Bean(destroyMethod = "shutdown")
    public AmazonSQS amazonSQSSync(){
        return AmazonSQSClientBuilder
            .standard()
            .withCredentials(this.buildCredentials())
            .withEndpointConfiguration(this.buildEndpointConfiguration())
            .withClientConfiguration(clientConfiguration())
            .build();
    }

    @Bean
    public ClientConfiguration clientConfiguration() {
        return new ClientConfiguration()
            .withTcpKeepAlive(this.connectionKeepAlive)
            .withConnectionTTL(this.connectionTTL);
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer() {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setAmazonSqs(amazonSQSAsync());
        simpleMessageListenerContainer.setMessageHandler(queueMessageHandler());
        simpleMessageListenerContainer.setMaxNumberOfMessages(10);
        simpleMessageListenerContainer.setWaitTimeOut(20);;
        return simpleMessageListenerContainer;
    }

    @Bean
    public QueueMessageHandler queueMessageHandler() {
        QueueMessageHandlerFactory queueMessageHandlerFactory = new QueueMessageHandlerFactory();
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setStrictContentTypeMatch(false);
        messageConverter
            .setObjectMapper(objectMapper.copy().disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS));
        queueMessageHandlerFactory.setArgumentResolvers(getMethodArgumentResolvers(messageConverter));
        queueMessageHandlerFactory.setAmazonSqs(amazonSQSAsync());
        return queueMessageHandlerFactory.createQueueMessageHandler();
    }

    @Bean
    public QueueMessagingTemplate messagingTemplate() {
        return new QueueMessagingTemplate(amazonSQSAsync());
    }

    private List<HandlerMethodArgumentResolver> getMethodArgumentResolvers(MessageConverter messageConverter) {
        return Arrays.asList(
            new HeadersMethodArgumentResolver(),
            new AcknowledgmentHandlerMethodArgumentResolver("Acknowledgment"),
            new PayloadMethodArgumentResolver(messageConverter));
    }
}
