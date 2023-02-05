package com.jdsjara.vendas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jdsjara.vendas.domain.entity.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

}
