package com.jdsjara.vendas.rest.controller;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.jdsjara.vendas.domain.entity.ItemPedido;
import com.jdsjara.vendas.domain.entity.Pedido;
import com.jdsjara.vendas.domain.enums.StatusPedido;
import com.jdsjara.vendas.rest.dto.AtualizacaoStatusPedidoDTO;
import com.jdsjara.vendas.rest.dto.InformacaoItemPedidoDTO;
import com.jdsjara.vendas.rest.dto.InformacoesPedidoDTO;
import com.jdsjara.vendas.rest.dto.PedidoDTO;
import com.jdsjara.vendas.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

	private PedidoService pedidoService;

	public PedidoController(PedidoService pedidoService) {
		this.pedidoService = pedidoService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Integer save(@RequestBody @Valid PedidoDTO dto) {
		Pedido pedido = pedidoService.salvar(dto);
		return pedido.getId();
	}

	@GetMapping("{id}")
	public InformacoesPedidoDTO getById(@PathVariable Integer id) {
		return pedidoService.obterPedidoCompleto(id).map(p -> converter(p))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado."));

	}

	@PatchMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateStatus(@PathVariable Integer id, @RequestBody AtualizacaoStatusPedidoDTO dto) {

		String novoStatus = dto.getNovoStatus();
		pedidoService.atualizaStatus(id, StatusPedido.valueOf(novoStatus));

	}

	private InformacoesPedidoDTO converter(Pedido pedido) {
		return InformacoesPedidoDTO.builder().codigo(pedido.getId())
				.dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
				.cpf(pedido.getCliente().getCpf()).nomeCliente(pedido.getCliente().getNome()).total(pedido.getTotal())
				.status(pedido.getStatus().name()).items(converter(pedido.getItens())).build();
	}

	private List<InformacaoItemPedidoDTO> converter(List<ItemPedido> itens) {

		if (CollectionUtils.isEmpty(itens)) {
			return Collections.emptyList();
		}

		return itens.stream()
				.map(item -> InformacaoItemPedidoDTO.builder().descricaoProduto(item.getProduto().getDescricao())
						.precoUnitario(item.getProduto().getPreco()).quantidade(item.getQuantidade()).build())
				.collect(Collectors.toList());
	}

}
