package com.cubans.awssqs.service.sqs.visitor;

import com.cubans.awssqs.service.sqs.messages.UserAdminMessage;
import com.cubans.awssqs.service.sqs.messages.UserClientMessage;

public interface UserMessageVisitor <T> {

    T visit(UserAdminMessage userAdminMessage);

    T visit(UserClientMessage userClientMessage);
}
