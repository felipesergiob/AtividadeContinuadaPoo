package br.edu.cesarschool.cc.poo.ac.cliente;

import br.edu.cesarschool.cc.poo.ac.utils.ValidadorCPF;
import br.edu.cesarschool.cc.poo.ac.utils.StringUtils;

public class ClienteMediator {
	private static ClienteMediator instancia = new ClienteMediator();
	private ClienteDAO clienteDao = new ClienteDAO();

	private ClienteMediator() {}

	public static ClienteMediator obterInstancia() {
		return instancia;
	}

	public Cliente buscar(String cpf) {
		return clienteDao.buscar(cpf);
	}

	public String validar(Cliente cliente) {
		if (!ValidadorCPF.isCpfValido(cliente.getCpf())) {
			return "CPF errado";
		}
		if (StringUtils.isVaziaOuNula(cliente.getNome()) || cliente.getNome().length() < 2) {
			return "nome errado";
		}
		if (cliente.getSaldoPontos() < 0) {
			return "saldo errado";
		}
		return null;
	}

	public String incluir(Cliente cliente) {
		String validacao = validar(cliente);
		if (validacao != null) {
			return validacao;
		}
		boolean incluido = clienteDao.incluir(cliente);
		if (!incluido) {
			return "Cliente ja existente";
		}
		return null;
	}

	public String alterar(Cliente cliente) {
		String validacao = validar(cliente);
		if (validacao != null) {
			return validacao;
		}
		boolean alterado = clienteDao.alterar(cliente);
		if (!alterado) {
			return "Cliente inexistente";
		}
		return null;
	}

	public String excluir(String cpf) {
		if (!ValidadorCPF.isCpfValido(cpf)) {
			return "CPF errado";
		}
		boolean excluido = clienteDao.excluir(cpf);
		if (!excluido) {
			return "Cliente inexistente";
		}
		return null;
	}
}
