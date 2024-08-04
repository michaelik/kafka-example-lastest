package com.kafka.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "email_message")
public class EmailMessage {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "email_message_id")
    private Long id;

    @Column(name = "from_address")
    private String from;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "email_message_recipients",
            joinColumns = @JoinColumn(name = "email_message_id")
    )
    @Column(name = "recipient")
    private List<String> to;

    @Column(name = "subject")
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;
}
