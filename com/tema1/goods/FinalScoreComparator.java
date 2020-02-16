package com.tema1.goods;

import java.util.Comparator;

public class FinalScoreComparator implements Comparator<Player> {
    /**
     * Aceasta metoda ma va ajuta la afisarea corecta a clasamentului jocului.
     * @param player2 este un jucator din Nottingham
     * @param player1 este un alt jucator din Nottingham
     * @return diferenta de scor sau de id in ideea de a ma ajuta la sortarea jucatorilor
     * pentru afisarea clasamentului final
     */
    @Override
    public int compare(final Player player2, final Player player1) {
        if (player1.getFinalScore() != player2.getFinalScore()) {
            return player1.getFinalScore() - player2.getFinalScore();
        } else {
            return player2.getId() - player1.getId();
        }
    }
}
