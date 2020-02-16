package com.tema1.goods;

import java.util.Comparator;

public class ProfitComparator implements Comparator<Goods> {
    /**
     * Metoda ma va ajuta la alegerea bunurilor cele mai valoroase.
     * @param good1 este un bun care poate ajunge in Nottingham
     * @param good2 este un alt bun care poate ajunge in Nottingham
     * @return diferenta de profit dintre cele 2 bunuri
     */
    @Override
    public int compare(final Goods good1, final Goods good2) {
        return good2.getProfit() - good1.getProfit();
    }
}
