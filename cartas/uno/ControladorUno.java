package cartas.uno;

import cartas.framework.Jogador;
import javax.swing.Timer;

public class ControladorUno {

    private final JogoUno jogo;
    private final TelaUno tela;

    private boolean aguardandoCorHumano = false;
    private CartaUno cartaPendente = null;

    public ControladorUno() {
        this.jogo = new JogoUno();
        this.tela = new TelaUno(this);
    }

    public void iniciarNovoJogo() {
        jogo.iniciar();
        tela.atualizar();
        iniciarTurno();
    }

    private void iniciarTurno() {
        if (jogo.verificarFimDeJogo()) {
            tela.setStatus("FIM DE JOGO! Vencedor: " + jogo.determinarVencedor());
            tela.habilitarCartas(false);
            tela.habilitarCompra(false);
            return;
        }

        tela.atualizar();
        EstadoUno estado = jogo.getEstado();
        Jogador vez = estado.getJogadorAtual();
        int pendentes = estado.getCartasParaComprar();

        boolean temEmpilhavel = vez.getMao().stream()
                .anyMatch(c -> jogo.getRegraJogada().podeJogar(c, estado));

        if (pendentes > 0) {
            tela.setStatus(temEmpilhavel
                    ? "Vez de: " + vez.getNome() + " — empilhe ou compre " + pendentes + " cartas!"
                    : "Vez de: " + vez.getNome() + " — compre " + pendentes + " cartas!");
        } else {
            tela.setStatus(temEmpilhavel
                    ? "Vez de: " + vez.getNome()
                    : "Vez de: " + vez.getNome() + " — sem jogadas, compre uma carta.");
        }

        tela.habilitarCartas(true);
        tela.habilitarCompra(true);
        tela.atualizarIndicadorSentido(estado.isSentidoHorario());
    }

    public void processarJogadaHumano(CartaUno carta) {
        EstadoUno estado = jogo.getEstado();
        if (!jogo.getRegraJogada().podeJogar(carta, estado)) {
            tela.setStatus("Jogada inválida!");
            return;
        }
        if (carta.isCoringa()) {
            aguardandoCorHumano = true;
            cartaPendente = carta;
            tela.habilitarCartas(false);
            tela.habilitarCompra(false);
            tela.mostrarSeletorCor(true);
            tela.setStatus("Escolha uma cor:");
        } else {
            jogarCarta(carta, null);
        }
    }

    public void processarEscolhaCor(String cor) {
        if (!aguardandoCorHumano || cartaPendente == null) return;
        tela.mostrarSeletorCor(false);
        aguardandoCorHumano = false;
        jogarCarta(cartaPendente, cor);
        cartaPendente = null;
    }

    private void jogarCarta(CartaUno carta, String cor) {
        jogo.jogarCarta(carta, cor);
        tela.habilitarCartas(false);
        tela.habilitarCompra(false);
        tela.atualizar();
        jogo.avancarTurno();
        // Consome o pulo pendente (Bloqueio) se não houver compras acumuladas
        if (jogo.getEstado().isPulaPróximo() && jogo.getEstado().getCartasParaComprar() == 0) {
            jogo.getEstado().avancarJogador();
            jogo.getEstado().setPulaPróximo(false);
        }
        new Timer(500, e -> {
            ((Timer) e.getSource()).stop();
            iniciarTurno();
        }).start();
    }

    public void processarCompra() {
        EstadoUno estado = jogo.getEstado();
        boolean temEmpilhavel = estado.getJogadorAtual().getMao().stream()
                .anyMatch(c -> jogo.getRegraJogada().podeJogar(c, estado));
        if (temEmpilhavel) {
            tela.setStatus("Você tem cartas válidas para jogar!");
            return;
        }

        if (estado.getCartasParaComprar() > 0) {
            // Aplica +2/+4 acumulados, já pula o turno de quem comprou
            jogo.aplicarComprasPendentes();
            tela.habilitarCartas(false);
            tela.habilitarCompra(false);
            tela.atualizar();
            new Timer(500, e -> {
                ((Timer) e.getSource()).stop();
                iniciarTurno();
            }).start();
        } else {
            CartaUno comprada = jogo.comprarCarta();
            tela.atualizar();
            tela.habilitarCompra(false);
            if (comprada != null && jogo.getRegraJogada().podeJogar(comprada, estado)) {
                tela.setStatus("Comprou: pode jogar agora!");
                tela.habilitarCartas(true);
            } else {
                tela.setStatus("Comprou uma carta e passou a vez.");
                tela.habilitarCartas(false);
                jogo.avancarTurno();
                new Timer(500, e -> {
                    ((Timer) e.getSource()).stop();
                    iniciarTurno();
                }).start();
            }
        }
    }

    public JogoUno getJogo() { return jogo; }
}
