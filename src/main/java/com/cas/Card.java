package com.cas;

import lombok.Data;

import java.util.Comparator;
import java.util.Map;

@Data
public class Card implements Comparable<Card> {

  public static final Comparator<Card> COMPARE_CARD_VALUES =
      Comparator.comparing(Card::getValueAsInt);

  private static final Map<String, Integer> MAP_CARD_VALUES =
      Map.ofEntries(
          Map.entry("2", 2),
          Map.entry("3", 3),
          Map.entry("4", 4),
          Map.entry("5", 5),
          Map.entry("6", 6),
          Map.entry("7", 7),
          Map.entry("8", 8),
          Map.entry("9", 9),
          Map.entry("T", 10),
          Map.entry("J", 11),
          Map.entry("Q", 12),
          Map.entry("K", 13),
          Map.entry("A", 14));

  private final String value;
  private final int valueAsInt;
  private final Suit suit;

  public Card(final String value, final String suit) {
    this.value = value.toUpperCase();
    this.valueAsInt = MAP_CARD_VALUES.getOrDefault(this.value, -1);
    this.suit = Suit.valueOfLetter(suit);
  }

  public String getFullNameFromValue() {
    switch (this.value) {
      case "A":
        return "ACE";
      case "K":
        return "KING";
      case "Q":
        return "QUEEN";
      case "J":
        return "JACK";
      case "T":
        return "TEN";
      case "9":
        return "NINE";
      case "8":
        return "EIGHT";
      case "7":
        return "SEVEN";
      case "6":
        return "SIX";
      case "5":
        return "FIVE";
      case "4":
        return "FOUR";
      case "3":
        return "THREE";
      case "2":
        return "TWO";
      default:
        return this.value;
    }
  }

  @Override
  public int compareTo(Card otherCard) {
    return Integer.compare(this.getValueAsInt(), otherCard.getValueAsInt());
  }
}
