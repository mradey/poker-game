package com.cas;

import java.util.*;
import java.util.stream.Collectors;

public class Hand {
  private final List<CardCounts> sortedListCardCounts;
  private final Set<String> suits;

  protected Hand(List<CardCounts> cardCounts, Set<String> suits) {
    this.suits = suits;
    this.sortedListCardCounts = cardCounts;
  }

  public HandRank getHandRank() {
    return new HandRank(getRank(), sortedListCardCounts);
  }
  public Rank getRank() {
    //check for flush and straight
    boolean isFlush = isFlush();
    boolean isStraight = isStraight();
    if(isFlush && isStraight) return Rank.STRAIGHT_FLUSH;
    if(isFlush) return Rank.FLUSH;
    if(isStraight) return Rank.STRAIGHT;
    return getRankFromCombo();
  }

  /*
    1) if element 1 numCards = 4: its four of a kind with the 5th highest card being element 2
    2) if element 1 numCards = 3:
      a) if element 2 numCards = 2: full house
      b) if element 2 numCards = 1: three of a kind with the 4th and 5th high cards being the next two elements in order
    3) if element 1 numCards = 2:
      a) if element 2 numCards = 2: two pair
      b) if element 2 numCards = 1: pair with the next 3 elements representing the highest cards in order
    4) the list is the high cards in order
  */
  public Rank getRankFromCombo() {
    int highCardCount = sortedListCardCounts.get(0).count;
    int highCardCount2 = sortedListCardCounts.get(1).count;
    if(highCardCount == 4) return Rank.FOUR_OF_A_KIND;
    if(highCardCount == 3) {
      if(highCardCount2 == 2) return Rank.FULL_HOUSE;
      else return Rank.THREE_OF_A_KIND;
    }
    if(highCardCount == 2) {
      if(highCardCount2 == 2) return Rank.TWO_PAIRS;
      else return Rank.PAIR;
    }
    return Rank.HIGH_CARD;
  }

  public boolean isFlush() {
    return this.suits.size() == 1;
  }

  public boolean isStraight() {
    if(sortedListCardCounts.size() != 5) return false;
    return sortedListCardCounts.get(0).card.getValueAsInt() - sortedListCardCounts.get(4).card.getValueAsInt() == 4;
  }
  protected static List<CardCounts> buildSortedListOfCardCounts(Map<String, Integer> mapCardCounts) {
    return mapCardCounts.entrySet().stream()
      .map(ent-> new CardCounts(new Card(ent.getKey()), ent.getValue()))
      .sorted()
      .collect(Collectors.toList());
  }


  public static Hand newHand(final Collection<String> cards) {
    if (null == cards)
      throw new NullPointerException("Unable to parse null hand in List<String> method!");

    Map<String, Integer> valueCounts = new HashMap<>();
    Set<String> suits = new HashSet<>();
    for (String numberWithSuite : cards) {
      final String number = numberWithSuite.substring(0, 1);
      final String suite = numberWithSuite.substring(1);
      int val = valueCounts.containsKey(number) ? valueCounts.get(number) : 0;
      valueCounts.put(number, val + 1);
      if(!suits.contains(suite)) suits.add(suite);
    }
    // cardObjectList is a set, therefore any dupes will not be added
    if (cards.size() != 5) {
      throw new RuntimeException(
          String.format(
              "Poker hand cannot be less than or greater than 5 cards! Size of hand parsed: [%s]",
              cards.size()));
    }
    return new Hand(buildSortedListOfCardCounts(valueCounts), suits);
  }

}
