package cartas.truco;

import cartas.framework.BotaoCarta;
import cartas.framework.Carta;

public class BotaoCartaTruco extends BotaoCarta {

    public BotaoCartaTruco(Carta carta) {
        super(carta);
    }

    @Override
    protected String caminhoImagem() {
        Carta c = getCarta();
        if (c.isEncoberta() || c.getValor().isEmpty()) {
            return "/resources/truco/verso.png";
        }
        String valor = c.getValor().toLowerCase();
        String naipe = c.getNaipe().toLowerCase();
        return "/resources/truco/" + valor + "_" + naipe + ".png";
    }
}
