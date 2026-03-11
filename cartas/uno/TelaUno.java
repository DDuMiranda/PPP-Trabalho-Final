package cartas.uno;

import cartas.framework.Carta;
import cartas.framework.Jogador;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class TelaUno extends JFrame {

    private final ControladorUno controlador;

    private JPanel painelMesa, painelMaoHumano, painelBots;
    private JLabel labelStatus, labelSentido;
    private JButton btnNovoJogo, btnComprar;
    private JPanel painelSeletorCor;

    public TelaUno(ControladorUno controlador) {
        super("Uno");
        this.controlador = controlador;
        construirUI();
        setVisible(true);
    }

    private void construirUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));
        getContentPane().setBackground(new Color(21, 81, 56));

        painelBots = new JPanel(new GridLayout(1, 3, 5, 0));
        painelBots.setOpaque(false);
        painelBots.setPreferredSize(new Dimension(0, 170));
        add(painelBots, BorderLayout.NORTH);

        painelMesa = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        painelMesa.setBackground(new Color(57, 114, 90));
        painelMesa.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 60), 2));

        labelStatus = new JLabel("Clique em 'Novo Jogo' para começar.", SwingConstants.CENTER);
        labelStatus.setFont(new Font("Arial", Font.BOLD, 16));
        labelStatus.setForeground(Color.WHITE);

        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.add(painelMesa, BorderLayout.CENTER);
        painelCentral.add(labelStatus, BorderLayout.SOUTH);
        painelCentral.setOpaque(false);
        add(painelCentral, BorderLayout.CENTER);

        painelMaoHumano = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        painelMaoHumano.setPreferredSize(new Dimension(0, 155));
        painelMaoHumano.setOpaque(false);
        add(painelMaoHumano, BorderLayout.SOUTH);

        // Painel lateral
        JPanel painelLeste = new JPanel();
        painelLeste.setLayout(new BoxLayout(painelLeste, BoxLayout.Y_AXIS));
        painelLeste.setPreferredSize(new Dimension(180, 0));
        painelLeste.setBackground(new Color(20, 20, 60));
        painelLeste.setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 8));

        labelSentido = new JLabel("Sentido Horário", SwingConstants.CENTER);
        labelSentido.setFont(new Font("Arial", Font.BOLD, 14));
        labelSentido.setForeground(new Color(243, 169, 28));
        labelSentido.setAlignmentX(CENTER_ALIGNMENT);

        btnNovoJogo = new JButton("Novo Jogo");
        btnNovoJogo.setAlignmentX(CENTER_ALIGNMENT);
        btnNovoJogo.setMaximumSize(new Dimension(160, 35));

        btnComprar = new JButton("Comprar Carta");
        btnComprar.setAlignmentX(CENTER_ALIGNMENT);
        btnComprar.setMaximumSize(new Dimension(160, 35));
        btnComprar.setEnabled(false);
        btnComprar.setBackground(new Color(243, 169, 28));
        btnComprar.setForeground(Color.black);

        painelSeletorCor = new JPanel(new GridLayout(2, 2, 4, 4));
        painelSeletorCor.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE), "Escolha a cor:",
                TitledBorder.CENTER, TitledBorder.TOP, null, Color.WHITE));
        painelSeletorCor.setOpaque(false);
        painelSeletorCor.setMaximumSize(new Dimension(160, 100));
        painelSeletorCor.setAlignmentX(CENTER_ALIGNMENT);

        String[] cores = {CartaUno.VERMELHO, CartaUno.AZUL, CartaUno.VERDE, CartaUno.AMARELO};
        Color[] bgCores = {new Color(200, 50, 50), new Color(50, 100, 200),
                           new Color(50, 160, 50), new Color(220, 180, 0)};
        for (int i = 0; i < 4; i++) {
            final String cor = cores[i];
            JButton btn = new JButton(cor);
            btn.setBackground(bgCores[i]);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 11));
            btn.addActionListener(e -> controlador.processarEscolhaCor(cor));
            painelSeletorCor.add(btn);
        }
        painelSeletorCor.setVisible(false);

        painelLeste.add(Box.createVerticalStrut(10));
        painelLeste.add(labelSentido);
        painelLeste.add(Box.createVerticalStrut(20));
        painelLeste.add(btnNovoJogo);
        painelLeste.add(Box.createVerticalStrut(10));
        painelLeste.add(btnComprar);
        painelLeste.add(Box.createVerticalStrut(20));
        painelLeste.add(painelSeletorCor);
        painelLeste.add(Box.createVerticalGlue());
        add(painelLeste, BorderLayout.EAST);

        btnNovoJogo.addActionListener(e -> controlador.iniciarNovoJogo());
        btnComprar.addActionListener(e -> controlador.processarCompra());
    }

    public void atualizar() {
        JogoUno jogo = controlador.getJogo();
        if (jogo.getEstado() == null) return;

        EstadoUno estado = jogo.getEstado();
        List<Jogador> jogadores = estado.getJogadores();

        painelMaoHumano.removeAll();
        painelBots.removeAll();
        painelMesa.removeAll();

        // Monte
        JPanel painelMonte = new JPanel(new BorderLayout());
        painelMonte.setOpaque(false);
        painelMonte.setBorder(BorderFactory.createTitledBorder(null, "Monte",
                TitledBorder.CENTER, TitledBorder.TOP, null, Color.WHITE));
        CartaUno verso = new CartaUno("", "", CartaUno.Tipo.NUMERADA);
        verso.setEncoberta(true);
        painelMonte.add(new BotaoCartaUno(verso), BorderLayout.CENTER);
        painelMesa.add(painelMonte);

        // Topo do descarte
        CartaUno topo = estado.getTopoDescarte();
        if (topo != null) {
            String labelTopo = "Descarte";
            if (topo.isCoringa() && estado.getCorAtual() != null && !estado.getCorAtual().isEmpty()) {
                labelTopo += " — Cor: " + switch (estado.getCorAtual()) {
                    case CartaUno.VERMELHO -> "🔴 Vermelho";
                    case CartaUno.AZUL     -> "🔵 Azul";
                    case CartaUno.VERDE    -> "🟢 Verde";
                    case CartaUno.AMARELO  -> "🟡 Amarelo";
                    default -> estado.getCorAtual();
                };
            }
            JPanel painelTopo = new JPanel(new BorderLayout());
            painelTopo.setOpaque(false);
            painelTopo.setBorder(BorderFactory.createTitledBorder(null, labelTopo,
                    TitledBorder.CENTER, TitledBorder.TOP, null, Color.YELLOW));
            painelTopo.add(new BotaoCartaUno(topo), BorderLayout.CENTER);
            painelMesa.add(painelTopo);
        }

        // Mão do jogador atual (sul)
        Jogador atual = estado.getJogadorAtual();
        for (Carta c : atual.getMao()) {
            if (!(c instanceof CartaUno cu)) continue;
            BotaoCartaUno bc = new BotaoCartaUno(cu);
            if (jogo.getRegraJogada().podeJogar(cu, estado))
                bc.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
            bc.addActionListener(e ->
                controlador.processarJogadaHumano((CartaUno) ((BotaoCartaUno) e.getSource()).getCarta()));
            painelMaoHumano.add(bc);
        }

        // Outros jogadores (norte) — cartas empilhadas com sobreposição
        for (Jogador j : jogadores) {
            if (j == atual) continue;

            int qtd = j.getMao().size();
            int overlap = 28;
            int maxW = 300;
            int panelW = qtd > 0 ? Math.min(BotaoCartaUno.LARGURA + (qtd - 1) * overlap, maxW)
                                  : BotaoCartaUno.LARGURA;

            JPanel painelCartas = new JPanel(null);
            painelCartas.setOpaque(false);
            painelCartas.setPreferredSize(new Dimension(panelW, BotaoCartaUno.ALTURA));

            int exibir = qtd > 0 ? Math.min(qtd, (maxW - BotaoCartaUno.LARGURA) / overlap + 1) : 0;
            for (int i = 0; i < exibir; i++) {
                CartaUno v = new CartaUno("", "", CartaUno.Tipo.NUMERADA);
                v.setEncoberta(true);
                BotaoCartaUno bc = new BotaoCartaUno(v);
                bc.setBounds(i * overlap, 0, BotaoCartaUno.LARGURA, BotaoCartaUno.ALTURA);
                painelCartas.add(bc);
            }

            JPanel centralizador = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            centralizador.setOpaque(false);
            centralizador.add(painelCartas);

            JPanel painelJ = new JPanel(new BorderLayout(0, 2));
            painelJ.setOpaque(false);
            painelJ.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.WHITE),
                    j.getNome() + " (" + qtd + ")",
                    TitledBorder.CENTER, TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 11), Color.WHITE));
            painelJ.add(centralizador, BorderLayout.CENTER);
            painelBots.add(painelJ);
        }

        revalidate();
        repaint();
    }

    public void setStatus(String texto)      { labelStatus.setText(texto); }
    public void habilitarCartas(boolean h)   { for (Component c : painelMaoHumano.getComponents()) c.setEnabled(h); }
    public void habilitarCompra(boolean h)   { btnComprar.setEnabled(h); }
    public void mostrarSeletorCor(boolean v) { painelSeletorCor.setVisible(v); revalidate(); repaint(); }
    public void atualizarIndicadorSentido(boolean horario) {
        labelSentido.setText(horario ? "Sentido Horário" : "Sentido Anti-horário");
    }
}
