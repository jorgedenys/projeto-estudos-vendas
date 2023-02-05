package com.jdsjara.vendas.service;

import java.util.Optional;

import com.jdsjara.vendas.domain.entity.Pedido;
import com.jdsjara.vendas.domain.enums.StatusPedido;
import com.jdsjara.vendas.rest.dto.PedidoDTO;

public interface PedidoService {

	Pedido salvar(PedidoDTO dto);
	
	Optional<Pedido> obterPedidoCompleto(Integer id);
	
	void atualizaStatus(Integer id, StatusPedido statusPedido);

}
