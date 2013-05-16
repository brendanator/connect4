package gui;

public enum Difficulty {
	EASY,
	MEDIUM,
	HARD,
	BRUTAL;

	public Difficulty getNext() {
		return values()[(ordinal()+1) % values().length];
	}
}
