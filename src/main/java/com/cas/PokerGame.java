package com.cas;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.cas.Rank.*;

/**
 * Each card has a suit which is one of:
 * (C)lubs,
 * (D)iamonds,
 * (H)earts,
 * (S)pades
 * <p>
 * Each card also has a value which is one of:
 * 2, 3, 4, 5, 6, 7, 8, 9, 10, jack, queen, king, ace
 * (denoted 2, 3, 4, 5, 6, 7, 8, 9, T, J, Q, K, A)
 * <p>
 * For scoring purposes, the suits are unordered while the values are ordered as given above,
 * with 2 being the lowest and ace the highest value
 * <p>
 * T = 10
 * J = 11
 * Q = 12
 * K = 13
 * A = 14
 */
public class PokerGame {

	private static final Set<Rank> RANK_VALUE_SET_FOR_HIGH_CARDS = Set.of(
			STRAIGHT_FLUSH, FLUSH, STRAIGHT, HIGH_CARD
	);

	private String playerWhoWon = null;
	private String winningCondition = "";

	public String play(final String[] strArrBlackHand,
										 final String[] strArrWhiteHand) {

		final Hand blackHand = Hand.newHand(strArrBlackHand);
		final Hand whiteHand = Hand.newHand(strArrWhiteHand);

		List<Card> blackHandCardsToCompare = new ArrayList<>();
		List<Card> whiteHandCardsToCompare = new ArrayList<>();

		if (doPlayersHaveMatchingRankValues(blackHand, whiteHand)) {
			final Rank matchingRank = blackHand.getHighestRankedValue();

			if (RANK_VALUE_SET_FOR_HIGH_CARDS.contains(matchingRank))
				setWinnerBasedOnHighCards(blackHand, whiteHand);
			else if (matchingRank == FOUR_OF_A_KIND) {
				blackHandCardsToCompare = blackHand.getFourOfAKindCards();
				whiteHandCardsToCompare = whiteHand.getFourOfAKindCards();
			} else if (matchingRank == FULL_HOUSE) {
				blackHandCardsToCompare = blackHand.getFullHouseCards();
				whiteHandCardsToCompare = whiteHand.getFullHouseCards();
			} else if (matchingRank == THREE_OF_A_KIND) {
				blackHandCardsToCompare = blackHand.getThreeOfAKindCards();
				whiteHandCardsToCompare = whiteHand.getThreeOfAKindCards();
			} else if (matchingRank == TWO_PAIRS)
				handleTwoPairs(blackHand, whiteHand);
			else if (matchingRank == PAIR)
				handlePairs(blackHand, whiteHand);
			else
				throw new RuntimeException(String.format(
						"Should not be possible to not have an assigned rank! Black hand rank [%s] White hand rank [%s]",
						matchingRank, whiteHand.getHighestRankedValue()
				));

			// if rank exists in RANK_VALUE_SET_FOR_HIGH_CARDS list, both lists will still be empty
			if (!blackHandCardsToCompare.isEmpty() && !whiteHandCardsToCompare.isEmpty()) {
				determineAndSetWinnerBasedOnCardsValues(
						blackHand.getHighestRankedValue(), blackHandCardsToCompare,
						whiteHand.getHighestRankedValue(), whiteHandCardsToCompare
				);
			}

		} else {
			// if players do not have matching ranks
			setWinnerBasedOnRank(blackHand.getHighestRankedValue(), whiteHand.getHighestRankedValue());
		}

		return formatAndPrintWinner();
	}

	protected String formatAndPrintWinner() {
		if (null != playerWhoWon)
			return String.format("PLAYER [%s] WINS! REASON = [%s]", playerWhoWon, this.winningCondition);

		return "TIE";
	}

	/**
	 * 2 of the 5 cards in the hand have the same value.
	 * Hands which both contain a pair are ranked by the value of the cards forming the pair.
	 * If these values are the same,
	 * the hands are ranked by the values of the cards not forming the pair, in decreasing order.
	 * <p>
	 * "...in decreasing order" is a bit ambiguous as my interpretation of it is either:
	 * 1. check the next highest cards and compare those until one card is greater or,
	 * 2. get the total value of the remaining cards and compare those
	 * <p>
	 * I have implemented the latter method.
	 */
	private void handlePairs(final Hand blackHand,
													 final Hand whiteHand) {
		final List<Card> blackHandPair = blackHand.getPairCards();
		final List<Card> whiteHandPair = whiteHand.getPairCards();

		int blackHandPairRankValue = this.getRankValueSumFromCollectionOfCards(blackHandPair);
		int whiteHandPairRankValue = this.getRankValueSumFromCollectionOfCards(whiteHandPair);

		if (blackHandPairRankValue == whiteHandPairRankValue) {
			final List<Card> blackHandOtherThreeCards = new ArrayList<>(blackHand.getCards());
			blackHandOtherThreeCards.removeAll(blackHandPair);

			final List<Card> whiteHandOtherThreeCards = new ArrayList<>(whiteHand.getCards());
			whiteHandOtherThreeCards.removeAll(whiteHandPair);

			blackHandPairRankValue = this.getRankValueSumFromCollectionOfCards(blackHandOtherThreeCards);
			whiteHandPairRankValue = this.getRankValueSumFromCollectionOfCards(whiteHandOtherThreeCards);
		}

		setWinnerFromBoolean(blackHandPairRankValue > whiteHandPairRankValue);
	}

	/**
	 * The hand contains 2 different pairs.
	 * Hands which both contain 2 pairs are ranked by the value of their highest pair.
	 * Hands with the same highest pair are ranked by the value of their other pair.
	 * If these values are the same the hands are ranked by the value of the remaining card.
	 * <p>
	 * todo: difficult to read, need to refactor into smaller methods?
	 */
	private void handleTwoPairs(final Hand blackHand,
															final Hand whiteHand) {
		final List<List<Card>> blackHandTwoPairsList = blackHand.getTwoPairsCards();
		final int blackHandFirstPairValue = this.getRankValueSumFromCollectionOfCards(blackHandTwoPairsList.get(0));
		final int blackHandSecondPairValue = this.getRankValueSumFromCollectionOfCards(blackHandTwoPairsList.get(1));

		// get highest rank value of the pairs
		int blackHandRankValue = Math.max(blackHandFirstPairValue, blackHandSecondPairValue);
		// remove the list of cards that is greater than the other pair
		if (blackHandRankValue == blackHandSecondPairValue) blackHandTwoPairsList.remove(1);
		else blackHandTwoPairsList.remove(0);

		final List<List<Card>> whiteHandTwoPairsList = whiteHand.getTwoPairsCards();
		final int whiteHandFirstPairValue = this.getRankValueSumFromCollectionOfCards(whiteHandTwoPairsList.get(0));
		final int whiteHandSecondPairValue = this.getRankValueSumFromCollectionOfCards(whiteHandTwoPairsList.get(1));

		// get highest rank value of the pairs
		int whiteHandRankValue = Math.max(whiteHandFirstPairValue, whiteHandSecondPairValue);
		// remove the list of cards that is greater than the other pair
		if (whiteHandRankValue == whiteHandSecondPairValue) whiteHandTwoPairsList.remove(1);
		else whiteHandTwoPairsList.remove(0);

		// if highest pairs match, get the other pair
		if (blackHandRankValue == whiteHandRankValue) {
			blackHandRankValue = this.getRankValueSumFromCollectionOfCards(blackHandTwoPairsList.get(0));
			whiteHandRankValue = this.getRankValueSumFromCollectionOfCards(whiteHandTwoPairsList.get(0));

			// if the other pairs match, get the value of the last remaining card
			if (blackHandRankValue == whiteHandRankValue) {
				blackHandRankValue = blackHand.getCardsForExpectedSize(1).get(0).getValueAsInt();
				whiteHandRankValue = whiteHand.getCardsForExpectedSize(1).get(0).getValueAsInt();
			}
		}

		setWinnerFromBoolean(blackHandRankValue > whiteHandRankValue);
	}


	// todo: I feel like there should be a cleaner way to implement setting the winning condition and the playerWhoWon,
	// 	but I'm not seeing it right now.
	// 	The method could also return the winner, not sure if necessary however
	public void setWinnerBasedOnValues(final Rank blackRank,
																		 final int blackHandValue,
																		 final Rank whiteRank,
																		 final int whiteHandValue) {
		System.out.printf("Black hand value [%s] White hand value [%s]%n", blackHandValue, whiteHandValue);
		boolean doesBlackWin = blackHandValue > whiteHandValue;
		setWinnerFromBooleanWithWinCondition(doesBlackWin, String.valueOf((doesBlackWin ? blackRank : whiteRank)));
	}

	// todo: I feel like there should be a cleaner way to implement setting the winning condition and the playerWhoWon,
	// 	but I'm not seeing it right now.
	// 	The method could also return the winner, not sure if necessary however
	public void setWinnerBasedOnRank(final Rank blackHandRank,
																	 final Rank whiteHandRank) {
		System.out.printf("Black hand rank [%s] White hand rank [%s]%n", blackHandRank, whiteHandRank);
		boolean doesBlackWin = blackHandRank.getValue() > whiteHandRank.getValue();
		final String winMsg = doesBlackWin
				? blackHandRank + " OVER " + whiteHandRank
				: whiteHandRank + " OVER " + blackHandRank;
		setWinnerFromBooleanWithWinCondition(doesBlackWin, winMsg);
	}

	public void setWinnerBasedOnHighCards(final Hand blackHand,
																				final Hand whiteHand) {
		final Card winningCard = compareAndGetHighCardFromHands(blackHand, whiteHand);
		if (null != winningCard) {
			setWinnerFromBooleanWithWinCondition(
					blackHand.getCards().contains(winningCard),
					"HIGH CARD: " + winningCard.getFullNameFromValue()
			);
		}
	}

	protected void setWinnerFromBoolean(final boolean doesBlackWin) {
		setWinnerFromBooleanWithWinCondition(doesBlackWin, "");
	}

	protected void setWinnerFromBooleanWithWinCondition(final boolean doesBlackWin,
																											final String winningCondition) {
		if (doesBlackWin) playerWhoWon = "BLACK";
		else playerWhoWon = "WHITE";
		this.winningCondition = winningCondition;
	}

	protected void determineAndSetWinnerBasedOnCardsValues(final Rank blackRank,
																												 final Collection<Card> blackCards,
																												 final Rank whiteRank,
																												 final Collection<Card> whiteCards) {
		final int blackValueOfThree = this.getRankValueSumFromCollectionOfCards(blackCards);
		final int whiteValueOfThree = this.getRankValueSumFromCollectionOfCards(whiteCards);
		setWinnerBasedOnValues(blackRank, blackValueOfThree, whiteRank, whiteValueOfThree);
	}

	public boolean doPlayersHaveMatchingRankValues(final Hand hand1,
																								 final Hand hand2) {
		return hand1.getHighestRankedValue() == hand2.getHighestRankedValue();
	}

	public Card compareAndGetHighCardFromHands(final Hand blackHand,
																						 final Hand whiteHand) {
		final List<Card> blackHandCardList = new ArrayList<>(blackHand.getCards());
		Card blackHighCard = blackHand.getHighCard();

		final List<Card> whiteHandCardList = new ArrayList<>(whiteHand.getCards());
		Card whiteHighCard = whiteHand.getHighCard();

		Card winningHighCard = null;

		// iterate up to the max possible size of a hand  
		for (int i = 1; i < 5; i++) {
			int compareValue = blackHighCard.compareTo(whiteHighCard);
			System.out.printf(
					"--> Black high card [%s] White high card [%s] Compare Value [%s]%n",
					blackHighCard.getFullNameFromValue(), whiteHighCard.getFullNameFromValue(), compareValue
			);
			if (compareValue != 0) {
				if (compareValue > 0) winningHighCard = blackHighCard;
				else winningHighCard = whiteHighCard;
				break;
			} else {
				blackHighCard = blackHand.getNextHighestCard(blackHandCardList);
				whiteHighCard = whiteHand.getNextHighestCard(whiteHandCardList);
			}
		}

		return winningHighCard;
	}

	// todo: I feel like I call this method a lot unnecessarily. Not seeing a better option at the moment though.
	public final Integer getRankValueSumFromCollectionOfCards(final Collection<Card> cards) {
		return cards.stream().mapToInt(Card::getValueAsInt).sum();
	}

}
