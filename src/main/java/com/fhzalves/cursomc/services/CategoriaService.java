package com.fhzalves.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fhzalves.cursomc.domain.Categoria;
import com.fhzalves.cursomc.repositories.CategoriasRepository;

@Service
public class CategoriaService {

	@Autowired
	private CategoriasRepository repo;
	
	
	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElse(null);
	}
	
}
