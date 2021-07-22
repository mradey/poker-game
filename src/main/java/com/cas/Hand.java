package com.cas;

import lombok.Data;
import lombok.NonNull;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class Hand {

  @NonNull private final Collection<Card> cards;

  private final Map<Integer, List<Card>> map_CardValueWithMatchingCards;
  private final Card highCard;
  private final Rank highestRank;

  protected Hand(@NonNull Collection<Card> cards) {
    this.cards = cards.stream().sorted(Card.COMPARE_CARD_VALUES).collect(Collectors.toList());
    this.map_CardValueWithMatchingCards = this.buildMapOfCards();
    this.highCard = ((List<Card>) this.cards).get(4);
    this.highestRank = this.determineRank();
  }

  protected Map<Integer, List<Card>> buildMapOfCards() {
    return this.cards.stream()
        .collect(Collectors.groupingBy(Card::getValueAsInt, Collectors.toList()));
  }

  public static Hand newHand(final Collection<String> cards) {
    if (null == cards)
      throw new NullPointerException("Unable to parse null hand in List<String> method!");

    final Set<Card> cardObjectList = new HashSet<>();
    for (String numberWithSuite : cards) {
      final String number = numberWithSuite.substring(0, 1);
      final String suite = numberWithSuite.substring(1);
      cardObjectList.add(new Card(number, suite));
    }
    // cardObjectList is a set, therefore any dupes will not be added
    if (cards.size() != 5) {
      throw new RuntimeException(
          String.format(
              "Poker hand cannot be less than or greater than 5 cards! Size of hand parsed: [%s]",
              cards.size()));
    }
    return new Hand(cardObjectList);
  }

  public static Hand newHand(final String[] cards) {
    if (null == cards)
      throw new NullPointerException("Unable to parse null hand in String[] method!");
    return newHand(Arrays.asList(cards));
  }

  /** 2 of the 5 cards in the hand have the same value. */
  public boolean hasPair() {
    return !this.getPairCards().isEmpty();
  }

  public List<Card> getPairCards() {
    return this.getCardsForExpectedSize(2);
  }

  /** The hand contains 2 different pairs. */
  public boolean hasTwoPairs() {
    return this.getTwoPairsCards().size() == 2;
  }

  public List<List<Card>> getTwoPairsCards() {
    List<List<Card>> list_twoPairs = new ArrayList<>();
    for (Integer cardValue : this.map_CardValueWithMatchingCards.keySet()) {
      if (map_CardValueWithMatchingCards.get(cardValue).size() == 2)
        list_twoPairs.addAll(List.of(map_CardValueWithMatchingCards.get(cardValue)));
    }
    return list_twoPairs;
  }

  /** Three of the cards in the hand have the same value. */
  public boolean hasThreeOfAKind() {
    return !getThreeOfAKindCards().isEmpty();
  }

  public List<Card> getThreeOfAKindCards() {
    return this.getCardsForExpectedSize(3);
  }

  /**
   * Hand contains 5 cards with consecutive values.
   *
   * <p>Hands which both contain a straight are ranked by their highest card.
   *
   * <p>By using reduce, we can check if the second card's value minus 1 is equal to the first
   * card's value.
   *
   * <p>Then, check if the final value is any other number than 1.
   *
   * <p>If 1, we use that as an indicator that there is a card value greater than the previous card
   * value by more than 1.
   */
  public boolean hasStraight() {
    if (this.map_CardValueWithMatchingCards.size() != 5) return false;
    return this.map_CardValueWithMatchingCards.keySet().stream()
            .reduce(
                (card1Value, card2Value) -> {
                  if (card1Value.compareTo(card2Value - 1) == 0) return card2Value;
                  else return 1;
                })
            .get()
        != 1;
  }

  /**
   * Hand contains 5 cards of the same suit.
   *
   * <p>Hands which are both flushes are ranked using the rules for High Card.
   */
  public boolean hasFlush() {
    return this.cards.stream().map(Card::getSuit).distinct().count() == 1;
  }

  /**
   * 3 cards of the same value, with the remaining 2 cards forming a pair.
   *
   * <p>Ranked by the value oF the 3 cards.
   */
  public boolean hasFullHouse() {
    return this.hasThreeOfAKind() && this.hasPair();
  }

  /** For the sake of simplicity, this does not check if hasFullHouse is true */
  public List<Card> getFullHouseCards() {
    return this.getCardsForExpectedSize(3);
  }

  /**
   * 4 cards with the same value.
   *
   * <p>Ranked by the value of the 4 cards
   */
  public boolean hasFourOfAKind() {
    return !getFourOfAKindCards().isEmpty();
  }

  public List<Card> getFourOfAKindCards() {
    return getCardsForExpectedSize(4);
  }

  public List<Card> getCardsForExpectedSize(final int expectedSize) {
    for (Map.Entry<Integer, List<Card>> entry : this.map_CardValueWithMatchingCards.entrySet()) {
      if (entry.getValue().size() == expectedSize) return entry.getValue();
    }
    return Collections.emptyList();
  }

  /**
   * 5 cards of the same suit with consecutive values.
   *
   * <p>Ranked by the highest card in the hand.
   */
  public boolean hasStraightFlush() {
    return this.hasFlush() && this.hasStraight();
  }

  public Rank determineRank() {
    if (this.hasStraightFlush()) return Rank.STRAIGHT_FLUSH;
    else if (this.hasFourOfAKind()) return Rank.FOUR_OF_A_KIND;
    else if (this.hasFullHouse()) return Rank.FULL_HOUSE;
    else if (this.hasFlush()) return Rank.FLUSH;
    else if (this.hasStraight()) return Rank.STRAIGHT;
    else if (this.hasThreeOfAKind()) return Rank.THREE_OF_A_KIND;
    else if (this.hasTwoPairs()) return Rank.TWO_PAIRS;
    else if (this.hasPair()) return Rank.PAIR;
    else return Rank.HIGH_CARD;
  }
}
