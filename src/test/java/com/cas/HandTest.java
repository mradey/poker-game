package com.cas;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class HandTest {

  @Test
  void canary() {
    String[] arrHand = new String[] {"2C", "3H", "4S", "KC", "AH"};
    Hand hand = Hand.newHand(arrHand);

    boolean isFirstCardClubs = ((List<Card>) hand.getCards()).get(0).getSuit() == Suit.CLUBS;
    assertThat(isFirstCardClubs).isTrue();
  }

  @Test
  void parseHandStringArrayParamNoException() {
    String[] arrHand = new String[] {"2C", "3H", "4S", "KC", "AH"};

    List<Card> expectedValue =
        List.of(
            new Card("2", "C"),
            new Card("3", "H"),
            new Card("4", "S"),
            new Card("K", "C"),
            new Card("A", "H"));

    Hand actualValue = Hand.newHand(arrHand);

    assertThat(actualValue.getCards()).containsExactlyElementsOf(expectedValue);
  }

  @Test
  void parseHandListStringParamNoException() {
    List<String> listHand = List.of("2C", "3H", "4S", "KC", "AH");

    List<Card> expectedValue =
        List.of(
            new Card("2", "C"),
            new Card("3", "H"),
            new Card("4", "S"),
            new Card("K", "C"),
            new Card("A", "H"));

    Hand actualValue = Hand.newHand(listHand);

    assertThat(actualValue.getCards()).containsExactlyElementsOf(expectedValue);
  }

  @Test
  void parseHandExceptionNullListStringParameter() {
    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(() -> Hand.newHand((List<String>) null))
        .withMessage("Unable to parse null hand in List<String> method!");
  }

  @Test
  void parseHandExceptionNullStringArrayParameter() {
    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(() -> Hand.newHand((String[]) null))
        .withMessage("Unable to parse null hand in String[] method!");
  }

  @Test
  void parseHandExceptionSize() {
    List<String> listHand = List.of("2C", "3H");

    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(() -> Hand.newHand(listHand))
        .withMessage(
            "Poker hand cannot be less than or greater than 5 cards! Size of hand parsed: [2]");
  }

  @Test
  void getHighCardReturnsAce() {
    String[] arrHand = new String[] {"2C", "3H", "4S", "KC", "AH"};
    Hand hand = Hand.newHand(arrHand);

    Card expectedCard = new Card("A", "H");

    Card actualCard = hand.getHighCard();

    assertThat(actualCard).isEqualByComparingTo(expectedCard);
  }

  @Test
  void getHighCardReturnsEitherAceWhenTwoAcesExist() {
    String[] arrHand = new String[] {"2C", "3H", "4S", "AC", "AD"};
    Hand hand = Hand.newHand(arrHand);

    Card expectedCard = new Card("A", "H");

    Card actualCard = hand.getHighCard();

    assertThat(actualCard).isEqualByComparingTo(expectedCard);
  }

  @Test
  void hasPairIsTrue() {
    String[] arrHand = new String[] {"2C", "3D", "4S", "AC", "AD"};
    Hand hand = Hand.newHand(arrHand);

    assertThat(hand.hasPair()).isTrue();
  }

  @Test
  void hasPairIsFalse() {
    String[] arrHand = new String[] {"2C", "3D", "4S", "KC", "AD"};
    Hand hand = Hand.newHand(arrHand);

    assertThat(hand.hasPair()).isFalse();
  }

  @Test
  void hasTwoPairIsTrue() {
    String[] arrHand = new String[] {"2C", "2D", "4S", "4C", "AD"};
    Hand hand = Hand.newHand(arrHand);

    assertThat(hand.hasTwoPairs()).isTrue();
  }

  @Test
  void hasTwoPairIsFalse() {
    String[] arrHand = new String[] {"2C", "2D", "4S", "KC", "AD"};
    Hand hand = Hand.newHand(arrHand);

    assertThat(hand.hasTwoPairs()).isFalse();
  }

  @Test
  void hasThreeOfAKindIsTrue() {
    String[] arrHand = new String[] {"2C", "2D", "2S", "KC", "AD"};
    Hand hand = Hand.newHand(arrHand);

    assertThat(hand.hasThreeOfAKind()).isTrue();
  }

  @Test
  void hasThreeOfAKindIsFalse() {
    String[] arrHand = new String[] {"2C", "2D", "4S", "KC", "AD"};
    Hand hand = Hand.newHand(arrHand);

    assertThat(hand.hasThreeOfAKind()).isFalse();
  }

  @Test
  void hasStraightIsTrue() {
    String[] arrHand = new String[] {"2C", "3D", "4S", "5C", "6H"};
    Hand hand = Hand.newHand(arrHand);

    assertThat(hand.hasStraight()).isTrue();
  }

  @Test
  void hasStraightIsFalse() {
    String[] arrHand = new String[] {"2C", "3D", "4S", "5C", "7D"};
    Hand hand = Hand.newHand(arrHand);

    assertThat(hand.hasStraight()).isFalse();
  }

  @Test
  void hasStraightIsFalseFromSizeNotEqualToFive() {
    String[] arrHand = new String[] {"2C", "2D", "4S", "5C", "6D"};
    Hand hand = Hand.newHand(arrHand);

    assertThat(hand.hasStraight()).isFalse();
  }

  @Test
  void hasFlushIsTrue() {
    String[] arrHand = new String[] {"2C", "3C", "4C", "5C", "8C"};
    Hand hand = Hand.newHand(arrHand);

    assertThat(hand.hasFlush()).isTrue();
  }

  @Test
  void hasFlushIsFalse() {
    String[] arrHand = new String[] {"2C", "3C", "4C", "5C", "7D"};
    Hand hand = Hand.newHand(arrHand);

    assertThat(hand.hasFlush()).isFalse();
  }

  @Test
  void hasFullHouseIsTrue() {
    String[] arrHand = new String[] {"2C", "2D", "2S", "5C", "5D"};
    Hand hand = Hand.newHand(arrHand);

    assertThat(hand.hasFullHouse()).isTrue();
  }

  @Test
  void hasFullHouseIsFalse() {
    String[] arrHand = new String[] {"2C", "2D", "2S", "2H", "5D"};
    Hand hand = Hand.newHand(arrHand);

    assertThat(hand.hasFullHouse()).isFalse();
  }

  @Test
  void hasFourOfAKindIsTrue() {
    String[] arrHand = new String[] {"2C", "2D", "2S", "2H", "5D"};
    Hand hand = Hand.newHand(arrHand);

    assertThat(hand.hasFourOfAKind()).isTrue();
  }

  @Test
  void hasFourOfAKindIsFalse() {
    String[] arrHand = new String[] {"2C", "2D", "2S", "3H", "5D"};
    Hand hand = Hand.newHand(arrHand);

    assertThat(hand.hasFourOfAKind()).isFalse();
  }

  @Test
  void hasStraightFlushIsTrue() {
    String[] arrHand = new String[] {"3C", "4C", "5C", "6C", "7C"};
    Hand hand = Hand.newHand(arrHand);

    assertThat(hand.hasStraightFlush()).isTrue();
  }

  @Test
  void hasStraightFlushIsFalse() {
    String[] arrHand = new String[] {"2C", "2D", "2S", "3H", "5D"};
    Hand hand = Hand.newHand(arrHand);

    assertThat(hand.hasStraightFlush()).isFalse();
  }
}
