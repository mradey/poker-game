package com.cas;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.cas.Rank.*;

/**
 * Each card has a suit which is one of: (C)lubs, (D)iamonds, (H)earts, (S)pades
 *
 * <p>Each card also has a value which is one of: 2, 3, 4, 5, 6, 7, 8, 9, 10, jack, queen, king, ace
 * (denoted 2, 3, 4, 5, 6, 7, 8, 9, T, J, Q, K, A)
 *
 * <p>For scoring purposes, the suits are unordered while the values are ordered as given above,
 * with 2 being the lowest and ace the highest value
 *
 * <p>T = 10 J = 11 Q = 12 K = 13 A = 14
 */
@Getter
public class PokerGame {

  private static final Set<Rank> RANK_VALUES_THAT_USE_HIGH_CARD_TO_DETERMINE_WINNER =
      Set.of(STRAIGHT_FLUSH, FLUSH, STRAIGHT, HIGH_CARD);

  private String playerWhoWon = null;
  private String winningCondition = "";

  public String play(final String[] strArrBlackHand, final String[] strArrWhiteHand) {
    PokerUtils.validateCardsInput(strArrBlackHand, strArrWhiteHand);

    final Hand blackHand = Hand.newHand(strArrBlackHand);
    final Hand whiteHand = Hand.newHand(strArrWhiteHand);

    List<Card> blackHandCardsToCompare = new ArrayList<>();
    List<Card> whiteHandCardsToCompare = new ArrayList<>();

    if (doPlayersHaveMatchingRanks(blackHand, whiteHand)) {
      final Rank matchingRank = blackHand.getHighestRank();

      if (RANK_VALUES_THAT_USE_HIGH_CARD_TO_DETERMINE_WINNER.contains(matchingRank))
        setWinnerBasedOnHighCardHandComparison(blackHand, whiteHand);
      else if (matchingRank == FOUR_OF_A_KIND) {
        blackHandCardsToCompare = blackHand.getFourOfAKindCards();
        whiteHandCardsToCompare = whiteHand.getFourOfAKindCards();
      } else if (matchingRank == FULL_HOUSE) {
        blackHandCardsToCompare = blackHand.getFullHouseCards();
        whiteHandCardsToCompare = whiteHand.getFullHouseCards();
      } else if (matchingRank == THREE_OF_A_KIND) {
        blackHandCardsToCompare = blackHand.getThreeOfAKindCards();
        whiteHandCardsToCompare = whiteHand.getThreeOfAKindCards();
      } else if (matchingRank == TWO_PAIRS) handleTwoPairs(blackHand, whiteHand);
      else if (matchingRank == PAIR) handlePairs(blackHand, whiteHand);

      // if rank exists in RANK_VALUE_SET_FOR_HIGH_CARDS list, both lists will still be empty
      if (!blackHandCardsToCompare.isEmpty() && !whiteHandCardsToCompare.isEmpty()) {
        determineAndSetWinnerBasedOnCardsValues(
            blackHand.getHighestRank(), blackHandCardsToCompare,
            whiteHand.getHighestRank(), whiteHandCardsToCompare);
      }

    } else {
      // if players do not have matching ranks
      setWinnerBasedOnRank(blackHand.getHighestRank(), whiteHand.getHighestRank());
    }

    return formatAndPrintWinner();
  }

  protected String formatAndPrintWinner() {
    if (null != playerWhoWon)
      return String.format("PLAYER [%s] WINS! REASON = %s", playerWhoWon, this.winningCondition);

    return "TIE";
  }

  /**
   * 2 of the 5 cards in the hand have the same value. Hands which both contain a pair are ranked by
   * the value of the cards forming the pair. If these values are the same, the hands are ranked by
   * the values of the cards not forming the pair, in decreasing order.
   *
   * <p>"...in decreasing order" is a bit ambiguous as my interpretation of it is either: 1. check
   * the next highest cards and compare those until one card is greater or, 2. get the total value
   * of the remaining cards and compare those
   *
   * <p>I have implemented the latter method.
   */
  protected void handlePairs(final Hand blackHand, final Hand whiteHand) {
    final List<Card> blackHandPair = blackHand.getPairCards();
    final List<Card> whiteHandPair = whiteHand.getPairCards();

    int blackHandPairValue = this.getTotalValueFromCollection(blackHandPair);
    int whiteHandPairValue = this.getTotalValueFromCollection(whiteHandPair);

    if (blackHandPairValue == whiteHandPairValue) {
      final List<Card> blackHandOtherThreeCards = new ArrayList<>(blackHand.getCards());
      blackHandOtherThreeCards.removeAll(blackHandPair);

      final List<Card> whiteHandOtherThreeCards = new ArrayList<>(whiteHand.getCards());
      whiteHandOtherThreeCards.removeAll(whiteHandPair);

      blackHandPairValue = this.getTotalValueFromCollection(blackHandOtherThreeCards);
      whiteHandPairValue = this.getTotalValueFromCollection(whiteHandOtherThreeCards);
    }

    setWinnerBasedOnValues(
        blackHand.getHighestRank(),
        blackHandPairValue,
        whiteHand.getHighestRank(),
        whiteHandPairValue);
  }

  /**
   * The hand contains 2 different pairs. Hands which both contain 2 pairs are ranked by the value
   * of their highest pair. Hands with the same highest pair are ranked by the value of their other
   * pair. If these values are the same the hands are ranked by the value of the remaining card.
   *
   * <p>Note: Cards are sorted based on value when instantiated
   */
  protected void handleTwoPairs(final Hand blackHand, final Hand whiteHand) {
    final List<List<Card>> blackHandTwoPairsList = blackHand.getTwoPairsCards();
    int blackHandValue = this.getTotalValueFromCollection(blackHandTwoPairsList.get(1));

    final List<List<Card>> whiteHandTwoPairsList = whiteHand.getTwoPairsCards();
    int whiteHandValue = this.getTotalValueFromCollection(whiteHandTwoPairsList.get(1));

    // if highest pairs match, get the other pair
    if (blackHandValue == whiteHandValue) {
      blackHandValue = this.getTotalValueFromCollection(blackHandTwoPairsList.get(0));
      whiteHandValue = this.getTotalValueFromCollection(whiteHandTwoPairsList.get(0));

      // if the other pairs match, get the value of the last remaining card
      if (blackHandValue == whiteHandValue) {
        setWinnerBasedOnHighCards(
            blackHand.getCardsForExpectedSize(1).get(0),
            whiteHand.getCardsForExpectedSize(1).get(0));
      }
    }

    // in the event the last card is checked, playerWhoWon will not be null
    if (null == playerWhoWon)
      setWinnerBasedOnValues(
          blackHand.getHighestRank(), blackHandValue, whiteHand.getHighestRank(), whiteHandValue);
  }

  /**
   * todo: I feel like there should be a cleaner way to implement setting the winning condition and
   * the playerWhoWon, but I'm not seeing it right now. The method could also return the winner, not
   * sure if a good idea however
   */
  protected void setWinnerBasedOnValues(
      final Rank blackRank,
      final int blackHandValue,
      final Rank whiteRank,
      final int whiteHandValue) {
    System.out.printf(
        "Black hand value [%s] White hand value [%s]%n", blackHandValue, whiteHandValue);
    boolean doesBlackWin = blackHandValue > whiteHandValue;
    boolean doJustCompareRanks = blackHandValue == -1 && whiteHandValue == -1;
    String winMsg = "";
    // todo: there HAS to be a better way to do this...
    if (doJustCompareRanks) {
      if (blackRank.compareTo(whiteRank) > 0) {
        doesBlackWin = true;
        winMsg = String.format("[%s] over [%s]", blackRank, whiteRank);
      } else winMsg = String.format("[%s] over [%s]", whiteRank, blackRank);
    } else {
      if (doesBlackWin)
        winMsg =
            String.format(
                "[%s] value of [%s] over [%s] value of [%s]",
                blackRank, blackHandValue, whiteRank, whiteHandValue);
      else
        winMsg =
            String.format(
                "[%s] value of [%s] over [%s] value of [%s]",
                whiteRank, whiteHandValue, blackRank, blackHandValue);
    }

    setWinnerFromBooleanWithWinCondition(doesBlackWin, winMsg);
  }

  protected void setWinnerBasedOnRank(final Rank blackHandRank, final Rank whiteHandRank) {
    setWinnerBasedOnValues(blackHandRank, -1, whiteHandRank, -1);
  }

  protected void setWinnerBasedOnHighCards(final Card blackHighCard, final Card whiteHighCard) {
    boolean doesBlackWin = blackHighCard.compareTo(whiteHighCard) > 0;
    String winMsg = "";
    if (blackHighCard.compareTo(whiteHighCard) > 0) {
      winMsg = String.format("BLACK wins with %s over %s", blackHighCard, whiteHighCard);
    } else {
      winMsg = String.format("WHITE wins with %s over %s", whiteHighCard, blackHighCard);
    }
    setWinnerFromBooleanWithWinCondition(doesBlackWin, winMsg);
  }

  protected void setWinnerBasedOnHighCardHandComparison(
      final Hand blackHand, final Hand whiteHand) {
    final Card winningCard = compareAndGetHighCardFromHands(blackHand, whiteHand);
    if (null != winningCard) {
      setWinnerFromBooleanWithWinCondition(
          blackHand.getCards().contains(winningCard),
          "HIGH CARD: " + "[" + winningCard.getFullNameFromValue() + "]");
    }
  }

  protected void setWinnerFromBooleanWithWinCondition(
      final boolean doesBlackWin, final String winningCondition) {
    if (doesBlackWin) playerWhoWon = "BLACK";
    else playerWhoWon = "WHITE";
    this.winningCondition = winningCondition;
  }

  protected void determineAndSetWinnerBasedOnCardsValues(
      final Rank blackRank,
      final Collection<Card> blackCards,
      final Rank whiteRank,
      final Collection<Card> whiteCards) {
    final int blackValueOfThree = this.getTotalValueFromCollection(blackCards);
    final int whiteValueOfThree = this.getTotalValueFromCollection(whiteCards);
    setWinnerBasedOnValues(blackRank, blackValueOfThree, whiteRank, whiteValueOfThree);
  }

  public boolean doPlayersHaveMatchingRanks(final Hand blackHand, final Hand whiteHand) {
    return blackHand.getHighestRank() == whiteHand.getHighestRank();
  }

  /**
   * Hands which do not fit any higher category are ranked by the value of their highest card.
   *
   * <p>If the highest cards have the same value, the hands are ranked by the next highest, and so
   * on.
   */
  public Card compareAndGetHighCardFromHands(final Hand blackHand, final Hand whiteHand) {
    final Collection<Card> blackHandCardList = blackHand.getCards();
    final Collection<Card> whiteHandCardList = whiteHand.getCards();

    Card whiteHighCard = whiteHand.getHighCard();
    Card blackHighCard = blackHand.getHighCard();

    Card winningHighCard = null;

    for (int i = 1; i <= 5; i++) {
      int compareValue = blackHighCard.compareTo(whiteHighCard);
      System.out.printf(
          "--> Black high card [%s] White high card [%s] Compare Value [%s]%n",
          blackHighCard.getFullNameFromValue(), whiteHighCard.getFullNameFromValue(), compareValue);
      if (compareValue != 0) {
        if (compareValue > 0) winningHighCard = blackHighCard;
        else winningHighCard = whiteHighCard;
        break;
      } else {
        blackHighCard = this.getNextHighestCard(blackHighCard, blackHandCardList);
        whiteHighCard = this.getNextHighestCard(whiteHighCard, whiteHandCardList);
      }
    }

    return winningHighCard;
  }

  protected Card getHighCardFromCollection(final Collection<Card> cards) {
    return cards.stream().max(Card.COMPARE_CARD_VALUES).orElse(new Card("1", "D"));
  }

  // todo: I feel like I call this method a lot unnecessarily. Not seeing a better option however
  protected int getTotalValueFromCollection(final Collection<Card> cards) {
    return cards.stream().mapToInt(Card::getValueAsInt).sum();
  }

  protected Card getNextHighestCard(final Card currentHighCard, final Collection<Card> cards) {
    final Collection<Card> copiedCards = new ArrayList<>(cards);
    copiedCards.remove(currentHighCard);
    return this.getHighCardFromCollection(copiedCards);
  }
}
