package cartas.uno;

import cartas.framework.*;
import java.util.ArrayList;
import java.util.List;

public class JogoUno extends JogoCartas {

    private EstadoUno estado;
    private List<CartaUno> pilhaDescarte = new ArrayList<>();

    @Override
    protected List<Jogador> criarJogadores() {
        List<Jogador> lista = new ArrayList<>();
        lista.add(new Jogador("Jogador 1", false));
        lista.add(new Jogador("Jogador 2", false));
        lista.add(new Jogador("Jogador 3", false));
        lista.add(new Jogador("Jogador 4", false));
        return lista;
    }

    @Override protected Baralho criarBaralho()         { return new BaralhoUno(); }
    @Override protected RegraJogada criarRegraJogada() { return new RegraJogadaUno(); }
    @Override protected int cartasPorJogador()          { return 7; }

    @Override
    protected void configurarInicio() {
        estado = new EstadoUno(jogadores);
        CartaUno primeira;
        do { primeira = (CartaUno) baralho.distribuir(); } while (primeira.isCoringa());
        pilhaDescarte.add(primeira);
        estado.setTopoDescarte(primeira);
        estado.setCorAtual(primeira.getCor());
    }

    public boolean jogarCarta(CartaUno carta, String corEscolhida) {
        if (!regraJogada.podeJogar(carta, estado)) return false;

        estado.getJogadorAtual().getMao().remove(carta);
        pilhaDescarte.add(carta);
        estado.setTopoDescarte(carta);

        if (carta.isCoringa() && corEscolhida != null) {
            estado.setCorAtual(corEscolhida);
        } else if (!carta.isCoringa()) {
            estado.setCorAtual(carta.getCor());
        }

        switch (carta.getTipo()) {
            case MAIS_DOIS   -> { estado.adicionarCartasParaComprar(2); estado.setPulaPróximo(true); }
            case MAIS_QUATRO -> { estado.adicionarCartasParaComprar(4); estado.setPulaPróximo(true); }
            case BLOQUEIO    -> estado.setPulaPróximo(true);
            case INVERTER    -> estado.inverterSentido();
            default          -> estado.setCartasParaComprar(0);
        }

        notificar("CARTA_JOGADA");
        return true;
    }

    public void aplicarComprasPendentes() {
        int n = estado.getCartasParaComprar();
        if (n > 0) {
            Jogador alvo = estado.getJogadorAtual();
            for (int i = 0; i < n; i++) {
                if (baralho.isEmpty()) reembaralharDescarte();
                Carta c = baralho.distribuir();
                if (c != null) alvo.receberCarta(c);
            }
            estado.setCartasParaComprar(0);
            // Pula o turno de quem comprou
            if (estado.isPulaPróximo()) {
                estado.avancarJogador();
                estado.setPulaPróximo(false);
            }
        }
    }

    public CartaUno comprarCarta() {
        if (baralho.isEmpty()) reembaralharDescarte();
        if (baralho.isEmpty()) return null;
        CartaUno c = (CartaUno) baralho.distribuir();
        estado.getJogadorAtual().receberCarta(c);
        notificar("CARTA_COMPRADA");
        return c;
    }

    private void reembaralharDescarte() {
        if (pilhaDescarte.size() <= 1) return;
        CartaUno topo = pilhaDescarte.remove(pilhaDescarte.size() - 1);
        baralho.getCartas().addAll(pilhaDescarte);
        pilhaDescarte.clear();
        pilhaDescarte.add(topo);
        baralho.embaralhar();
    }

    public void avancarTurno() {
        estado.avancarJogador();
        notificar("TURNO_AVANCADO");
    }

    @Override public boolean verificarFimDeJogo() {
        return jogadores.stream().anyMatch(j -> j.getMao().isEmpty());
    }

    @Override public String determinarVencedor() {
        return jogadores.stream()
                .filter(j -> j.getMao().isEmpty())
                .map(Jogador::getNome)
                .findFirst().orElse("?");
    }

    public EstadoUno getEstado()             { return estado; }
    public List<CartaUno> getPilhaDescarte() { return pilhaDescarte; }
}
