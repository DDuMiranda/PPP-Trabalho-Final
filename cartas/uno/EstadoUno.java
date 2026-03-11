package cartas.uno;

import cartas.framework.Jogador;
import java.util.ArrayList;
import java.util.List;

public class EstadoUno {
    private final List<Jogador> jogadores;
    private int jogadorAtualIndex = 0;
    private boolean sentidoHorario = true;

    private CartaUno topoDescarte;
    private String   corAtual;

    private int     cartasParaComprar = 0;
    private boolean pulaPróximo       = false;

    public EstadoUno(List<Jogador> jogadores) {
        this.jogadores = new ArrayList<>(jogadores);
    }

    public Jogador getJogadorAtual() {
        return jogadores.get(jogadorAtualIndex);
    }

    // Avanço simples — pulaPróximo é consumido em aplicarComprasPendentes
    public void avancarJogador() {
        jogadorAtualIndex = proximoIndice(jogadorAtualIndex);
    }

    public void inverterSentido() { sentidoHorario = !sentidoHorario; }

    private int proximoIndice(int atual) {
        return sentidoHorario
                ? (atual + 1) % jogadores.size()
                : (atual - 1 + jogadores.size()) % jogadores.size();
    }

    public List<Jogador> getJogadores()           { return jogadores; }
    public boolean isSentidoHorario()             { return sentidoHorario; }
    public CartaUno getTopoDescarte()             { return topoDescarte; }
    public void setTopoDescarte(CartaUno c)       { this.topoDescarte = c; }
    public String getCorAtual()                   { return corAtual; }
    public void setCorAtual(String cor)           { this.corAtual = cor; }
    public int getCartasParaComprar()             { return cartasParaComprar; }
    public void setCartasParaComprar(int n)       { this.cartasParaComprar = n; }
    public void adicionarCartasParaComprar(int n) { this.cartasParaComprar += n; }
    public boolean isPulaPróximo()                { return pulaPróximo; }
    public void setPulaPróximo(boolean p)         { this.pulaPróximo = p; }
}
