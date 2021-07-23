package com.cas;

public class CardCounts implements Comparable<CardCounts> {
    public int count;
    public Card card;

    public CardCounts(Card card, int count) {
        this.card = card;
        this.count = count;
    }
    @Override
    public int compareTo(CardCounts c) {
        return c.count - this.count != 0 ? c.count - this.count : c.card.compareTo(this.card);
    }

    @Override
    public String toString() {
        return " {" + count + " " + card + "} ";
    }
}