package cartas.truco;

import cartas.framework.Carta;
import cartas.framework.RegraJogada;

/**
 * Strategy de jogada para o Truco.
 * No Truco o jogador pode jogar qualquer carta da mão — sem restrição de cor ou valor.
 */
public class RegraJogadaTruco implements RegraJogada {

    @Override
    public boolean podeJogar(Carta carta, Object estado) {
        return true; // Truco não restringe qual carta pode ser jogada
    }
}
