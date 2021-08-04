package com.cubans.awssqs.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SendMessageResponseDTO {

    private String message;
}
