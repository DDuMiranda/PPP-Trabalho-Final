package cartas.framework;

/**
 * Strategy: define a regra de validação de uma jogada.
 *
 * Cada jogo concreto implementa sua própria lógica de quais cartas
 * podem ser jogadas em um determinado momento.
 */
public interface RegraJogada {
    /**
     * @param carta  a carta que o jogador deseja jogar
     * @param estado o estado atual do jogo (Object para ser genérico)
     * @return true se a jogada é permitida pelas regras do jogo
     */
    boolean podeJogar(Carta carta, Object estado);
}
