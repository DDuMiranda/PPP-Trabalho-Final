package cartas.framework;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Botão visual reutilizável que representa uma carta na interface Swing.
 *
 * O carregamento de imagem é centralizado aqui no framework. Subclasses
 * apenas informam o caminho da imagem via caminhoImagem(), mantendo o
 * sistema de imagens extensível para qualquer jogo.
 */
public abstract class BotaoCarta extends JButton {
    public static final int LARGURA = 94;
    public static final int ALTURA  = 125;

    private final Carta carta;

    public BotaoCarta(Carta carta) {
        this.carta = carta;
        setPreferredSize(new Dimension(LARGURA, ALTURA));
        setBorder(null);
        setContentAreaFilled(false);
        setFocusPainted(false);
        carregarImagem();
    }

    protected abstract String caminhoImagem();

    private void carregarImagem() {
        String caminho = caminhoImagem();
        URL url = getClass().getResource(caminho);
        if (url != null) {
            Image img = new ImageIcon(url).getImage()
                    .getScaledInstance(LARGURA, ALTURA, Image.SCALE_SMOOTH);
            setIcon(new ImageIcon(img));
        } else {
            System.err.println("[BotaoCarta] Imagem não encontrada: " + caminho);
            setText(carta.getValor().isEmpty() ? "?" : carta.toString());
            setFont(new Font("Arial", Font.BOLD, 10));
        }
    }

    public Carta getCarta() { return carta; }
}
