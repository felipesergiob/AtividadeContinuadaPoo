package br.edu.cesarschool.cc.poo.ac.passagem;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;

public class BilheteDAO {
    private CadastroObjetos cadastro = new CadastroObjetos(Bilhete.class);

    private String obterIdUnico(Bilhete bilhete) {
        return bilhete.gerarNumero();
    }

    public Bilhete buscar(String numeroBilhete) {
        return (Bilhete) cadastro.buscar(numeroBilhete);
    }

    public boolean incluir(Bilhete bilhete) {
        String idUnico = obterIdUnico(bilhete);
        Bilhete bilheteExistente = buscar(idUnico);
        if (bilheteExistente != null) {
            return false;
        }
        cadastro.incluir(bilhete, idUnico);
        return true;
    }

    public boolean alterar(Bilhete bilhete) {
        String idUnico = obterIdUnico(bilhete);
        Bilhete bilheteExistente = buscar(idUnico);
        if (bilheteExistente != null) {
            cadastro.alterar(bilhete, idUnico);
            return true;
        }
        return false;
    }

    public boolean excluir(String numeroBilhete) {
        Bilhete bilheteExistente = buscar(numeroBilhete);
        if (bilheteExistente != null) {
            cadastro.excluir(numeroBilhete);
            return true;
        }
        return false;
    }
}
