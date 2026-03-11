package cartas.framework;

/**
 * Observer: interface para que a View seja notificada sobre eventos do jogo.
 */

public interface ObservadorJogo {
    /** Chamado sempre que o estado do jogo mudar  */
    void onEstadoAtualizado(String evento);

    /** Chamado quando o jogo termina, passando o nome do vencedor. */
    void onJogoTerminado(String vencedor);
}
