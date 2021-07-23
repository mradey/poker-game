package com.cas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.cas.Rank.*;

public class PokerGame {
  public String play(final String[] strArrBlackHand, final String[] strArrWhiteHand) {
    PokerUtils.validateCardsInput(strArrBlackHand, strArrWhiteHand);

    final Hand blackHand = Hand.newHand(Arrays.asList(strArrBlackHand));
    final Hand whiteHand = Hand.newHand(Arrays.asList(strArrWhiteHand));

    int compare = blackHand.getHandRank().compareTo(whiteHand.getHandRank());
    if(compare > 0) {
      return "white wins: " + whiteHand.getHandRank().toString();
    }
    else if(compare < 0) {
      return "black wins: " + blackHand.getHandRank().toString();
    }
    return "Tie";
  
  }

  public static void main(String[] args) { 
    PokerGame pokerGame = new PokerGame();
    String res = pokerGame.play(new String[]{"2H", "3D", "5S", "9C", "KD"}, new String[]{"2C", "3H", "4S", "8C", "AH"});
    System.out.println(res);

    res = pokerGame.play(new String[]{"2H", "4S", "4C", "2D", "4H"}, new String[]{"2S", "8S", "AS", "QS", "3S"});
    System.out.println(res);

    res = pokerGame.play(new String[]{"2H", "3D", "5S", "9C", "KD"}, new String[]{"2C", "3H", "4S", "8C", "KH"});
    System.out.println(res);

    res = pokerGame.play(new String[]{"2H", "3D", "5S", "9C", "KD"}, new String[]{"2C", "3H", "5C", "9S", "KH"});
    System.out.println(res);

  }
}
