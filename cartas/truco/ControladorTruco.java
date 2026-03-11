package cartas.truco;

import cartas.framework.Carta;
import cartas.framework.Jogador;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.Timer;

public class ControladorTruco {
    private TelaTruco tela;
    private final JogoTruco jogo;
    private Dupla dupla1, dupla2;
    private Mao mao;
    private List<Jogador> ordemDeJogadores;
    private int jogadorDaVezIndex = 0;
    private boolean jogoIniciado = false;
    private final Random random = new Random();

    public ControladorTruco() {
        this.jogo = new JogoTruco();
        this.tela = new TelaTruco(this);
    }

    public void iniciarNovoJogo() {
        jogo.iniciar();
        List<Jogador> js = jogo.getJogadores();
        Jogador j1 = js.get(0), j2 = js.get(1), j3 = js.get(2), j4 = js.get(3);
        dupla1 = new Dupla("Nós",  j1, j2);
        dupla2 = new Dupla("Eles", j3, j4);
        ordemDeJogadores = new ArrayList<>(List.of(j1, j3, j2, j4));
        jogoIniciado = true;
        tela.configurarBotoesDeControle(false, false);
        iniciarNovaMao();
    }

    public void iniciarNovaMao() {
        if (!jogoIniciado) return;
        jogo.redistribuir();
        List<Jogador> js = jogo.getJogadores();
        for (int i = 0; i < ordemDeJogadores.size(); i++) {
            String nome = ordemDeJogadores.get(i).getNome();
            for (Jogador j : js)
                if (j.getNome().equals(nome)) { ordemDeJogadores.set(i, j); break; }
        }
        mao = new Mao(dupla1, dupla2, ordemDeJogadores);
        jogadorDaVezIndex = 0;
        tela.atualizar(mao, dupla1, dupla2);
        proximoTurno();
    }

    public void processarAcaoHumano(String acao) {
        switch (acao) {
            case "AUMENTAR" -> {
                if (mao.isApostaEmAndamento()) mao.setValor(mao.getProximoValorAposta());
                iniciarAposta(dupla1);
            }
            case "ACEITAR" -> {
                mao.setValor(mao.getProximoValorAposta());
                mao.setApostaEmAndamento(false);
                mao.setDuplaUltimaApostaVencedora(mao.getDuplaQueApostou());
                tela.setStatus("Você ACEITOU! A mão vale " + mao.getValor() + " tentos.");
                tela.atualizar(mao, dupla1, dupla2);
                atualizarBotoesParaTurno();
                proximoTurno();
            }
            case "CORRER" -> {
                tela.setStatus("Você CORREU! " + mao.getDuplaQueApostou().getNome()
                        + " ganhou " + mao.getValor() + " tento(s).");
                mao.getDuplaQueApostou().adicionaPontos(mao.getValor());
                concluirMao(false);
            }
        }
    }

    private void iniciarAposta(Dupla duplaApostadora) {
        int prox = mao.getProximoValorAposta();
        Dupla desafiada = (duplaApostadora == dupla1) ? dupla2 : dupla1;
        mao.setDuplaQueApostou(duplaApostadora);
        mao.setDuplaDesafiada(desafiada);
        mao.setApostaEmAndamento(true);
        tela.setStatus(duplaApostadora.getNome() + " pediu " + (prox == 3 ? "TRUCO" : "VALE " + prox) + "!");
        atualizarBotoesParaTurno();
        if (desafiada == dupla2) botRespondeAposta();
    }

    private void botRespondeAposta() {
        tela.habilitarCartas(false);
        new Timer(2000, e -> {
            ((Timer) e.getSource()).stop();
            if (random.nextBoolean()) {
                tela.setStatus("O adversário CORREU! Você ganhou " + mao.getValor() + " tento(s).");
                mao.getDuplaQueApostou().adicionaPontos(mao.getValor());
                concluirMao(false);
            } else {
                boolean podeAumentar = dupla2 != mao.getDuplaUltimaApostaVencedora();
                if (podeAumentar && mao.getProximoValorAposta() <= 12 && random.nextBoolean()) {
                    mao.setValor(mao.getProximoValorAposta());
                    iniciarAposta(dupla2);
                } else {
                    mao.setValor(mao.getProximoValorAposta());
                    mao.setApostaEmAndamento(false);
                    mao.setDuplaUltimaApostaVencedora(mao.getDuplaQueApostou());
                    tela.setStatus("O adversário ACEITOU! A mão vale " + mao.getValor() + " tentos.");
                    tela.atualizar(mao, dupla1, dupla2);
                    atualizarBotoesParaTurno();
                    new Timer(1500, e2 -> { ((Timer) e2.getSource()).stop(); proximoTurno(); }).start();
                }
            }
        }).start();
    }

    public void processarJogadaHumano(Carta carta) {
        if (jogadorDaVezIndex >= ordemDeJogadores.size()) return;
        Jogador humano = ordemDeJogadores.get(jogadorDaVezIndex);
        if (humano.isBot()) return;

        carta.setEncoberta(tela.isJogadaEncoberta());
        tela.resetarJogadaEncoberta();

        humano.getMao().remove(carta);
        mao.getVazaAtual().adicionarJogada(humano, carta, dupla1);
        jogadorDaVezIndex++;
        tela.atualizar(mao, dupla1, dupla2);
        proximoTurno();
    }

    private void proximoTurno() {
        if (maoTerminou()) { concluirMao(); return; }
        if (mao.getVazaAtual().getJogadas().size() == 4) { tela.atualizar(mao, dupla1, dupla2); concluirVaza(); return; }
        if (mao.isApostaEmAndamento()) return;

        Jogador vez = ordemDeJogadores.get(jogadorDaVezIndex);
        tela.setStatus("Vez de: " + vez.getNome());
        atualizarBotoesParaTurno();

        if (vez.isBot()) {
            tela.habilitarCartas(false);
            new Timer(1500, e -> {
                ((Timer) e.getSource()).stop();
                Dupla duplaBot = dupla1.getJogadores().contains(vez) ? dupla1 : dupla2;
                if (duplaBot != mao.getDuplaUltimaApostaVencedora() && mao.getProximoValorAposta() < 12 && random.nextBoolean()) {
                    iniciarAposta(duplaBot);
                } else {
                    Carta c = vez.jogarCartaAleatoriamente();
                    mao.getVazaAtual().adicionarJogada(vez, c, duplaBot);
                    jogadorDaVezIndex++;
                    tela.atualizar(mao, dupla1, dupla2);
                    proximoTurno();
                }
            }).start();
        } else {
            tela.habilitarCartas(true);
        }
    }

    private void atualizarBotoesParaTurno() {
        Jogador vez = ordemDeJogadores.get(jogadorDaVezIndex);
        boolean turnoHumano = !vez.isBot();
        if (mao.isApostaEmAndamento()) {
            boolean humanoDesafiado = mao.getDuplaDesafiada() == dupla1;
            tela.configurarBotoesDeAposta(humanoDesafiado, humanoDesafiado, mao.getProximoValorAposta());
            tela.habilitarCartas(false);
        } else {
            boolean podeApostar = turnoHumano && (dupla1 != mao.getDuplaUltimaApostaVencedora());
            tela.configurarBotoesDeAposta(podeApostar, false, mao.getProximoValorAposta());
        }
    }

    private void concluirVaza() {
        Vaza.Jogada jv = mao.getVazaAtual().getJogadaVencedora();
        tela.setStatus(jv != null
                ? "Vencedor da vaza: " + jv.getJogador().getNome() + " com " + jv.getCarta()
                : "Vaza empatada!");
        new Timer(2500, e -> {
            ((Timer) e.getSource()).stop();
            mao.proximaVaza();
            ordemDeJogadores = mao.getOrdemJogadores();
            jogadorDaVezIndex = 0;
            tela.atualizar(mao, dupla1, dupla2);
            proximoTurno();
        }).start();
    }

    private boolean maoTerminou() {
        int v1 = 0, v2 = 0, emp = 0;
        Vaza[] vazas = mao.getVazas();
        for (int i = 0; i < 2; i++) {
            if (vazas[i].getJogadas().size() < 4) return false;
            Vaza.Jogada jv = vazas[i].getJogadaVencedora();
            if (jv == null) emp++;
            else if (jv.getDupla() == dupla1) v1++;
            else v2++;
        }
        if (v1 == 2 || v2 == 2) return true;
        if (emp == 1 && (v1 == 1 || v2 == 1)) return true;
        return vazas[2].getJogadas().size() == 4;
    }

    private void concluirMao(boolean verificar) {
        if (verificar) {
            Dupla venc = mao.getVencedorDaMao();
            if (venc != null) {
                venc.adicionaPontos(mao.getValor());
                if (venc.venceu()) {
                    tela.setStatus("!!! FIM DE JOGO !!! Vencedor: " + venc.getNome());
                    tela.configurarBotoesDeControle(true, false);
                } else {
                    tela.setStatus("Fim da mão! Vencedor: " + venc.getNome() + ". Clique em 'Próxima Mão'.");
                    tela.configurarBotoesDeControle(false, true);
                }
            } else {
                tela.setStatus("Fim da mão! Empate. Clique em 'Próxima Mão'.");
                tela.configurarBotoesDeControle(false, true);
            }
        } else {
            tela.configurarBotoesDeControle(false, true);
        }
        mao.setApostaEmAndamento(false);
        tela.atualizar(mao, dupla1, dupla2);
        tela.habilitarCartas(false);
        tela.configurarBotoesDeAposta(false, false, 0);
    }

    private void concluirMao() { concluirMao(true); }
}
