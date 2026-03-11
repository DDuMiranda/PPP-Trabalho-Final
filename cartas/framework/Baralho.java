package cartas.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Baralho genérico e reutilizável.
 *
 * Padrão Template Method: criarCartas() é o passo variável que cada
 * jogo concreto implementa para definir seu conjunto de cartas.
 */
public abstract class Baralho {
    protected final List<Carta> cartas = new ArrayList<>();

    public Baralho() {
        criarCartas(); // Template Method
    }

    /**
     * Passo variável do Template Method.
     * Subclasses criam e adicionam as cartas específicas do jogo.
     */
    protected abstract void criarCartas();

    public void embaralhar() {
        Collections.shuffle(cartas);
    }

    public Carta distribuir() {
        return cartas.isEmpty() ? null : cartas.remove(0);
    }

    public boolean isEmpty()  { return cartas.isEmpty(); }
    public int getTamanho()   { return cartas.size(); }
    public List<Carta> getCartas() { return cartas; }
}
