package com.hillel.evoApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_photos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPhoto extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "resource_url")
    private String resourceUrl;

    @Column(name = "default_photo")
    private boolean defaultPhoto;
}
