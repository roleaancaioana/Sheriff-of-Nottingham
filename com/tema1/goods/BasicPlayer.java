package com.tema1.goods;

import java.util.List;
import java.util.ArrayList;

public class BasicPlayer extends Player {
    /**
     * Prin aceasta metoda am implementat modul in care se comporta jucatorul de baza atunci
     * cand este comerciant.
     * @param assetIds este o lista ce contine id-urile cartilor pe care le extrage un jucator
     */
    public void merchant(final List<Integer> assetIds) {
        int[] frequency = new int[super.getMaxId()];
        int[] mostFrequentAsset = new int[super.getMaxId()];
        int i, counter = 0, maxFrequency = 0, ok = 0;

        for (i = 0; i < super.getMaxId(); i++) {
            frequency[i] = 0;
        }

        for (Integer assetId : assetIds) {
            Goods good = super.getAllGoods().get(assetId);
            if (good.getType() == GoodsType.Legal) {
                // Asta inseamna ca un comerciant va avea bunuri legale in mana
                // si ma voi axa apoi doar pe alegerea celui mai potrivit bun legal.
                ok = 1;
            }
        }

        if (ok == 1) {
            // Pentru a cauta tipul de bunuri legale cel mai frecvent pe care il are un comerciant
            // am construit un vector de frecventa.
            // Vreau sa aflu frecventa de aparitie doar a bunurilor legale.
            for (Integer assetId : assetIds) {
                if (super.getAllGoods().get(assetId).getType() == GoodsType.Legal) {
                    frequency[assetId]++;
                }
            }

            for (i = 0; i < getMaxId(); i++) {
                if (frequency[i] > maxFrequency) {
                    maxFrequency = frequency[i];
                }
            }

            /* Pot exista mai multe carti legale cu aceeasi frecventa de aparitie.
               Prin urmare, retin intr-un alt vector toate id-urile cartilor legale
               care au cea mai mare frecventa de aparitie.
             */
            for (int id = 0; id < getMaxId(); id++) {
                if (frequency[id] == maxFrequency) {
                    mostFrequentAsset[counter++] = id;
                }
            }

            // In goods voi avea toate bunurile legale cu cea mai mare frecventa de aparitie
            ArrayList<Goods> goods = new ArrayList<Goods>();
            for (i = 0; i < counter; i++) {
                goods.add(super.getAllGoods().get(mostFrequentAsset[i]));
            }

            // Sortez bunurile cu cea mai mare frecventa de aparitie
            // conform criteriilor din enunt.
            goods.sort(new MerchantComparator());

            // In bag o sa am bunurile pe care le declara comerciantul serifului.
            // Voi avea in bag mai multe bunuri de acelasi tip
            // pentru ca este un comerciant corect.
            for (i = 0; i < maxFrequency; i++) {
                super.getBag().add(goods.get(0));
            }

            // Fiind un comerciant sincer, el va declara un singur tip de bun.
            // Tipul bunului declarat este in concordanta cu ce are el in sac.
            Goods good = goods.get(0);
            super.setDeclaredGood(good);

        } else {
            // De data aceasta, in goods voi avea doar bunuri ilegale.
            ArrayList<Goods> goods = new ArrayList<Goods>();

            for (Integer assetId : assetIds) {
                goods.add(super.getAllGoods().get(assetId));
            }

            // Pentru a afla care este cartea ilegala cu cel mai mare profit
            // am sortat arraylist-ul goods.
            goods.sort(new ProfitComparator());

            // Baga doar o carte in sac pentru ca de data asta comerciantul are doar bunuri ilegale.
            super.getBag().add(goods.get(0));

            // Declara ca in sac se afla mere.
            super.setDeclaredGood(super.getAllGoods().get(getIdApple()));
        }
    }

    /**
     * Prin aceasta metoda am implementat modul in care un jucator de baza
     * actioneaza atunci cand este serif.
     * @param players este un arraylist ce contine toti jucatorii pe care ii va verifica seriful
     */
    public void sheriff(final ArrayList<Player> players) {
        for (Player player : players) {
            checkBag(player);
        }
    }
}
