package cartas.framework;

/**
 * Representa uma carta genérica de qualquer jogo de cartas.
 * A força da carta é definida por subclasses (polimorfismo).
 */
public class Carta {
    private final String valor;
    private final String naipe;
    private boolean encoberta = false;

    public Carta(String valor, String naipe) {
        this.valor = valor;
        this.naipe = naipe;
    }

    public String getValor()  { return valor; }
    public String getNaipe()  { return naipe; }
    public boolean isEncoberta() { return encoberta; }
    public void setEncoberta(boolean encoberta) { this.encoberta = encoberta; }


    public int getForca() { return 0; }

    @Override
    public String toString() {
        return valor + " de " + naipe;
    }
}
