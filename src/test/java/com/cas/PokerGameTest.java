package com.cas;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class PokerGameTest {

  private final PokerGame POKER_GAME = spy(new PokerGame());

  @Nested
  class SampleInputCasesFromDojo {

    @Test
    void whiteWinsWithHighCardAce() {
      // Black: 2H 3D 5S 9C KD, 32 if added together
      // White: 2C 3H 4S 8C AH, 31 if added together

      String[] inputBlack = new String[] {"2H", "3D", "5S", "9C", "KD"};
      String[] inputWhite = new String[] {"2C", "3H", "4S", "8C", "AH"};

      String expectedValue = "PLAYER [WHITE] WINS! REASON = HIGH CARD: [ACE]";

      String actualValue = POKER_GAME.play(inputBlack, inputWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    void blackWinsWithFullHouseFourOverTwo() {
      // Black: 2H 4S 4C 2D 4H  White: 2S 8S AS QS 3S

      String[] inputBlack = new String[] {"2H", "4S", "4C", "2D", "4H"};
      String[] inputWhite = new String[] {"2S", "3S", "8S", "QS", "AS"};

      String expectedValue = "PLAYER [BLACK] WINS! REASON = [FULL_HOUSE] over [FLUSH]";

      String actualValue = POKER_GAME.play(inputBlack, inputWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    void blackWinsWithHighCardNine() {
      String[] inputBlack = new String[] {"2H", "3D", "5S", "9C", "KD"};
      String[] inputWhite = new String[] {"2C", "3H", "4S", "8C", "KH"};

      String expectedValue = "PLAYER [BLACK] WINS! REASON = HIGH CARD: [NINE]";

      String actualValue = POKER_GAME.play(inputBlack, inputWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
    }

    @DisplayName("No winning player as no player has a greater high card value than the other")
    @Test
    void tieBreaker() {
      String[] inputBlack = new String[] {"2H", "3D", "5S", "9C", "KD"};
      String[] inputWhite = new String[] {"2D", "3H", "5C", "9S", "KH"};

      String expectedValue = "TIE";

      String actualValue = POKER_GAME.play(inputBlack, inputWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
    }
  }

  @Nested
  class HandlePairsTests {

    @Test
    void blackWinsFromPairRankValueBeingGreaterThanWhiteHand() {
      String[] arrBlack = new String[] {"3C", "3D", "5C", "6H", "7S"};
      String[] arrWhite = new String[] {"2C", "2H", "6C", "7D", "9S"};

      String expectedValue =
          "PLAYER [BLACK] WINS! REASON = [PAIR] value of [6] over [PAIR] value of [4]";

      String actualValue = POKER_GAME.play(arrBlack, arrWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
      verify(POKER_GAME).handlePairs(any(Hand.class), any(Hand.class));
    }

    @Test
    void whiteWinsFromPairRankValueBeingGreaterThanBlackHand() {
      String[] arrBlack = new String[] {"2C", "2H", "6C", "7D", "9S"};
      String[] arrWhite = new String[] {"3C", "3D", "5C", "6H", "7S"};

      String expectedValue =
          "PLAYER [WHITE] WINS! REASON = [PAIR] value of [6] over [PAIR] value of [4]";

      String actualValue = POKER_GAME.play(arrBlack, arrWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
      verify(POKER_GAME).handlePairs(any(Hand.class), any(Hand.class));
    }

    @Test
    void blackWinsFromPairRankValueAfterFirstSetIsEqualRanking() {
      String[] arrBlack = new String[] {"2D", "2S", "5D", "QH", "TS"};
      String[] arrWhite = new String[] {"2C", "2H", "5C", "7D", "9S"};

      String expectedValue =
          "PLAYER [BLACK] WINS! REASON = [PAIR] value of [27] over [PAIR] value of [21]";

      String actualValue = POKER_GAME.play(arrBlack, arrWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
      verify(POKER_GAME).handlePairs(any(Hand.class), any(Hand.class));
    }

    @Test
    void whiteWinsFromPairRankValueAfterFirstSetIsEqualRanking() {
      String[] arrBlack = new String[] {"2C", "2H", "5C", "7D", "9S"};
      String[] arrWhite = new String[] {"2D", "2S", "5D", "QH", "TS"};

      String expectedValue =
          "PLAYER [WHITE] WINS! REASON = [PAIR] value of [27] over [PAIR] value of [21]";

      String actualValue = POKER_GAME.play(arrBlack, arrWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
      verify(POKER_GAME).handlePairs(any(Hand.class), any(Hand.class));
    }
  }

  @Nested
  class HandleTwoPairsTest {

    @Test
    void blackWinsFromFirstCheck() {
      String[] arrBlack = new String[] {"2H", "2S", "4D", "4H", "AH"};
      String[] arrWhite = new String[] {"2C", "2D", "3S", "3C", "AD"};

      String expectedValue =
          "PLAYER [BLACK] WINS! REASON = [TWO_PAIRS] value of [8] over [TWO_PAIRS] value of [6]";

      String actualValue = POKER_GAME.play(arrBlack, arrWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
      verify(POKER_GAME, times(2)).getTotalValueFromCollection(anyCollection());
      verify(POKER_GAME)
          .setWinnerBasedOnValues(eq(Rank.TWO_PAIRS), anyInt(), eq(Rank.TWO_PAIRS), anyInt());
    }

    @Test
    void whiteWinsFromFirstCheck() {
      String[] arrWhite = new String[] {"3C", "3D", "5D", "5H", "AH"};
      String[] arrBlack = new String[] {"2C", "2D", "4S", "4C", "AD"};

      String expectedValue =
          "PLAYER [WHITE] WINS! REASON = [TWO_PAIRS] value of [10] over [TWO_PAIRS] value of [8]";

      String actualValue = POKER_GAME.play(arrBlack, arrWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
      verify(POKER_GAME, times(2)).getTotalValueFromCollection(anyCollection());
      verify(POKER_GAME)
          .setWinnerBasedOnValues(eq(Rank.TWO_PAIRS), anyInt(), eq(Rank.TWO_PAIRS), anyInt());
    }

    @Test
    void blackWinsFromSecondCheck() {
      String[] arrBlack = new String[] {"3C", "3D", "4D", "4H", "AH"};
      String[] arrWhite = new String[] {"2C", "2D", "4S", "4C", "AD"};

      String expectedValue =
          "PLAYER [BLACK] WINS! REASON = [TWO_PAIRS] value of [6] over [TWO_PAIRS] value of [4]";

      String actualValue = POKER_GAME.play(arrBlack, arrWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
      verify(POKER_GAME, times(4)).getTotalValueFromCollection(anyCollection());
      verify(POKER_GAME)
          .setWinnerBasedOnValues(eq(Rank.TWO_PAIRS), anyInt(), eq(Rank.TWO_PAIRS), anyInt());
    }

    @Test
    void whiteWinsFromSecondCheck() {
      String[] arrBlack = new String[] {"2C", "2D", "4S", "4C", "AD"};
      String[] arrWhite = new String[] {"3C", "3D", "4D", "4H", "AH"};

      String expectedValue =
          "PLAYER [WHITE] WINS! REASON = [TWO_PAIRS] value of [6] over [TWO_PAIRS] value of [4]";

      String actualValue = POKER_GAME.play(arrBlack, arrWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
      verify(POKER_GAME, times(4)).getTotalValueFromCollection(anyCollection());
      verify(POKER_GAME)
          .setWinnerBasedOnValues(eq(Rank.TWO_PAIRS), anyInt(), eq(Rank.TWO_PAIRS), anyInt());
    }

    @Test
    void blackWinsFromLastCard() {
      String[] arrBlack = new String[] {"3H", "3S", "4S", "4C", "AD"};
      String[] arrWhite = new String[] {"3C", "3D", "4D", "4H", "KH"};

      String expectedValue =
          "PLAYER [BLACK] WINS! REASON = BLACK wins with [ACE of DIAMONDS] over [KING of HEARTS]";

      String actualValue = POKER_GAME.play(arrBlack, arrWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
      verify(POKER_GAME, times(4)).getTotalValueFromCollection(anyCollection());
      verify(POKER_GAME).setWinnerBasedOnHighCards(any(Card.class), any(Card.class));
      verify(POKER_GAME, never())
          .setWinnerBasedOnValues(eq(Rank.TWO_PAIRS), anyInt(), eq(Rank.TWO_PAIRS), anyInt());
    }

    @Test
    void whiteWinsFromLastCard() {
      String[] arrBlack = new String[] {"3C", "3D", "4D", "4H", "KH"};
      String[] arrWhite = new String[] {"3H", "3S", "4S", "4C", "AD"};

      String expectedValue =
          "PLAYER [WHITE] WINS! REASON = WHITE wins with [ACE of DIAMONDS] over [KING of HEARTS]";

      String actualValue = POKER_GAME.play(arrBlack, arrWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
      verify(POKER_GAME, times(4)).getTotalValueFromCollection(anyCollection());
      verify(POKER_GAME).setWinnerBasedOnHighCards(any(Card.class), any(Card.class));
      verify(POKER_GAME, never())
          .setWinnerBasedOnValues(eq(Rank.TWO_PAIRS), anyInt(), eq(Rank.TWO_PAIRS), anyInt());
    }
  }

  @Nested
  class HighCardPlayTests {

    @Test
    void blackWinsWhenBothPlayersHaveStraightFlushes() {
      String[] arrBlack = new String[] {"7C", "8C", "9C", "TC", "JC"};
      String[] arrWhite = new String[] {"2C", "3C", "4C", "5C", "6C"};

      String expectedValue = "PLAYER [BLACK] WINS! REASON = HIGH CARD: [JACK]";

      String actualValue = POKER_GAME.play(arrBlack, arrWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
      verify(POKER_GAME, never())
          .determineAndSetWinnerBasedOnCardsValues(
              eq(Rank.STRAIGHT_FLUSH), anyList(), eq(Rank.STRAIGHT_FLUSH), anyList());
    }

    @Test
    void whiteWinsWhenBothPlayersHaveStraightFlushes() {
      String[] arrBlack = new String[] {"2C", "3C", "4C", "5C", "6C"};
      String[] arrWhite = new String[] {"7C", "8C", "9C", "TC", "JC"};

      String expectedValue = "PLAYER [WHITE] WINS! REASON = HIGH CARD: [JACK]";

      String actualValue = POKER_GAME.play(arrBlack, arrWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
      verify(POKER_GAME, never())
          .determineAndSetWinnerBasedOnCardsValues(
              eq(Rank.STRAIGHT_FLUSH), anyList(), eq(Rank.STRAIGHT_FLUSH), anyList());
    }

    @Test
    void blackWinsWhenBothPlayersHaveFlushes() {
      String[] arrBlack = new String[] {"3C", "5C", "7C", "9C", "QC"};
      String[] arrWhite = new String[] {"2D", "3D", "4D", "5D", "9D"};

      String expectedValue = "PLAYER [BLACK] WINS! REASON = HIGH CARD: [QUEEN]";

      String actualValue = POKER_GAME.play(arrBlack, arrWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
      verify(POKER_GAME, never())
          .determineAndSetWinnerBasedOnCardsValues(
              eq(Rank.FLUSH), anyList(), eq(Rank.FLUSH), anyList());
    }

    @Test
    void whiteWinsWhenBothPlayersHaveFlushes() {
      String[] arrBlack = new String[] {"2C", "3C", "4C", "5C", "9C"};
      String[] arrWhite = new String[] {"QC", "6C", "7C", "8C", "TC"};

      String expectedValue = "PLAYER [WHITE] WINS! REASON = HIGH CARD: [QUEEN]";

      String actualValue = POKER_GAME.play(arrBlack, arrWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
      verify(POKER_GAME, never())
          .determineAndSetWinnerBasedOnCardsValues(
              eq(Rank.FLUSH), anyList(), eq(Rank.FLUSH), anyList());
    }

    @Test
    void blackWinsWhenBothPlayersHaveStraights() {
      String[] arrBlack = new String[] {"TC", "JD", "QS", "KC", "AH"};
      String[] arrWhite = new String[] {"2C", "3D", "4S", "5C", "6H"};

      String expectedValue = "PLAYER [BLACK] WINS! REASON = HIGH CARD: [ACE]";

      String actualValue = POKER_GAME.play(arrBlack, arrWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
      verify(POKER_GAME, never())
          .determineAndSetWinnerBasedOnCardsValues(
              eq(Rank.STRAIGHT), anyList(), eq(Rank.STRAIGHT), anyList());
    }

    @Test
    void whiteWinsWhenBothPlayersHaveStraights() {
      String[] arrBlack = new String[] {"2C", "3D", "4H", "5S", "9C"};
      String[] arrWhite = new String[] {"5C", "6H", "7S", "8H", "TC"};

      String expectedValue = "PLAYER [WHITE] WINS! REASON = HIGH CARD: [TEN]";

      String actualValue = POKER_GAME.play(arrBlack, arrWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
      verify(POKER_GAME, never())
          .determineAndSetWinnerBasedOnCardsValues(
              eq(Rank.STRAIGHT), anyList(), eq(Rank.STRAIGHT), anyList());
    }

    @Test
    void tieBreakerWhenBothPlayersHaveStraights() {
      String[] arrBlack = new String[] {"2C", "3D", "7H", "8S", "TD"};
      String[] arrWhite = new String[] {"2S", "3H", "7S", "8H", "TC"};

      String expectedValue = "TIE";

      String actualValue = POKER_GAME.play(arrBlack, arrWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
      verify(POKER_GAME).setWinnerBasedOnHighCardHandComparison(any(Hand.class), any(Hand.class));
      verify(POKER_GAME, never()).setWinnerFromBooleanWithWinCondition(anyBoolean(), anyString());
      verify(POKER_GAME, never())
          .determineAndSetWinnerBasedOnCardsValues(
              eq(Rank.STRAIGHT), anyList(), eq(Rank.STRAIGHT), anyList());
    }
  }

  @Nested
  class FullHouseTests {

    @Test
    void blackWins() {
      String[] arrBlack = new String[] {"3C", "3D", "3S", "5C", "5D"};
      String[] arrWhite = new String[] {"2C", "2D", "2S", "5H", "5S"};

      String expectedValue =
          "PLAYER [BLACK] WINS! REASON = [FULL_HOUSE] value of [9] over [FULL_HOUSE] value of [6]";

      String actualValue = POKER_GAME.play(arrBlack, arrWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
      verify(POKER_GAME)
          .determineAndSetWinnerBasedOnCardsValues(
              eq(Rank.FULL_HOUSE), anyList(), eq(Rank.FULL_HOUSE), anyList());
    }

    @Test
    void whiteWins() {
      String[] arrBlack = new String[] {"2C", "2D", "2S", "5H", "5S"};
      String[] arrWhite = new String[] {"3C", "3D", "3S", "5C", "5D"};

      String expectedValue =
          "PLAYER [WHITE] WINS! REASON = [FULL_HOUSE] value of [9] over [FULL_HOUSE] value of [6]";

      String actualValue = POKER_GAME.play(arrBlack, arrWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
      verify(POKER_GAME)
          .determineAndSetWinnerBasedOnCardsValues(
              eq(Rank.FULL_HOUSE), anyList(), eq(Rank.FULL_HOUSE), anyList());
    }
  }

  @Nested
  class FourOfAKindTests {

    @Test
    void blackWins() {
      String[] arrBlack = new String[] {"5C", "5D", "5S", "5H", "AD"};
      String[] arrWhite = new String[] {"4S", "4D", "4H", "4C", "AC"};

      String expectedValue =
          "PLAYER [BLACK] WINS! REASON = [FOUR_OF_A_KIND] value of [20] over [FOUR_OF_A_KIND] value of [16]";

      String actualValue = POKER_GAME.play(arrBlack, arrWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
      verify(POKER_GAME)
          .determineAndSetWinnerBasedOnCardsValues(
              eq(Rank.FOUR_OF_A_KIND), anyList(), eq(Rank.FOUR_OF_A_KIND), anyList());
    }

    @Test
    void whiteWins() {
      String[] arrBlack = new String[] {"4S", "4D", "4H", "4C", "AC"};
      String[] arrWhite = new String[] {"5C", "5D", "5S", "5H", "AD"};

      String expectedValue =
          "PLAYER [WHITE] WINS! REASON = [FOUR_OF_A_KIND] value of [20] over [FOUR_OF_A_KIND] value of [16]";

      String actualValue = POKER_GAME.play(arrBlack, arrWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
      verify(POKER_GAME)
          .determineAndSetWinnerBasedOnCardsValues(
              eq(Rank.FOUR_OF_A_KIND), anyList(), eq(Rank.FOUR_OF_A_KIND), anyList());
    }
  }

  @Nested
  class ThreeOfAKindTests {

    @Test
    void blackWins() {
      String[] arrBlack = new String[] {"5C", "5D", "5S", "KC", "AD"};
      String[] arrWhite = new String[] {"2S", "2D", "2H", "KD", "AC"};

      String expectedValue =
          "PLAYER [BLACK] WINS! REASON = [THREE_OF_A_KIND] value of [15] over [THREE_OF_A_KIND] value of [6]";

      String actualValue = POKER_GAME.play(arrBlack, arrWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
      verify(POKER_GAME)
          .determineAndSetWinnerBasedOnCardsValues(
              eq(Rank.THREE_OF_A_KIND), anyList(), eq(Rank.THREE_OF_A_KIND), anyList());
    }

    @Test
    void whiteWins() {
      String[] arrBlack = new String[] {"2C", "2D", "2S", "7H", "8S"};
      String[] arrWhite = new String[] {"3C", "3D", "3S", "2H", "5D"};

      String expectedValue =
          "PLAYER [WHITE] WINS! REASON = [THREE_OF_A_KIND] value of [9] over [THREE_OF_A_KIND] value of [6]";

      String actualValue = POKER_GAME.play(arrBlack, arrWhite);

      assertThat(actualValue).isEqualTo(expectedValue);
      verify(POKER_GAME)
          .determineAndSetWinnerBasedOnCardsValues(
              eq(Rank.THREE_OF_A_KIND), anyList(), eq(Rank.THREE_OF_A_KIND), anyList());
    }
  }
}
