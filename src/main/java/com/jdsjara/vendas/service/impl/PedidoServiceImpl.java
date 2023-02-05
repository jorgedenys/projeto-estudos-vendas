package com.jdsjara.vendas.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jdsjara.vendas.domain.entity.Cliente;
import com.jdsjara.vendas.domain.entity.ItemPedido;
import com.jdsjara.vendas.domain.entity.Pedido;
import com.jdsjara.vendas.domain.entity.Produto;
import com.jdsjara.vendas.domain.enums.StatusPedido;
import com.jdsjara.vendas.domain.repository.ClienteRepository;
import com.jdsjara.vendas.domain.repository.ItemPedidoRepository;
import com.jdsjara.vendas.domain.repository.PedidoRepository;
import com.jdsjara.vendas.domain.repository.ProdutoRepository;
import com.jdsjara.vendas.exception.PedidoNaoEncontradoException;
import com.jdsjara.vendas.exception.RegraNegocioException;
import com.jdsjara.vendas.rest.dto.ItemPedidoDTO;
import com.jdsjara.vendas.rest.dto.PedidoDTO;
import com.jdsjara.vendas.service.PedidoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

	private final PedidoRepository pedidoRepository;
	private final ClienteRepository clienteRepository;
	private final ProdutoRepository produtoRepository;
	private final ItemPedidoRepository itemPedidoRepository;
	
	@Override
	@Transactional
	public Pedido salvar(PedidoDTO dto) {
		Integer idCliente = dto.getCliente();
		Cliente cliente = clienteRepository.findById(idCliente)
				.orElseThrow(() -> new RegraNegocioException("Código de cliente inválido."));

		Pedido pedido = new Pedido();
		pedido.setTotal(dto.getTotal());
		pedido.setDataPedido(LocalDate.now());
		pedido.setCliente(cliente);

		List<ItemPedido> itemsPedido = converterItems(pedido, dto.getItens());
		pedidoRepository.save(pedido);
		itemPedidoRepository.saveAll(itemsPedido);
		pedido.setItens(itemsPedido);

		return pedido;
	}

	private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDTO> items) {
		
		if (items.isEmpty()) {
			throw new RegraNegocioException("Não é possível realizar um pedido sem itens.");
		}

		return items
				.stream()
				.map(dto -> {
					Integer idProduto = dto.getProduto();
					Produto produto = produtoRepository.findById(idProduto)
							.orElseThrow(() -> new RegraNegocioException("Código de produto inválido: " + idProduto));

					ItemPedido itemPedido = new ItemPedido();
					itemPedido.setQuantidade(dto.getQuantidade());
					itemPedido.setPedido(pedido);
					itemPedido.setProduto(produto);
					return itemPedido;
		}).collect(Collectors.toList());

	}

	@Override
	public Optional<Pedido> obterPedidoCompleto(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    @Transactional
    public void atualizaStatus( Integer id, StatusPedido statusPedido ) {
		pedidoRepository
                .findById(id)
                .map( pedido -> {
                    pedido.setStatus(statusPedido);
                    return pedidoRepository.save(pedido);
                }).orElseThrow(() -> new PedidoNaoEncontradoException() );
    }

}
