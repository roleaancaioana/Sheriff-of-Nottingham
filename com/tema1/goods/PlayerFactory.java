package com.tema1.goods;

public abstract class PlayerFactory {
    public static Player getTypeOfPlayer(final String name) {
        if (name.equals("greedy")) {
            return new GreedyPlayer();
        }
        if (name.equals("bribed")) {
            return new BribedPlayer();
        }
        if (name.equals("basic")) {
            return new BasicPlayer();
        }
        return null;
    }
}
