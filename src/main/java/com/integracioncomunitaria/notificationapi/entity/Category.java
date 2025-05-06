package com.integracioncomunitaria.notificationapi.entity;

import com.integracioncomunitaria.notificationapi.entity.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Entidad que representa una categoría de proveedor.
 * Hereda de BaseEntity para incluir campos de auditoría (quién creó/modificó y cuándo).
 */
@Entity
@Table(name = "category")
@Getter
@Setter
public class Category extends BaseEntity {

    /**
     * Clave primaria de la categoría.
     * Se genera automáticamente por la base de datos (IDENTITY).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_category")
    private Integer idCategory;

    /**
     * Nombre de la categoría.
     * No puede ser nulo y tiene un máximo de 45 caracteres.
     */
    @Column(name = "name", length = 45, nullable = false)
    private String name;

    /**
     * Relación uno-a-muchos con la entidad Provider.
     * - mappedBy: indica que el campo 'category' en Provider es el dueño de la relación.
     * - cascade = ALL: operaciones sobre Category se propagan a sus Providers.
     * - orphanRemoval = true: si se elimina un Provider de la lista, se borra de la base de datos.
     */
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Provider> providers;

    /**
     * Constructor por defecto necesario para JPA.
     */
    public Category() {
    }

    /**
     * Constructor de conveniencia para instanciar una categoría con solo su ID.
     * Útil para asignar la referencia sin necesidad de cargar toda la entidad.
     *
     * @param idCategory ID de la categoría.
     */
    public Category(Integer idCategory) {
        this.idCategory = idCategory;
    }
}
