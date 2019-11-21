package com.hillel.evoApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.List;

@EqualsAndHashCode(callSuper = true, exclude = {"users"})
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deal extends BaseEntity {

    @ManyToMany(mappedBy = "deals", fetch = FetchType.LAZY)
    private List<User> users;
}
