package com.fhzalves.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fhzalves.cursomc.domain.Categoria;

@Repository
public interface CategoriasRepository extends JpaRepository<Categoria, Integer> {

}
