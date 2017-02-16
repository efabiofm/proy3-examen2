package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Solicitud;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Solicitud entity.
 */
@SuppressWarnings("unused")
public interface SolicitudRepository extends JpaRepository<Solicitud,Long> {

    @Query("select distinct solicitud from Solicitud solicitud left join fetch solicitud.tipos")
    List<Solicitud> findAllWithEagerRelationships();

    @Query("select solicitud from Solicitud solicitud left join fetch solicitud.tipos where solicitud.id =:id")
    Solicitud findOneWithEagerRelationships(@Param("id") Long id);

}
