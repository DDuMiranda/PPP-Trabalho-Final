package cartas.framework;

import java.util.ArrayList;
import java.util.List;

/**
 * Esqueleto (Template Method) do ciclo de vida de um jogo de cartas.
 * Subclasses implementam os Factory Methods para definir jogadores, baralho e regras.
 */
public abstract class JogoCartas {

    protected List<Jogador> jogadores;
    protected Baralho baralho;
    protected RegraJogada regraJogada;

    private final List<ObservadorJogo> observadores = new ArrayList<>();

    public final void iniciar() {
        jogadores   = criarJogadores();
        baralho     = criarBaralho();
        regraJogada = criarRegraJogada();
        baralho.embaralhar();
        distribuirCartas();
        configurarInicio();
        notificar("JOGO_INICIADO");
    }

    protected abstract List<Jogador> criarJogadores();
    protected abstract Baralho criarBaralho();
    protected abstract RegraJogada criarRegraJogada();
    protected abstract int cartasPorJogador();
    protected abstract boolean verificarFimDeJogo();
    protected abstract String determinarVencedor();

    protected void distribuirCartas() {
        for (int i = 0; i < cartasPorJogador(); i++)
            for (Jogador j : jogadores)
                j.receberCarta(baralho.distribuir());
    }

    protected void configurarInicio() {}

    public void adicionarObservador(ObservadorJogo obs) { observadores.add(obs); }

    protected void notificar(String evento) {
        for (ObservadorJogo o : observadores) o.onEstadoAtualizado(evento);
    }

    protected void notificarFim(String vencedor) {
        for (ObservadorJogo o : observadores) o.onJogoTerminado(vencedor);
    }

    public List<Jogador> getJogadores() { return jogadores; }
    public Baralho getBaralho()         { return baralho; }
    public RegraJogada getRegraJogada() { return regraJogada; }
}
