package cartas.truco;

import cartas.framework.Baralho;

/**
 * Baralho de 40 cartas do Truco Paulista.
 * Implementa o passo criarCartas() do Template Method definido em Baralho.
 */
public class BaralhoTruco extends Baralho {

    @Override
    protected void criarCartas() {
        String[] naipes = {"Paus", "Copas", "Espadas", "Ouros"};
        String[] valores = {"4", "5", "6", "7", "Q", "J", "K", "A", "2", "3"};

        for (String valor : valores)
            for (String naipe : naipes)
                cartas.add(new CartaTruco(valor, naipe));
    }
}
