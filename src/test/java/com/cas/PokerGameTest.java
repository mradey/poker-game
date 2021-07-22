package com.cas;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PokerGameTest {

	private final PokerGame pokerGame = new PokerGame();

	@Nested
	class SampleInputCases {

		@Test
		void whiteWinsWithHighCardAce() {
			// Black: 2H 3D 5S 9C KD, 32 if added together
			// White: 2C 3H 4S 8C AH, 31 if added together

			String[] inputBlack = new String[]{"2H", "3D", "5S", "9C", "KD"};
			String[] inputWhite = new String[]{"2C", "3H", "4S", "8C", "AH"};

			String expectedValue = "PLAYER [WHITE] WINS! REASON = [HIGH CARD: ACE]";

			String actualValue = pokerGame.play(inputBlack, inputWhite);

			assertThat(actualValue).isEqualTo(expectedValue);
		}

		@Test
		void blackWinsWithFullHouseFourOverTwo() {
			// Black: 2H 4S 4C 2D 4H  White: 2S 8S AS QS 3S

			String[] inputBlack = new String[]{"2H", "4S", "4C", "2D", "4H"};
			String[] inputWhite = new String[]{"2S", "3S", "8S", "QS", "AS"};

			String expectedValue = "PLAYER [BLACK] WINS! REASON = [FULL_HOUSE OVER FLUSH]";

			String actualValue = pokerGame.play(inputBlack, inputWhite);

			assertThat(actualValue).isEqualTo(expectedValue);
		}

		@Test
		void blackWinsWithHighCardNine() {
			String[] inputBlack = new String[]{"2H", "3D", "5S", "9C", "KD"};
			String[] inputWhite = new String[]{"2C", "3H", "4S", "8C", "KH"};

			String expectedValue = "PLAYER [BLACK] WINS! REASON = [HIGH CARD: NINE]";

			String actualValue = pokerGame.play(inputBlack, inputWhite);

			assertThat(actualValue).isEqualTo(expectedValue);
		}

		@DisplayName("No winning player as no player has a greater high card value than the other")
		@Test
		void tieBreaker() {
			String[] inputBlack = new String[]{"2H", "3D", "5S", "9C", "KD"};
			String[] inputWhite = new String[]{"2D", "3H", "5C", "9S", "KH"};

			String expectedValue = "TIE";

			String actualValue = pokerGame.play(inputBlack, inputWhite);

			assertThat(actualValue).isEqualTo(expectedValue);
		}

	}


	@Nested
	class PlayTesting {

		@Test
		void blackWinsWhenBothPlayersHaveStraightFlushes() {
			String[] arrBlack = new String[]{"3C", "4C", "5C", "6C", "7C"};
			String[] arrWhite = new String[]{"2C", "3C", "4C", "5C", "6C"};

			String expectedValue = "PLAYER [BLACK] WINS! REASON = [HIGH CARD: SEVEN]";

			String actualValue = pokerGame.play(arrBlack, arrWhite);

			assertThat(actualValue).isEqualTo(expectedValue);
		}

		@Test
		void whiteWinsWhenBothPlayersHaveStraightFlushes() {
			String[] arrBlack = new String[]{"2C", "3C", "4C", "5C", "6C"};
			String[] arrWhite = new String[]{"3C", "4C", "5C", "6C", "7C"};

			String expectedValue = "PLAYER [WHITE] WINS! REASON = [HIGH CARD: SEVEN]";

			String actualValue = pokerGame.play(arrBlack, arrWhite);

			assertThat(actualValue).isEqualTo(expectedValue);
		}

		@Test
		void blackWinsWhenBothPlayersHaveFullHouses() {
			String[] arrBlack = new String[]{"3C", "3D", "3S", "5C", "5D"};
			String[] arrWhite = new String[]{"2C", "2D", "2S", "5C", "5D"};

			String expectedValue = "PLAYER [BLACK] WINS! REASON = [FULL_HOUSE]";

			String actualValue = pokerGame.play(arrBlack, arrWhite);

			assertThat(actualValue).isEqualTo(expectedValue);
		}

		@Test
		void whiteWinsWhenBothPlayersHaveFullHouses() {
			String[] arrBlack = new String[]{"2C", "2D", "2S", "5C", "5D"};
			String[] arrWhite = new String[]{"3C", "3D", "3S", "5C", "5D"};

			String expectedValue = "PLAYER [WHITE] WINS! REASON = [FULL_HOUSE]";

			String actualValue = pokerGame.play(arrBlack, arrWhite);

			assertThat(actualValue).isEqualTo(expectedValue);
		}

		@Test
		void blackWinsWhenBothPlayersHaveFlushes() {
			String[] arrBlack = new String[]{"3C", "5C", "7C", "9C", "QC"};
			String[] arrWhite = new String[]{"2C", "3C", "4C", "5C", "9C"};

			String expectedValue = "PLAYER [BLACK] WINS! REASON = [HIGH CARD: QUEEN]";

			String actualValue = pokerGame.play(arrBlack, arrWhite);

			assertThat(actualValue).isEqualTo(expectedValue);
		}

		@Test
		void whiteWinsWhenBothPlayersHaveFlushes() {
			String[] arrBlack = new String[]{"2C", "3C", "4C", "5C", "9C"};
			String[] arrWhite = new String[]{"5C", "6C", "7C", "8C", "TC"};

			String expectedValue = "PLAYER [WHITE] WINS! REASON = [HIGH CARD: TEN]";

			String actualValue = pokerGame.play(arrBlack, arrWhite);

			assertThat(actualValue).isEqualTo(expectedValue);
		}

		@Test
		void blackWinsWhenBothPlayersHaveStraights() {
			String[] arrBlack = new String[]{"TC", "JD", "QS", "KC", "AH"};
			String[] arrWhite = new String[]{"2C", "3D", "4S", "5C", "6H"};

			String expectedValue = "PLAYER [BLACK] WINS! REASON = [HIGH CARD: ACE]";

			String actualValue = pokerGame.play(arrBlack, arrWhite);

			assertThat(actualValue).isEqualTo(expectedValue);
		}

		@Test
		void whiteWinsWhenBothPlayersHaveStraights() {
			String[] arrBlack = new String[]{"2C", "3D", "4H", "5S", "9C"};
			String[] arrWhite = new String[]{"5C", "6H", "7S", "8H", "TC"};

			String expectedValue = "PLAYER [WHITE] WINS! REASON = [HIGH CARD: TEN]";

			String actualValue = pokerGame.play(arrBlack, arrWhite);

			assertThat(actualValue).isEqualTo(expectedValue);
		}


	}

}