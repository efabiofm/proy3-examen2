package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Entrada;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Entrada entity.
 */
@SuppressWarnings("unused")
public interface EntradaRepository extends JpaRepository<Entrada,Long> {

}
