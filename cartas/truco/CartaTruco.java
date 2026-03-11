package cartas.truco;

import cartas.framework.Carta;

/**
 * Carta do Truco Paulista com hierarquia de força completa.
 */
public class CartaTruco extends Carta {

    public CartaTruco(String valor, String naipe) {
        super(valor, naipe);
    }

    @Override
    public int getForca() {
        if (isEncoberta()) return -1;

        // Manilhas fixas
        if (getValor().equals("4") && getNaipe().equals("Paus"))    return 14; // Zape
        if (getValor().equals("7") && getNaipe().equals("Copas"))   return 13;
        if (getValor().equals("A") && getNaipe().equals("Espadas")) return 12; // Espadilha
        if (getValor().equals("7") && getNaipe().equals("Ouros"))   return 11;

        // Hierarquia comum
        switch (getValor()) {
            case "3": return 10;
            case "2": return 9;
            case "A": return 8;
            case "K": return 7;
            case "J": return 6;
            case "Q": return 5;
            case "7": return 4;
            case "6": return 3;
            case "5": return 2;
            case "4": return 1;
            default:  return 0;
        }
    }
}
