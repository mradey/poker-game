package com.cas;

public enum Suit {
	C("CLUBS"),
	D("DIAMONDS"),
	H("HEARTS"),
	S("SPADES");

	private final String suit;

	Suit(String suit) {
		this.suit = suit.toUpperCase();
	}

}
