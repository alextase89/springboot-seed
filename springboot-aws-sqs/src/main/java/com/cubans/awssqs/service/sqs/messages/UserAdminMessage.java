package com.cubans.awssqs.service.sqs.messages;

import com.cubans.awssqs.api.UserRegistrationRequestDTO;
import com.cubans.awssqs.service.sqs.visitor.UserMessageVisitor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAdminMessage extends AbstractUserMessage {

    public UserAdminMessage(UserRegistrationRequestDTO userRegistrationRequest) {
        super();
        this.userRegistrationRequest = userRegistrationRequest;
    }

    @Override
    public <T> T accept(UserMessageVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
