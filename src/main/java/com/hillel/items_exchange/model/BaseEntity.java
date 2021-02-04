package com.hillel.items_exchange.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

import com.hillel.items_exchange.model.enums.Status;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "created", columnDefinition = "DATE", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime created;

    @Column(name = "updated", columnDefinition = "DATE", nullable = false)
    @LastModifiedDate
    private LocalDateTime updated;

    @Enumerated(EnumType.STRING)
    private Status status;
}
