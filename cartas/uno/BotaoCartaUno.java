package cartas.uno;

import cartas.framework.BotaoCarta;
import cartas.framework.Carta;

public class BotaoCartaUno extends BotaoCarta {

    public BotaoCartaUno(Carta carta) {
        super(carta);
    }

    @Override
    protected String caminhoImagem() {
        if (!(getCarta() instanceof CartaUno c)) {
            return "/resources/uno/verso_uno.png";
        }
        if (c.isEncoberta() || c.getValor().isEmpty()) {
            return "/resources/uno/verso_uno.png";
        }

        String cor = switch (c.getCor()) {
            case CartaUno.VERMELHO -> "Red";
            case CartaUno.AZUL     -> "Blue";
            case CartaUno.VERDE    -> "Green";
            case CartaUno.AMARELO  -> "Yellow";
            default -> "";
        };

        return switch (c.getTipo()) {
            case NUMERADA    -> "/resources/uno/" + cor + "_" + c.getValor() + ".png";
            case MAIS_DOIS   -> "/resources/uno/" + cor + "_Draw_2.png";
            case BLOQUEIO    -> "/resources/uno/" + cor + "_Skip.png";
            case INVERTER    -> "/resources/uno/" + cor + "_Reverse.png";
            case CURINGA     -> "/resources/uno/Wild_Card_Change_Colour.png";
            case MAIS_QUATRO -> "/resources/uno/Wild_Card_Draw_4.png";
        };
    }
}
