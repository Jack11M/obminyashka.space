package com.hillel.evoApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "phones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Phone extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "phone_number")
    private Long phoneNumber;

    @Column(name = "show")
    private boolean show;

    @Column(name = "default_phone")
    private boolean defaultPhone;
}
