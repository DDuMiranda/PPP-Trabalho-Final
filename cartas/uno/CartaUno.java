package cartas.uno;

import cartas.framework.Carta;

/**
 * Carta do Uno. Estende Carta do framework adicionando tipo e cor.
 * O "naipe" do framework é reutilizado como "cor" no Uno.
 */
public class CartaUno extends Carta {

    public enum Tipo { NUMERADA, MAIS_DOIS, BLOQUEIO, INVERTER, CURINGA, MAIS_QUATRO }

    public static final String VERMELHO = "Vermelho";
    public static final String AZUL     = "Azul";
    public static final String VERDE    = "Verde";
    public static final String AMARELO  = "Amarelo";
    public static final String[] CORES  = {VERMELHO, AZUL, VERDE, AMARELO};

    private final Tipo tipo;

    public CartaUno(String valor, String cor, Tipo tipo) {
        super(valor, cor); // naipe = cor no Uno
        this.tipo = tipo;
    }

    public Tipo   getTipo()    { return tipo; }
    public String getCor()     { return getNaipe(); }

    public boolean isCoringa() {
        return tipo == Tipo.CURINGA || tipo == Tipo.MAIS_QUATRO;
    }

    public boolean isAcao() {
        return tipo != Tipo.NUMERADA;
    }

    @Override
    public String toString() {
        if (isCoringa()) return getValor();
        return getCor() + " " + getValor();
    }
}
