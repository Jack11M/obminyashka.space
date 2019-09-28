package com.hillel.evoApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "advertisement_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementImage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_id")
    private Advertisement advertisement;

    @Column(name = "resource_url")
    private String resourceUrl;

    @Column(name = "default_photo")
    private boolean defaultPhoto;
}
