package com.fhzalves.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.fhzalves.cursomc.domain.Cliente;
import com.fhzalves.cursomc.domain.ItemPedido;
import com.fhzalves.cursomc.domain.PagamentoComBoleto;
import com.fhzalves.cursomc.domain.Pedido;
import com.fhzalves.cursomc.domain.enums.EstadoPagamento;
import com.fhzalves.cursomc.domain.enums.Perfil;
import com.fhzalves.cursomc.repositories.ItemPedidoRepository;
import com.fhzalves.cursomc.repositories.PagamentoRepository;
import com.fhzalves.cursomc.repositories.PedidoRepository;
import com.fhzalves.cursomc.security.UserSS;
import com.fhzalves.cursomc.services.exceptions.AuthorizationException;
import com.fhzalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	public Pedido find(Integer id) {

		UserSS userSS = UserService.authenticated();
		
		Optional<Pedido> obj = repo.findById(id);
		
		if(!obj.equals(null)) {
			if(userSS==null || !userSS.hasRole(Perfil.ADMIN) && 
							   !obj.get().getCliente().getId().equals(userSS.getId())) {

				throw new AuthorizationException("Acesso Negado");
			}
		}	
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
	
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		emailService.sendOrderConfirmationEmail(obj);
		return obj;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente =  clienteService.find(user.getId());
		return repo.findByCliente(cliente, pageRequest);
	}
	
}
