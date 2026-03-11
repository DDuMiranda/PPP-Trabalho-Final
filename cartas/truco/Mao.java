package cartas.truco;

import cartas.framework.Jogador;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa uma mão (conjunto de 3 vazas) no Truco.
 * Toda a lógica de apostas (truco/seis/nove/doze) está preservada aqui.
 */
public class Mao {
    private final Dupla dupla1;
    private final Dupla dupla2;
    private final List<Jogador> ordemJogadores;
    private final Vaza[] vazas = new Vaza[3];
    private int vazaAtual = 0;
    private int valor = 1;

    private Dupla duplaQueApostou;
    private Dupla duplaDesafiada;
    private boolean apostaEmAndamento = false;
    private Dupla duplaUltimaApostaVencedora = null;

    public Mao(Dupla d1, Dupla d2, List<Jogador> ordem) {
        this.dupla1 = d1;
        this.dupla2 = d2;
        this.ordemJogadores = new ArrayList<>(ordem);
        for (int i = 0; i < 3; i++) vazas[i] = new Vaza();
    }

    public int getVazaAtualIndex()           { return vazaAtual; }
    public Vaza getVazaAtual()               { return vazas[vazaAtual]; }
    public List<Jogador> getOrdemJogadores() { return ordemJogadores; }
    public Vaza[] getVazas()                 { return vazas; }
    public int getValor()                    { return valor; }
    public void setValor(int novoValor)      { this.valor = novoValor; }

    public Dupla getDuplaQueApostou()                    { return duplaQueApostou; }
    public void setDuplaQueApostou(Dupla d)              { this.duplaQueApostou = d; }
    public Dupla getDuplaDesafiada()                     { return duplaDesafiada; }
    public void setDuplaDesafiada(Dupla d)               { this.duplaDesafiada = d; }
    public boolean isApostaEmAndamento()                 { return apostaEmAndamento; }
    public void setApostaEmAndamento(boolean v)          { this.apostaEmAndamento = v; }
    public Dupla getDuplaUltimaApostaVencedora()         { return duplaUltimaApostaVencedora; }
    public void setDuplaUltimaApostaVencedora(Dupla d)   { this.duplaUltimaApostaVencedora = d; }

    /** Retorna o próximo valor de aposta: 1→3→6→9→12 */
    public int getProximoValorAposta() {
        switch (valor) {
            case 1:  return 3;
            case 3:  return 6;
            case 6:  return 9;
            case 9:  return 12;
            default: return 12;
        }
    }

    /** Avança para a próxima vaza, reordenando os jogadores pelo vencedor. */
    public void proximaVaza() {
        if (vazaAtual < 2) {
            Vaza.Jogada jv = vazas[vazaAtual].getJogadaVencedora();
            if (jv != null) {
                int pos = ordemJogadores.indexOf(jv.getJogador());
                Collections.rotate(ordemJogadores, -pos);
            }
            vazaAtual++;
        }
    }

    /**
     * Determina e retorna a dupla vencedora da mão completa.
     * Implementa as regras de vitória do Truco Paulista.
     */
    public Dupla getVencedorDaMao() {
        Dupla[] vencedoresDasVazas = new Dupla[3];
        for (int i = 0; i < 3; i++) {
            if (vazas[i].getJogadas().size() == 4) {
                Vaza.Jogada jv = vazas[i].getJogadaVencedora();
                if (jv != null) vencedoresDasVazas[i] = jv.getDupla();
            }
        }

        // Regras de vencedor após 2a vaza
        if (vazas[1].getJogadas().size() == 4) {
            if (vencedoresDasVazas[0] == null && vencedoresDasVazas[1] != null)
                return vencedoresDasVazas[1];
            if (vencedoresDasVazas[0] != null && vencedoresDasVazas[1] == null)
                return vencedoresDasVazas[0];
        }

        // Contagem de vitórias
        int v1 = 0, v2 = 0;
        for (Dupla d : vencedoresDasVazas) {
            if (d == dupla1) v1++;
            else if (d == dupla2) v2++;
        }
        if (v1 >= 2) return dupla1;
        if (v2 >= 2) return dupla2;

        // Regras da 3a vaza
        if (vazas[2].getJogadas().size() == 4) {
            if (vencedoresDasVazas[0] == null && vencedoresDasVazas[1] == null)
                return vencedoresDasVazas[2];
            if (vencedoresDasVazas[0] != null)
                return vencedoresDasVazas[0];
        }

        return null;
    }
}
