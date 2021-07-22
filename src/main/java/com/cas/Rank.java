package com.cas;

import lombok.Getter;

@Getter
public enum Rank {
  HIGH_CARD(0),
  PAIR(1),
  TWO_PAIRS(2),
  THREE_OF_A_KIND(3),
  STRAIGHT(5),
  FLUSH(6),
  FULL_HOUSE(7),
  FOUR_OF_A_KIND(9),
  STRAIGHT_FLUSH(10);

  private final int value;

  Rank(final int value) {
    this.value = value;
  }
}
