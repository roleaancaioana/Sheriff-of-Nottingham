package com.tema1.goods;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public abstract class Player {
    private static final int COINS = 80;
    private static final int MAX_SHERIFF = 5;
    private static final int MAX_SIZE_BAG = 8;
    private static final int MIN_COINS = 5;
    private static final int ID_APPLE = 0;
    private static final int MAX_ID = 25;
    private static final int MIN_SUM_FOR_SHERIFF = 16;
    private Goods declaredGood;
    private int bribe, coins, finalScore;
    private int profitLegal, profitIllegal;
    private int bonus, id, numberOfRound;
    private String name;
    private boolean liar;
    private ArrayList<Goods> bag, allowedGoods;
    private GoodsFactory goodsFactory = GoodsFactory.getInstance();
    private Map<Integer, Goods>  allGoods = goodsFactory.getAllGoods();

    Player() {
        allowedGoods = new ArrayList<Goods>();
        bag = new ArrayList<Goods>();
        this.bribe = 0;
        this.coins = COINS;
        this.profitIllegal = 0;
        this.profitLegal = 0;
        this.bonus = 0;
        this.finalScore = 0;
        this.numberOfRound = 0;
        this.liar = false;
    }

    final String getName() {
        return name;
    }

    final Map<Integer, Goods> getAllGoods() {
        return allGoods;
    }

    final void setAllGoods(final Map<Integer, Goods> allGoods) {
        this.allGoods = allGoods;
    }

    final void setName(final String name) {
        this.name = name;
    }

    final int getFinalScore() {
        return finalScore;
    }

    final void setFinalScore(final int finalScore) {
        this.finalScore = finalScore;
    }

    final int getId() {
        return this.id;
    }

    final void setId(final int id) {
        this.id = id;
    }

    final void addFinalScore() {
        this.finalScore = this.coins + this.bonus + this.profitLegal + this.profitIllegal;
    }

    final int getBonus() {
        return bonus;
    }

    final void setBonus(final int bonus) {
        this.bonus = bonus;
    }

    final int getBribe() {
        return bribe;
    }

    final boolean hasBribe() {
        return this.bribe > 0;
    }

    final void setBribe(final int bribe) {
        this.bribe = bribe;
    }

    final int getProfitLegal() {
        return profitLegal;
    }

    final void setProfitLegal(final int profitLegal) {
        this.profitLegal = profitLegal;
    }

    final int getProfitIllegal() {
        return profitIllegal;
    }

    final void setProfitIllegal(final int profitIllegal) {
        this.profitIllegal = profitIllegal;
    }

    final ArrayList<Goods> getBag() {
        return bag;
    }

    final void setBag(final ArrayList<Goods> bag) {
        this.bag = bag;
    }

    final int getNumberOfRound() {
        return numberOfRound;
    }

    final void setNumberOfRound(final int numberOfRound) {
        this.numberOfRound = numberOfRound;
    }

    final int getCoins() {
        return coins;
    }

    final void setCoins(final int coins) {
        this.coins = coins;
    }

    final void addBribe(final int aditionalBribe) {
        this.coins += aditionalBribe;
    }

    final void addKingBonus(final int kingBonus) {
        this.bonus += kingBonus;
    }

    final void addQueenBonus(final int queenBonus) {
        this.bonus += queenBonus;
    }

    public static int getCOINS() {
        return COINS;
    }

    public static int getMaxSheriff() {
        return MAX_SHERIFF;
    }

    final int getMaxId() {
        return MAX_ID;
    }

    final int getMaxSizeBag() {
        return MAX_SIZE_BAG;
    }

    final int getIdApple() {
        return ID_APPLE;
    }

    private Goods getDeclaredGood() {
        return declaredGood;
    }

    final void setDeclaredGood(final Goods declaredGood) {
        this.declaredGood = declaredGood;
    }

    public abstract void merchant(List<Integer> assetIds);

    public abstract void sheriff(ArrayList<Player> players);

    final ArrayList<Goods> getAllowedGoods() {
        return allowedGoods;
    }

    final void setAllowedGoods(final ArrayList<Goods> allowedGoods) {
        this.allowedGoods = allowedGoods;
    }

    final int getMinCoins() {
        return MIN_COINS;
    }

    final void checkBag(final Player player) {
        if (this.getCoins() >= MIN_SUM_FOR_SHERIFF) {
            player.liar = false;
            if (player.getId() != this.getId()) {
                for (Goods good : player.getBag()) {
                    if (good.getId() != player.getDeclaredGood().getId()) {
                        player.liar = true;
                    }
                }
                if (player.liar) {
                    for (Goods good : player.getBag()) {
                        int currentCoins = player.getCoins();
                        // Daca e bun nedeclarat sau ilegal, atunci nu ajunge in Nottingham
                        if (good.getId() != player.getDeclaredGood().getId()
                                || good.getType() == GoodsType.Illegal) {
                            this.coins += good.getPenalty(); // Ii adaug bani serifului
                            int actualCoins = currentCoins - good.getPenalty();
                            player.setCoins(actualCoins); // Ii iau din bani comerciantului
                        } else {
                            player.getAllowedGoods().add(good); // Bunul poate fi adus in Nottingham
                        }
                    }

                } else {
                    // Daca nu e mincinos atunci isi pune toate bunurile pe taraba din Nottingham
                    int currentCoins = player.getCoins();
                    ArrayList<Goods> playerBag = player.getBag();

                    // Ii iau serifului din bani
                    this.coins -= playerBag.size() * player.getDeclaredGood().getPenalty();

                    int actualCoins = currentCoins + playerBag.size()
                            * player.getDeclaredGood().getPenalty();
                    // Ii adaug bani comerciantului
                    player.setCoins(actualCoins);
                    for (Goods good : player.getBag()) {
                        player.getAllowedGoods().add(good);
                    }
                }
            }
        } else {
            // Comerciantul isi va aduce toate bunurile in Nottingham daca nu e controlat
            for (Goods good : player.getBag()) {
                player.getAllowedGoods().add(good);
            }
        }
    }

    /**
     * Aceasta metoda ajuta la afisarea clasamentului final.
     * @return un string ce reprezinta modul in care e afisat scorul fiecarui jucator
     */
    public String toString() {
        return this.id + " " + this.name + " " + this.finalScore;
    }

}
