package com.cas;

import java.util.ArrayList;
import java.util.Collection;

public class PokerUtils {

  public static Card getHighCardFromCollection(final Collection<Card> cards) {
    return cards.stream().max(Card.COMPARE_CARD_VALUES).orElse(new Card("1", "D"));
  }

  // todo: I feel like I call this method a lot unnecessarily. Not seeing a better option however
  public static int getTotalValueFromCollection(final Collection<Card> cards) {
    return cards.stream().mapToInt(Card::getValueAsInt).sum();
  }

  public static void validateCardsInput(
      final String[] strArrBlackHand, final String[] strArrWhiteHand) {
    for (String strBlackCard : strArrBlackHand) {
      for (String strWhiteCard : strArrWhiteHand) {
        if (strBlackCard.equals(strWhiteCard))
          throw new RuntimeException(
              String.format("Card [%s] exists in both hands!", strBlackCard));
      }
    }
  }

  public static Card getNextHighestCard(final Card currentHighCard, final Collection<Card> cards) {
    final Collection<Card> copiedCards = new ArrayList<>(cards);
    copiedCards.remove(currentHighCard);
    return PokerUtils.getHighCardFromCollection(copiedCards);
  }
}
