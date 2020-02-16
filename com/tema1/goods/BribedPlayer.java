package com.tema1.goods;

import java.util.ArrayList;
import java.util.List;

public class BribedPlayer extends BasicPlayer {
    private static final int MAX_BRIBE = 10;
    private static final int MIN_BRIBE = 5;
    private static final int MIN_PLAYERS = 2;
    @Override
    public final void merchant(final List<Integer> assetIds) {
        int i;
        ArrayList<Goods> allowedGoodsWithoutPenalty = new ArrayList<Goods>();
        ArrayList<Goods> illegalGoodsInHand = new ArrayList<Goods>();
        ArrayList<Goods> legalGoodsInHand = new ArrayList<Goods>();

        for (Integer asset : assetIds) {
            if (super.getAllGoods().get(asset).getType() == GoodsType.Illegal) {
                illegalGoodsInHand.add(super.getAllGoods().get(asset));
            }
        }

        if (illegalGoodsInHand.size() == 0 || super.getCoins() <= super.getMinCoins()) {
            super.setBribe(0);
            super.merchant(assetIds);
        } else {
            int numberOfIllegalGoodsInBag;
            int numberOfIllegalGoodsForMinBribe = 2;

            illegalGoodsInHand.sort(new ProfitComparator());

            if (illegalGoodsInHand.size() >= super.getMaxSizeBag()) {
                for (i = 0; i < super.getMaxSizeBag(); ++i) {
                    allowedGoodsWithoutPenalty.add(illegalGoodsInHand.get(i));
                }

               numberOfIllegalGoodsInBag = verifyPenalty(allowedGoodsWithoutPenalty);
            } else {
                int counter = 0;

                // Jucatorul va adauga mai intai toate ilegalele pe care le are in mana.
                for (i = 0; i < illegalGoodsInHand.size(); ++i) {
                    allowedGoodsWithoutPenalty.add(illegalGoodsInHand.get(i));
                }

                for (Integer asset : assetIds) {
                    if (super.getAllGoods().get(asset).getType() == GoodsType.Legal) {
                        legalGoodsInHand.add(super.getAllGoods().get(asset));
                    }
                }

                legalGoodsInHand.sort(new MerchantComparator());

                // Voi completa apoi sacul cu bunurile legale care au cel mai mare profit.
                for (i = illegalGoodsInHand.size(); i < getMaxSizeBag(); i++) {
                    allowedGoodsWithoutPenalty.add(legalGoodsInHand.get(counter++));
                }

                numberOfIllegalGoodsInBag = verifyPenalty(allowedGoodsWithoutPenalty);
            }

            if (numberOfIllegalGoodsInBag > numberOfIllegalGoodsForMinBribe) {
                super.setBribe(MAX_BRIBE);
            } else {
                super.setBribe(MIN_BRIBE);
            }
            super.setDeclaredGood(super.getAllGoods().get(getIdApple()));
        }
    }

    @Override
    public final void sheriff(final ArrayList<Player> players) {
        Player leftPlayer;
        Player rightPlayer;
        int positionSheriff = this.getId();

        if (positionSheriff == 0) {
            leftPlayer = players.get(players.size() - 1);
        } else {
            leftPlayer = players.get(positionSheriff - 1);
        }

        super.checkBag(leftPlayer);

        if (players.size() > MIN_PLAYERS) {
            rightPlayer = players.get((positionSheriff + 1) % players.size());
            super.checkBag(rightPlayer);

            for (Player player : players) {
                if (player.getId() != this.getId()
                        && player != leftPlayer && player != rightPlayer) {
                    if (player.hasBribe()) {
                        // Ii adaug mita serifului
                        this.setCoins(this.getCoins() + player.getBribe());
                        // Ii scad mita comerciantului
                        player.setCoins(player.getCoins() - player.getBribe());
                        // Ii lasa pe comercianti sa-si puna toate bunurile pe taraba.
                        for (Goods good : player.getBag()) {
                            player.getAllowedGoods().add(good);
                        }
                    } else {
                        // Ii lasa pe comercianti sa-si puna toate bunurile pe taraba
                        // chiar daca nu ofera mita.
                        for (Goods good : player.getBag()) {
                            player.getAllowedGoods().add(good);
                        }
                    }
                }
            }
        }
    }

    public final int verifyPenalty(final ArrayList<Goods> allowedGoodsWithoutPenalty) {
        int sumOfCoins = this.getCoins();
        int numberOfIllegalGoodsInBag = 0;

        // Am grija ca penalty-urile acumulate sa nu aduca
        // suma de bani a comerciantului pe 0.
        for (Goods good : allowedGoodsWithoutPenalty) {
            if (sumOfCoins - good.getPenalty() > 0) {
                this.getBag().add(good);
                sumOfCoins -= good.getPenalty();
                if (good.getType() == GoodsType.Illegal) {
                    numberOfIllegalGoodsInBag++;
                }
            }
        }
        return numberOfIllegalGoodsInBag;
    }
}
