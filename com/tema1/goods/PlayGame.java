package com.tema1.goods;

import com.tema1.main.GameInput;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public abstract class PlayGame {
    private static List<Integer> assetIds;
    private static ArrayList<Player> players;
    private static int rounds;
    private static final int MAX_ID_LEGAL = 10;
    private static final int GOODS_IN_HAND = 10;

    public static void getGameParameters(final GameInput gameInput) {
        rounds = gameInput.getRounds();
        players = new ArrayList<Player>();
        assetIds = gameInput.getAssetIds();
        List<String> playerNames = gameInput.getPlayerNames();
        int counter = 0;

        // Se adauga fiecare jucator la arrayList-ul "players" utilizand factory-ul PlayerFactory.
        for (String playerName : playerNames) {
            players.add(PlayerFactory.getTypeOfPlayer(playerName));
            players.get(counter).setName(playerName.toUpperCase());
            counter++;
        }

        for (int i = 0; i < players.size(); i++) {
            players.get(i).setId(i); // Setez id-urile jucatorilor
        }
    }


    public static void playGame() {
        int countPlayers = 0, counter = 0, numberOfRound = 1;
        int numTurns = players.size() * rounds; // numarul de subrunde jucate

        for (int i = 0; i < numTurns; ++i) {
            // Seriful este al i-lea jucator, pentru fiecare parcurgere a vectorului acestora.
            Player currSheriff = players.get(i % players.size());
            if (countPlayers == players.size()) {
                countPlayers = 0;
                numberOfRound++;
            }
            countPlayers++;
            for (Player player : players) {
                if (player.getId() != currSheriff.getId()) {
                    player.setNumberOfRound(numberOfRound);
                    // Dupa fiecare subrunda,
                    // comerciantii vor avea un sac nou din care isi vor alege
                    // bunurile pe care vor incerca sa le aduca in Nottingham.
                    if (player.getBag().size() != 0) {
                        player.getBag().clear();
                    }
                    // Pentru fiecare jucator creez lista cu id-urile bunurilor
                    // pe care le va avea in mana
                    List<Integer> goodsInHandIds = new ArrayList<Integer>();
                    for (int j = counter * GOODS_IN_HAND; j < (counter + 1) * GOODS_IN_HAND; j++) {
                        goodsInHandIds.add(assetIds.get(j));
                    }

                    player.merchant(goodsInHandIds);
                    counter++;
                }
            }

            // Seriful verifica jucatorii conform algoritmului sau.
            try {
                currSheriff.sheriff(players);
            } catch (Exception e) {

            }
        }
        computeFinalScores();
    }

    // Aceasta metoda ma ajuta sa calculez scorul final al fiecarui jucator.
    private static void computeFinalScores() {
        GoodsFactory goodsFactory = GoodsFactory.getInstance();
        Map<Integer, Goods>  allGoods = goodsFactory.getAllGoods();
        int c = 0;
        // Daca un jucator a bagat bunuri ilegale in Nottingham
        // atunci automat a bagat si niste legale(din bonus).
        for (Player player : players) {
            for (int j = 0; j < player.getAllowedGoods().size(); j++) {
                try {
                    if (player.getAllowedGoods().get(j).getType() == GoodsType.Illegal) {
                        IllegalGoods illegalGood = (IllegalGoods) player.getAllowedGoods().get(j);
                        Map<Goods, Integer> illegalBonus = illegalGood.getIllegalBonus();
                        for (int id = 0; id < MAX_ID_LEGAL; id++) {
                            Goods asset = allGoods.get(id);
                            if (illegalBonus.containsKey(asset)) {
                                for (int i = 0; i < illegalBonus.get(asset); ++i) {
                                    player.getAllowedGoods().add(asset);
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                }
            }
        }

        int[][] bonusMatrix = new int[players.size()][MAX_ID_LEGAL];
        for (int i = 0; i < players.size(); ++i) {
            for (int j = 0; j < MAX_ID_LEGAL; ++j) {
                bonusMatrix[i][j] = 0;
            }
        }

        for (Player player : players) {
            for (Goods good : player.getAllowedGoods()) {
                if (good.getType() == GoodsType.Legal) {
                    bonusMatrix[c][good.getId()]++;
                }
            }
            c++;
        }

        for (int j = 0; j < MAX_ID_LEGAL; ++j) {
            int idPlayer = -1;
            int king = bonusMatrix[0][j];
            int queen = 0;
            int aux = -1;
            for (int i = 0; i < players.size(); ++i) {
                if (bonusMatrix[i][j] > king) {
                    king = bonusMatrix[i][j];
                    aux = i;
                }
            }

            if (king == bonusMatrix[0][j]) {
                aux = 0;
            }

            // Caut al doilea cel mai mare element de pe coloana
            // pentru a vedea cine ia Queen's bonus.
            for (int i = 0; i < players.size(); i++) {
                if (bonusMatrix[i][j] > queen && aux != i) {
                    queen = bonusMatrix[i][j];
                    idPlayer = i;
                }
            }

            if (king == queen) {
                /*  Daca 2 jucatori sunt favoriti sa ia King's Bonus pentru un bun,
                    atunci acestia vor lua in ordinea crescatoare a id-urilor King si Queen bonus.
                */
                int ok = 0;
                for (int i = 0; i < players.size(); ++i) {
                    if (bonusMatrix[i][j] == king && ok == 0 && bonusMatrix[i][j] != 0) {
                        LegalGoods good = (LegalGoods) allGoods.get(j);
                        players.get(i).addKingBonus(good.getKingBonus());
                        ok++;
                    } else if (bonusMatrix[i][j] == king && ok == 1 && bonusMatrix[i][j] != 0) {
                        LegalGoods good = (LegalGoods) allGoods.get(j);
                        players.get(i).addQueenBonus(good.getQueenBonus());
                        ok++;
                    }
                }
            }

            if (king != queen) {
                for (int i = 0; i < players.size(); ++i) {
                    if (bonusMatrix[i][j] == king && bonusMatrix[i][j] != 0) {
                        LegalGoods good = (LegalGoods) allGoods.get(j);
                        players.get(i).addKingBonus(good.getKingBonus());
                    }
                    if (bonusMatrix[i][j] == queen && bonusMatrix[i][j] != 0 && idPlayer == i) {
                        LegalGoods good = (LegalGoods) allGoods.get(j);
                        players.get(i).addQueenBonus(good.getQueenBonus());
                    }
                }
            }
        }

        for (Player player : players) {
            int profitLegal = 0;
            int profitIllegal = 0;
            for (Goods good : player.getAllowedGoods()) {
                if (good.getType() == GoodsType.Legal) {
                    profitLegal += good.getProfit();
                } else {
                    profitIllegal += good.getProfit();
                }
            }
            player.setProfitLegal(profitLegal);
            player.setProfitIllegal(profitIllegal);
        }
        for (Player player : players) {
            player.addFinalScore();
        }
    }

    // Aceasta metoda ma ajuta sa afisez clasamentul jucatorilor la finalul jocului.
    public static void getLeaderBoard() {
        players.sort(new FinalScoreComparator());
        for (Player player : players) {
            System.out.println(player);
        }
    }
}
