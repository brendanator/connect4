package gameplay;

public enum Difficulty {
  EASY,
  MEDIUM,
  HARD,
  BRUTAL;

  public Difficulty getNext() {
    return values()[(ordinal() + 1) % values().length];
  }

  public int getDepth() { return 2 + ordinal() * 2; }
}
