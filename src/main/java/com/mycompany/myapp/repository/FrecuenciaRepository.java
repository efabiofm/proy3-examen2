package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Frecuencia;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Frecuencia entity.
 */
@SuppressWarnings("unused")
public interface FrecuenciaRepository extends JpaRepository<Frecuencia,Long> {

}
