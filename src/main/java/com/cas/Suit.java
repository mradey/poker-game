package com.cas;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum Suit {
  CLUBS("C"),
  DIAMONDS("D"),
  HEARTS("H"),
  SPADES("S");

  public static final Map<String, Suit> BY_SINGLE_LETTER = new HashMap<>();

  static {
    for (Suit s : Suit.values()) {
      BY_SINGLE_LETTER.put(s.getLetter(), s);
    }
  }

  private final String letter;

  Suit(final String letter) {
    this.letter = letter.toUpperCase();
  }

  public static Suit valueOfLetter(final String letter) {
    return BY_SINGLE_LETTER.get(letter.toUpperCase());
  }
}
