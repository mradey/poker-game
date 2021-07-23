package com.cas;
import static com.cas.Rank.*;

import java.util.List;

public class HandRank implements Comparable<HandRank>{
    private Rank rank;
    private List<CardCounts> cards;
    public HandRank(Rank rank, List<CardCounts> cards) {
        this.rank = rank;
        this.cards = cards;
    }
    public Rank getRank() {
        return rank;
    }

    public List<CardCounts> getCards() {
        return cards;
    }

    @Override
    public int compareTo(HandRank otherHandRank) {
      int rankCompare = otherHandRank.getRank().compareTo(getRank());
      if(rankCompare != 0) return rankCompare;
      return compareListCardCounts(otherHandRank.getCards(), getCards());
    }

    public int compareListCardCounts(List<CardCounts> l1, List<CardCounts> l2) {
        for(int i = 0; i < l1.size(); i++) {
            int card1Value = l1.get(i).card.getValueAsInt();
            int card2Value = l2.get(i).card.getValueAsInt();
            int diff = card1Value - card2Value;
            if(diff != 0) return diff;
        }
        return 0;
    }

    @Override
    public String toString() {
        if(rank == Rank.HIGH_CARD) return "with high card: " + cards.get(0).card.getFullNameFromValue();
        if(rank == Rank.PAIR) return "with pair: " + cards.get(0).card.getFullNameFromValue();
        if(rank == Rank.TWO_PAIRS) return "with two pair: " + cards.get(0).card.getFullNameFromValue() + " over " + cards.get(1).card.getFullNameFromValue();
        if(rank == Rank.THREE_OF_A_KIND) return "with three of a kind: " + cards.get(0).card.getFullNameFromValue();
        if(rank == Rank.STRAIGHT) return "with straight: " + cards.get(0).card.getFullNameFromValue() + " high";
        if(rank == Rank.FLUSH) return "with flush: " + cards.get(0).card.getFullNameFromValue() + " high";
        if(rank == Rank.FULL_HOUSE) return "with full house: " + cards.get(0).card.getFullNameFromValue() + " over " + cards.get(1).card.getFullNameFromValue();
        if(rank == Rank.FOUR_OF_A_KIND) return "with four of a kind: " + cards.get(0).card.getFullNameFromValue();
        if(rank == Rank.STRAIGHT_FLUSH) return "with straight flush: " + cards.get(0).card.getFullNameFromValue() + " high";
        return "";
    }
}