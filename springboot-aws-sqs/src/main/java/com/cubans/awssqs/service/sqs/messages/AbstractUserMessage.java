package com.cubans.awssqs.service.sqs.messages;

import com.cubans.awssqs.api.UserRegistrationRequestDTO;
import com.cubans.awssqs.service.sqs.visitor.UserMessageVisitor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "message_type")
@JsonSubTypes({
    @Type(value = UserAdminMessage.class, name = "user_admin"),
    @Type(value = UserClientMessage.class, name = "user_client"),
})
public abstract class AbstractUserMessage {

    protected UserRegistrationRequestDTO userRegistrationRequest;

    public abstract <T> T accept(final UserMessageVisitor<T> visitor);
}
