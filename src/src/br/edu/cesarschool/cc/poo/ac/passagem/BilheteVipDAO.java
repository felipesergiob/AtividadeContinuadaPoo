package br.edu.cesarschool.cc.poo.ac.passagem;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;

public class BilheteVipDAO {
    private CadastroObjetos cadastro = new CadastroObjetos(BilheteVip.class);

    private String obterIdUnico(BilheteVip bilheteVip) {
        return bilheteVip.gerarNumero();
    }

    public BilheteVip buscar(String numeroBilhete) {
        return (BilheteVip) cadastro.buscar(numeroBilhete);
    }

    public boolean incluir(BilheteVip bilheteVip) {
        String idUnico = obterIdUnico(bilheteVip);
        BilheteVip bilheteVipExistente = buscar(idUnico);
        if (bilheteVipExistente != null) {
            return false;
        }
        cadastro.incluir(bilheteVip, idUnico);
        return true;
    }


    public boolean alterar(BilheteVip bilheteVip) {
        String idUnico = obterIdUnico(bilheteVip);
        BilheteVip bilheteVipExistente = buscar(idUnico);
        if (bilheteVipExistente != null) {
            cadastro.alterar(bilheteVip, idUnico);
            return true;
        }
        return false;
    }

    public boolean excluir(String numeroBilhete) {
        BilheteVip bilheteVipExistente = buscar(numeroBilhete);
        if (bilheteVipExistente != null) {
            cadastro.excluir(numeroBilhete);
            return true;
        }
        return false;
    }
}
