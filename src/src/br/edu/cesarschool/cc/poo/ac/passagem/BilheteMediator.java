package br.edu.cesarschool.cc.poo.ac.passagem;

import java.time.LocalDateTime;
import br.edu.cesarschool.cc.poo.ac.cliente.Cliente;
import br.edu.cesarschool.cc.poo.ac.cliente.ClienteMediator;
import br.edu.cesarschool.cc.poo.ac.utils.ValidadorCPF;

public class BilheteMediator {
    private static BilheteMediator instancia = new BilheteMediator();
    private BilheteDAO bilheteDao = new BilheteDAO();
    private BilheteVipDAO bilheteVipDao = new BilheteVipDAO();
    private VooMediator vooMediator = VooMediator.obterInstancia();
    private ClienteMediator clienteMediator = ClienteMediator.obterInstancia();

    private BilheteMediator() {}

    public static BilheteMediator obterInstancia() {
        return instancia;
    }

    public Bilhete buscar(String numeroBilhete) {
        return bilheteDao.buscar(numeroBilhete);
    }

    public BilheteVip buscarVip(String numeroBilhete) {
        return bilheteVipDao.buscar(numeroBilhete);
    }

    public String validar(String cpf, String ciaAerea, int numeroVoo, double preco, double pagamentoEmPontos, LocalDateTime dataHora) {
        if (!ValidadorCPF.isCpfValido(cpf)) {
            return "CPF errado";
        }
        String validacaoCiaNumero = vooMediator.validarCiaNumero(ciaAerea, numeroVoo);
        if (validacaoCiaNumero != null) {
            return validacaoCiaNumero;
        }
        if (preco <= 0) {
            return "Preco errado";
        }
        if (pagamentoEmPontos < 0) {
            return "Pagamento pontos errado";
        }
        if (preco < pagamentoEmPontos) {
            return "Preco menor que pagamento em pontos";
        }
        if (dataHora.isBefore(LocalDateTime.now().plusHours(1))) {
            return "data hora invalida";
        }
        return null;
    }

    public ResultadoGeracaoBilhete gerarBilhete(String cpf, String ciaAerea, int numeroVoo, double preco, double pagamentoEmPontos, LocalDateTime dataHora) {
        String validacao = validar(cpf, ciaAerea, numeroVoo, preco, pagamentoEmPontos, dataHora);
        if (validacao != null) {
            return new ResultadoGeracaoBilhete(null, null, validacao);
        }
        Voo voo = new Voo(null, null, ciaAerea, numeroVoo);
        String idVoo = voo.obterIdVoo();
        Voo vooEncontrado = vooMediator.buscar(idVoo);
        if (vooEncontrado == null) {
            return new ResultadoGeracaoBilhete(null, null, "Voo nao encontrado");
        }
        Cliente cliente = clienteMediator.buscar(cpf);
        if (cliente == null) {
            return new ResultadoGeracaoBilhete(null, null, "Cliente nao encontrado");
        }
        double valorPontosNecessario = pagamentoEmPontos * 20;
        if (cliente.getSaldoPontos() < valorPontosNecessario) {
            return new ResultadoGeracaoBilhete(null, null, "Pontos insuficientes");
        }
        Bilhete bilhete = new Bilhete(cliente, vooEncontrado, preco, pagamentoEmPontos, dataHora);
        cliente.debitarPontos(valorPontosNecessario);
        cliente.creditarPontos(bilhete.obterValorPontuacao());
        if (!bilheteDao.incluir(bilhete)) {
            return new ResultadoGeracaoBilhete(null, null, "Bilhete ja existente");
        }
        if (clienteMediator.alterar(cliente) != null) {
            return new ResultadoGeracaoBilhete(null, null, "Erro ao atualizar cliente");
        }
        return new ResultadoGeracaoBilhete(bilhete, null, null);
    }

    public ResultadoGeracaoBilhete gerarBilheteVip(String cpf, String ciaAerea, int numeroVoo, double preco, double pagamentoEmPontos, LocalDateTime dataHora, double bonusPontuacao) {
        String validacao = validar(cpf, ciaAerea, numeroVoo, preco, pagamentoEmPontos, dataHora);
        if (validacao != null) {
            return new ResultadoGeracaoBilhete(null, null, validacao);
        }
        if (bonusPontuacao <= 0 || bonusPontuacao > 100) {
            return new ResultadoGeracaoBilhete(null, null, "Bonus errado");
        }
        Voo voo = new Voo(null, null, ciaAerea, numeroVoo);
        String idVoo = voo.obterIdVoo();
        Voo vooEncontrado = vooMediator.buscar(idVoo);
        if (vooEncontrado == null) {
            return new ResultadoGeracaoBilhete(null, null, "Voo nao encontrado");
        }
        Cliente cliente = clienteMediator.buscar(cpf);
        if (cliente == null) {
            return new ResultadoGeracaoBilhete(null, null, "Cliente nao encontrado");
        }
        double valorPontosNecessario = pagamentoEmPontos * 20;
        if (cliente.getSaldoPontos() < valorPontosNecessario) {
            return new ResultadoGeracaoBilhete(null, null, "Pontos insuficientes");
        }
        BilheteVip bilheteVip = new BilheteVip(cliente, vooEncontrado, preco, pagamentoEmPontos, dataHora, bonusPontuacao);
        cliente.debitarPontos(valorPontosNecessario);
        cliente.creditarPontos(bilheteVip.obterValorPontuacaoVip());
        if (!bilheteVipDao.incluir(bilheteVip)) {
            return new ResultadoGeracaoBilhete(null, null, "Bilhete ja existente");
        }
        if (clienteMediator.alterar(cliente) != null) {
            return new ResultadoGeracaoBilhete(null, null, "Erro ao atualizar cliente");
        }
        return new ResultadoGeracaoBilhete(null, bilheteVip, null);
    }

}
