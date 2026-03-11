package cartas.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Jogador {
    private final String nome;
    private final boolean isBot;
    private final List<Carta> mao = new ArrayList<>();

    public Jogador(String nome, boolean isBot) {
        this.nome  = nome;
        this.isBot = isBot;
    }

    public String getNome()     { return nome; }
    public boolean isBot()      { return isBot; }
    public List<Carta> getMao() { return mao; }

    public void receberCarta(Carta carta) { mao.add(carta); }
    public void limparMao()              { mao.clear(); }

    public Carta jogarCartaAleatoriamente() {
        if (mao.isEmpty()) return null;
        return mao.remove(new Random().nextInt(mao.size()));
    }
}
