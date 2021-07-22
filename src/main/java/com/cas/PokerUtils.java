package com.cas;

public class PokerUtils {

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
}
