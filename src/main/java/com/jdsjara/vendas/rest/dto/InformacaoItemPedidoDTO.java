package com.jdsjara.vendas.rest.dto;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public class InformacaoItemPedidoDTO {

	private String descricaoProduto;
	private BigDecimal precoUnitario;
	private Integer quantidade;
	
}
