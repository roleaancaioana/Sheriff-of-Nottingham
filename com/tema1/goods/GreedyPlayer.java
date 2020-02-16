package com.tema1.goods;

import java.util.ArrayList;
import java.util.List;

public class GreedyPlayer extends BasicPlayer {
    @Override
    public final void sheriff(final ArrayList<Player> players) {
        for (Player player : players) {
            if (player.getId() != this.getId()) {
                if (player.hasBribe()) {
                    // Ii dau serifului mita
                    super.addBribe(player.getBribe());

                    // Ii scad comerciantului nr de monede pentru ca a dat mita
                    player.setCoins(player.getCoins() - player.getBribe());

                    // Deoarece player-ul a platit mita,
                    // seriful il lasa sa duca toate bunurile din sac in Nottingham
                    for (Goods good : player.getBag()) {
                        player.getAllowedGoods().add(good);
                    }
                } else {
                    super.checkBag(player);
                }
            }
        }
    }

    @Override
    public final void merchant(final List<Integer> assetIds) {
        super.merchant(assetIds);
        boolean hasIllegalGoods = false;

        if (this.getNumberOfRound() % 2 == 0 && super.getMaxSizeBag() > super.getBag().size()) {
            ArrayList<Goods> goods = new ArrayList<Goods>();
            for (Integer assetId : assetIds) {
                if (super.getAllGoods().get(assetId).getType() == GoodsType.Illegal) {
                    goods.add(super.getAllGoods().get(assetId));
                    hasIllegalGoods = true;
                }
            }

            if (hasIllegalGoods) {
                goods.sort(new ProfitComparator());
                if (super.getBag().size() == 1
                        && super.getBag().get(0).getType() == GoodsType.Illegal) {
                    super.getBag().add(goods.get(1));
                } else {
                    // Baga in sac cartea ilegala cu profitul cel mai mare
                    super.getBag().add(goods.get(0));
                }
            }
        }
    }
}
