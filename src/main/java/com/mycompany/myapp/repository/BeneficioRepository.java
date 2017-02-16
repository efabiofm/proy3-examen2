package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Beneficio;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Beneficio entity.
 */
@SuppressWarnings("unused")
public interface BeneficioRepository extends JpaRepository<Beneficio,Long> {

}
