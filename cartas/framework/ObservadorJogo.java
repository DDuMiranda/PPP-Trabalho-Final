package cartas.framework;

/**
 * Observer: interface para que a View seja notificada sobre eventos do jogo.
 *
 * Desacopla completamente a lógica (Model/Controller) da interface gráfica (View).
 */
public interface ObservadorJogo {
    /** Chamado sempre que o estado do jogo mudar (nova jogada, novo turno, etc.). */
    void onEstadoAtualizado(String evento);

    /** Chamado quando o jogo termina, passando o nome do vencedor. */
    void onJogoTerminado(String vencedor);
}
