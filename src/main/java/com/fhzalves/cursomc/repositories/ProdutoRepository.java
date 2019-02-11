package com.fhzalves.cursomc.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fhzalves.cursomc.domain.Categoria;
import com.fhzalves.cursomc.domain.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
	@Transactional(readOnly = true)
	/*
	 * @Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.nome LIKE %:nome% AND cat IN :categorias"
	 * ) 
	 * Page<Produto> search(@Param("nome") String nome,@Param("categorias")List<Categoria> categorias, Pageable pageRequest);
	 */
	// Seguindo o padrão de nomes do Spring Data é possivel substituir a Query
	Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome, List<Categoria> categorias,
			Pageable pageRequest);

}
