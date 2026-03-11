package cartas.truco;

import cartas.framework.Carta;
import cartas.framework.Jogador;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TelaTruco extends JFrame {

    private final ControladorTruco controlador;
    private JPanel painelMesa, painelMaoHumano, painelNorte;
    private JLabel labelStatus, labelPlacar, labelTentos;
    private JButton btnIniciarJogo, btnIniciarMao;
    private JCheckBox cbEncoberto;
    private JButton btnAumentarAposta, btnAceitar, btnCorrer;
    private List<JPanel> indicadoresVazaNos  = new ArrayList<>();
    private List<JPanel> indicadoresVazaEles = new ArrayList<>();

    public TelaTruco(ControladorTruco controlador) {
        super("Truco");
        this.controlador = controlador;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(57, 114, 90));

        painelMesa = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        painelMesa.setBackground(new Color(57, 114, 90));

        labelStatus = new JLabel("Bem-vindo! Clique em 'Novo Jogo' para começar.", SwingConstants.CENTER);
        labelStatus.setFont(new Font("Arial", Font.BOLD, 18));
        labelStatus.setForeground(Color.WHITE);

        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.add(painelMesa, BorderLayout.CENTER);
        painelCentral.add(labelStatus, BorderLayout.SOUTH);
        painelCentral.setOpaque(false);
        add(painelCentral, BorderLayout.CENTER);

        painelMaoHumano = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelMaoHumano.setPreferredSize(new Dimension(0, 150));
        painelMaoHumano.setOpaque(false);
        add(painelMaoHumano, BorderLayout.SOUTH);

        painelNorte = new JPanel(new GridLayout(1, 2));
        painelNorte.setOpaque(false);
        add(painelNorte, BorderLayout.NORTH);

        JPanel painelLeste = new JPanel();
        painelLeste.setLayout(new BoxLayout(painelLeste, BoxLayout.Y_AXIS));
        painelLeste.setPreferredSize(new Dimension(180, 0));
        painelLeste.setBackground(new Color(45, 90, 70));

        JPanel painelPlacar = new JPanel();
        painelPlacar.setBorder(BorderFactory.createTitledBorder("Placar do Jogo"));
        labelPlacar = new JLabel("Nós 0 x 0 Eles");
        painelPlacar.add(labelPlacar);

        JPanel painelTentos = new JPanel();
        painelTentos.setBorder(BorderFactory.createTitledBorder("Tentos"));
        labelTentos = new JLabel("Mão valendo: 1");
        painelTentos.add(labelTentos);

        JPanel painelVazas = new JPanel(new GridLayout(2, 4, 3, 3));
        painelVazas.setBorder(BorderFactory.createTitledBorder("Vazas da Mão"));
        painelVazas.add(new JLabel("Nós:"));
        for (int i = 0; i < 3; i++) {
            JPanel p = new JPanel();
            p.setPreferredSize(new Dimension(20, 20));
            p.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            p.setBackground(Color.LIGHT_GRAY);
            indicadoresVazaNos.add(p); painelVazas.add(p);
        }
        painelVazas.add(new JLabel("Eles:"));
        for (int i = 0; i < 3; i++) {
            JPanel p = new JPanel();
            p.setPreferredSize(new Dimension(20, 20));
            p.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            p.setBackground(Color.LIGHT_GRAY);
            indicadoresVazaEles.add(p); painelVazas.add(p);
        }

        JPanel painelBotoes = new JPanel(new GridLayout(0, 1, 5, 5));
        btnIniciarJogo    = new JButton("Novo Jogo");
        btnIniciarMao     = new JButton("Próxima Mão");
        cbEncoberto       = new JCheckBox("Jogar Encoberto");
        cbEncoberto.setHorizontalAlignment(SwingConstants.CENTER);
        btnAumentarAposta = new JButton("TRUCO");
        btnAceitar        = new JButton("Aceitar");
        btnCorrer         = new JButton("Correr");

        painelBotoes.add(btnIniciarJogo);
        painelBotoes.add(btnIniciarMao);
        painelBotoes.add(cbEncoberto);
        painelBotoes.add(btnAumentarAposta);
        painelBotoes.add(btnAceitar);
        painelBotoes.add(btnCorrer);

        painelLeste.add(painelPlacar);
        painelLeste.add(painelTentos);
        painelLeste.add(painelVazas);
        painelLeste.add(Box.createVerticalGlue());
        painelLeste.add(painelBotoes);
        add(painelLeste, BorderLayout.EAST);

        btnIniciarJogo.addActionListener(e -> controlador.iniciarNovoJogo());
        btnIniciarMao.addActionListener(e -> {
            configurarBotoesDeControle(false, false);
            controlador.iniciarNovaMao();
        });
        btnAumentarAposta.addActionListener(e -> controlador.processarAcaoHumano("AUMENTAR"));
        btnAceitar.addActionListener(e -> controlador.processarAcaoHumano("ACEITAR"));
        btnCorrer.addActionListener(e -> controlador.processarAcaoHumano("CORRER"));

        configurarBotoesDeControle(true, false);
        configurarBotoesDeAposta(false, false, 0);
        cbEncoberto.setEnabled(false);

        setVisible(true);
    }

    public void configurarBotoesDeAposta(boolean podeApostar, boolean podeResponder, int valorDesafio) {
        btnAumentarAposta.setVisible(podeApostar);
        btnAumentarAposta.setEnabled(podeApostar);
        if (podeApostar) {
            btnAumentarAposta.setText(podeResponder ? switch (valorDesafio) {
                case 3 -> "VALE 6"; case 6 -> "VALE 9"; case 9 -> "VALE 12"; default -> "AUMENTAR";
            } : "TRUCO");
        }
        btnAceitar.setVisible(podeResponder);
        btnAceitar.setEnabled(podeResponder);
        btnCorrer.setVisible(podeResponder);
        btnCorrer.setEnabled(podeResponder);
    }

    public void atualizar(Mao mao, Dupla d1, Dupla d2) {
        painelMaoHumano.removeAll();
        painelNorte.removeAll();
        painelMesa.removeAll();

        labelPlacar.setText("Nós " + d1.getPontosNoJogo() + " x " + d2.getPontosNoJogo() + " Eles");

        if (mao != null) {
            labelTentos.setText("Mão valendo: " + mao.getValor());

            for (Jogador j : mao.getOrdemJogadores()) {
                if (!j.isBot()) {
                    for (Carta c : j.getMao()) {
                        BotaoCartaTruco bc = new BotaoCartaTruco(c);
                        bc.addActionListener(e -> controlador.processarJogadaHumano(((BotaoCartaTruco) e.getSource()).getCarta()));
                        painelMaoHumano.add(bc);
                    }
                } else {
                    JPanel painelBot = new JPanel();
                    painelBot.setOpaque(false);
                    TitledBorder border = BorderFactory.createTitledBorder(j.getNome());
                    border.setTitleColor(Color.WHITE);
                    painelBot.setBorder(border);
                    for (int i = 0; i < j.getMao().size(); i++) {
                        Carta verso = new Carta("", "");
                        verso.setEncoberta(true);
                        painelBot.add(new BotaoCartaTruco(verso));
                    }
                    painelNorte.add(painelBot);
                }
            }

            for (Vaza.Jogada jogada : mao.getVazaAtual().getJogadas()) {
                JPanel pj = new JPanel(new BorderLayout());
                TitledBorder border = BorderFactory.createTitledBorder(null, jogada.getJogador().getNome(),
                        TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 12), Color.WHITE);
                pj.setBorder(border);
                pj.setOpaque(false);
                pj.add(new BotaoCartaTruco(jogada.getCarta()), BorderLayout.CENTER);
                painelMesa.add(pj);
            }

            for (int i = 0; i < 3; i++) {
                indicadoresVazaNos.get(i).setBackground(Color.LIGHT_GRAY);
                indicadoresVazaEles.get(i).setBackground(Color.LIGHT_GRAY);
            }
            Vaza[] vazas = mao.getVazas();
            for (int i = 0; i < vazas.length; i++) {
                if (vazas[i].getJogadas().isEmpty()) break;
                Vaza.Jogada jv = vazas[i].getJogadaVencedora();
                if (jv == null) {
                    indicadoresVazaNos.get(i).setBackground(Color.ORANGE);
                    indicadoresVazaEles.get(i).setBackground(Color.ORANGE);
                } else if (jv.getDupla() == d1) {
                    indicadoresVazaNos.get(i).setBackground(Color.CYAN);
                } else {
                    indicadoresVazaEles.get(i).setBackground(Color.RED);
                }
            }
        }

        revalidate();
        repaint();
    }

    public void setStatus(String texto)    { labelStatus.setText(texto); }
    public void habilitarCartas(boolean h) { for (Component c : painelMaoHumano.getComponents()) c.setEnabled(h); cbEncoberto.setEnabled(h); }
    public boolean isJogadaEncoberta()     { return cbEncoberto.isSelected(); }
    public void resetarJogadaEncoberta()   { cbEncoberto.setSelected(false); }
    public void configurarBotoesDeControle(boolean novoJogo, boolean proxMao) {
        btnIniciarJogo.setEnabled(novoJogo);
        btnIniciarMao.setEnabled(proxMao);
    }
}
