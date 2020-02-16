package com.tema1.goods;

import java.util.Comparator;

public class MerchantComparator implements Comparator<Goods> {
    /**
     * Aceasta metoda ma va ajuta la sortarea bunurilor cu cea mai mare
     * frecventa de aparitie.
     * @param good1 este un bun care poate ajunge in Nottingham
     * @param good2 este un alt bun care poate ajunge in Nottingham
     * @return diferenta de profit sau de id a bunurilor
     */
    @Override
    public int compare(final Goods good1, final Goods good2) {
        if (good2.getProfit() != good1.getProfit()) {
            return  good2.getProfit() - good1.getProfit();
        } else {
            return good2.getId() - good1.getId();
        }
    }
}

