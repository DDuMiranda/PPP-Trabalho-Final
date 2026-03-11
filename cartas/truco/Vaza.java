package cartas.truco;

import cartas.framework.Carta;
import cartas.framework.Jogador;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma vaza (rodada) do Truco.
 * A comparação de forças usa carta.getForca(), definido em CartaTruco,
 * eliminando a lógica duplicada que existia no código original.
 */
public class Vaza {
    private final List<Jogada> jogadas = new ArrayList<>();

    public void adicionarJogada(Jogador jogador, Carta carta, Dupla dupla) {
        jogadas.add(new Jogada(jogador, carta, dupla));
    }

    public List<Jogada> getJogadas() { return jogadas; }

    /**
     * Retorna a jogada vencedora da vaza.
     * Se houver empate entre duplas diferentes, retorna null (empate).
     * Se houver empate dentro da mesma dupla, retorna a primeira.
     */
    public Jogada getJogadaVencedora() {
        if (jogadas.isEmpty()) return null;

        // Encontra a força máxima
        int forcaMaxima = -1;
        for (Jogada j : jogadas) {
            int f = j.getCarta().getForca();
            if (f > forcaMaxima) forcaMaxima = f;
        }

        // Coleta todas as jogadas com força máxima
        List<Jogada> maisFortes = new ArrayList<>();
        for (Jogada j : jogadas)
            if (j.getCarta().getForca() == forcaMaxima)
                maisFortes.add(j);

        if (maisFortes.size() == 1) return maisFortes.get(0);

        // Verifica se todas as mais fortes são da mesma dupla
        Dupla primeiraDupla = maisFortes.get(0).getDupla();
        for (int i = 1; i < maisFortes.size(); i++)
            if (maisFortes.get(i).getDupla() != primeiraDupla)
                return null; // empate entre duplas diferentes

        return maisFortes.get(0);
    }

    // ---- Classe interna ----

    public static class Jogada {
        private final Jogador jogador;
        private final Carta   carta;
        private final Dupla   dupla;

        public Jogada(Jogador j, Carta c, Dupla d) {
            this.jogador = j;
            this.carta   = c;
            this.dupla   = d;
        }

        public Jogador getJogador() { return jogador; }
        public Carta   getCarta()   { return carta; }
        public Dupla   getDupla()   { return dupla; }
    }
}
