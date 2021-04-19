package com.project.devidea.modules.account.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendEmailToken {

    private String token;
    private String to;
}
