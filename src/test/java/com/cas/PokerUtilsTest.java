package com.cas;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PokerUtilsTest {

  @Test
  void validateCardsInputThrowsException() {
    String[] inputBlack = new String[] {"2H", "3D", "5S", "9C", "KD"};
    String[] inputWhite = new String[] {"2C", "3H", "5S", "8C", "AH"};

    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(() -> PokerUtils.validateCardsInput(inputBlack, inputWhite))
        .withMessage("Card [5S] exists in both hands!");
  }
}
