package cartas.uno;

import cartas.framework.Carta;
import cartas.framework.RegraJogada;

public class RegraJogadaUno implements RegraJogada {

    @Override
    public boolean podeJogar(Carta carta, Object estado) {
        if (!(carta instanceof CartaUno c))    return false;
        if (!(estado instanceof EstadoUno eu)) return false;

        CartaUno topo = eu.getTopoDescarte();
        if (topo == null) return true;

        // Se há compras pendentes: só pode empilhar +2 ou +4, coringa não vale
        if (eu.getCartasParaComprar() > 0) {
            if (topo.getTipo() == CartaUno.Tipo.MAIS_DOIS)
                return c.getTipo() == CartaUno.Tipo.MAIS_DOIS
                        || c.getTipo() == CartaUno.Tipo.MAIS_QUATRO;
            if (topo.getTipo() == CartaUno.Tipo.MAIS_QUATRO)
                return c.getTipo() == CartaUno.Tipo.MAIS_QUATRO;
        }

        if (c.isCoringa()) return true;

        return c.getCor().equals(eu.getCorAtual())
                || c.getValor().equals(topo.getValor());
    }
}
