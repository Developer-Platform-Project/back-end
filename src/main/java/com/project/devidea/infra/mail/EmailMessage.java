package com.project.devidea.infra.mail;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailMessage {

    private String to;
    private String subject;
    private String message;
}
