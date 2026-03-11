package cartas.truco;

import cartas.framework.Jogador;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma dupla de jogadores no Truco.
 */
public class Dupla {
    private final String nome;
    private final List<Jogador> jogadores = new ArrayList<>();
    private int pontosNoJogo = 0;

    public Dupla(String nome, Jogador j1, Jogador j2) {
        this.nome = nome;
        jogadores.add(j1);
        jogadores.add(j2);
    }

    public String getNome()             { return nome; }
    public List<Jogador> getJogadores() { return jogadores; }
    public int getPontosNoJogo()        { return pontosNoJogo; }
    public void adicionaPontos(int p)   { pontosNoJogo += p; }
    public boolean venceu()             { return pontosNoJogo > 12; }
}
