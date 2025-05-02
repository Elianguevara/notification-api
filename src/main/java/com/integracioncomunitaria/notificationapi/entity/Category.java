// src/main/java/com/integracioncomunitaria/notificationapi/entity/Category.java
package com.integracioncomunitaria.notificationapi.entity;

import com.integracioncomunitaria.notificationapi.entity.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "category")
@Getter @Setter
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_category")
    private Integer idCategory;

    @Column(name = "name", length = 45, nullable = false)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Provider> providers;

    public Category() {
    }

    
}
