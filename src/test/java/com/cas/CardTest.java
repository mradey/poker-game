package com.cas;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CardTest {

  @Test
  void getFullNameFromValueTest() {
    String suit = "D";

    Card card = new Card("A", suit);
    assertThat(card.getFullNameFromValue()).isEqualTo("ACE");

    card = new Card("K", suit);
    assertThat(card.getFullNameFromValue()).isEqualTo("KING");

    card = new Card("Q", suit);
    assertThat(card.getFullNameFromValue()).isEqualTo("QUEEN");

    card = new Card("J", suit);
    assertThat(card.getFullNameFromValue()).isEqualTo("JACK");

    card = new Card("T", suit);
    assertThat(card.getFullNameFromValue()).isEqualTo("TEN");

    card = new Card("9", suit);
    assertThat(card.getFullNameFromValue()).isEqualTo("NINE");

    card = new Card("8", suit);
    assertThat(card.getFullNameFromValue()).isEqualTo("EIGHT");

    card = new Card("7", suit);
    assertThat(card.getFullNameFromValue()).isEqualTo("SEVEN");

    card = new Card("6", suit);
    assertThat(card.getFullNameFromValue()).isEqualTo("SIX");

    card = new Card("5", suit);
    assertThat(card.getFullNameFromValue()).isEqualTo("FIVE");

    card = new Card("4", suit);
    assertThat(card.getFullNameFromValue()).isEqualTo("FOUR");

    card = new Card("3", suit);
    assertThat(card.getFullNameFromValue()).isEqualTo("THREE");

    card = new Card("2", suit);
    assertThat(card.getFullNameFromValue()).isEqualTo("TWO");

    card = new Card("1", suit);
    assertThat(card.getFullNameFromValue()).isEqualTo("1");
  }
}
