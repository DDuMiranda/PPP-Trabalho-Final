package cartas.truco;

import cartas.framework.*;
import java.util.List;

public class JogoTruco extends JogoCartas {

    @Override
    protected List<Jogador> criarJogadores() {
        return List.of(
            new Jogador("Você",           false),
            new Jogador("Bot Parceiro",   true),
            new Jogador("Bot Oponente 1", true),
            new Jogador("Bot Oponente 2", true)
        );
    }

    @Override protected Baralho criarBaralho()         { return new BaralhoTruco(); }
    @Override protected RegraJogada criarRegraJogada() { return new RegraJogadaTruco(); }
    @Override protected int cartasPorJogador()          { return 3; }
    @Override protected boolean verificarFimDeJogo()    { return false; }
    @Override protected String determinarVencedor()     { return ""; }

    public void redistribuir() {
        for (Jogador j : jogadores) j.limparMao();
        baralho = criarBaralho();
        baralho.embaralhar();
        distribuirCartas();
    }
}
