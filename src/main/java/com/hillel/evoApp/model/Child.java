package com.hillel.evoApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "children")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Child extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "birth_date")
    private Date birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private Sex sex;
}
