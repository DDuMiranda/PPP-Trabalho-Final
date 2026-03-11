package cartas.uno;

import cartas.framework.Baralho;

/**
 * Baralho oficial do Uno com 108 cartas:
 *  - 4 cores × (1 zero + 2×[1..9] + 2×+2 + 2×Bloqueio + 2×Inverter) = 100 cartas coloridas
 *  - 4 Curingas + 4 +4 = 8 cartas pretas
 * Total = 108 cartas
 */
public class BaralhoUno extends Baralho {

    @Override
    protected void criarCartas() {
        for (String cor : CartaUno.CORES) {
            // Um "0" por cor
            cartas.add(new CartaUno("0", cor, CartaUno.Tipo.NUMERADA));

            // Dois de cada número 1-9 por cor
            for (int n = 1; n <= 9; n++) {
                cartas.add(new CartaUno(String.valueOf(n), cor, CartaUno.Tipo.NUMERADA));
                cartas.add(new CartaUno(String.valueOf(n), cor, CartaUno.Tipo.NUMERADA));
            }

            // Duas de cada carta de ação por cor
            cartas.add(new CartaUno("+2",       cor, CartaUno.Tipo.MAIS_DOIS));
            cartas.add(new CartaUno("+2",       cor, CartaUno.Tipo.MAIS_DOIS));
            cartas.add(new CartaUno("Bloqueio", cor, CartaUno.Tipo.BLOQUEIO));
            cartas.add(new CartaUno("Bloqueio", cor, CartaUno.Tipo.BLOQUEIO));
            cartas.add(new CartaUno("Inverter", cor, CartaUno.Tipo.INVERTER));
            cartas.add(new CartaUno("Inverter", cor, CartaUno.Tipo.INVERTER));
        }

        // Quatro de cada coringa (sem cor)
        for (int i = 0; i < 4; i++) {
            cartas.add(new CartaUno("Curinga", "", CartaUno.Tipo.CURINGA));
            cartas.add(new CartaUno("+4",      "", CartaUno.Tipo.MAIS_QUATRO));
        }
    }
}
