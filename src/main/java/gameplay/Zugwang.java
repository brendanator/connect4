package gameplay;

public enum Zugwang {
  NONE(0),
  RED(PositionConsts.ZUGWANG_SCORE),
  YELLOW(-PositionConsts.ZUGWANG_SCORE);

  private int score;

  private Zugwang(int score) { this.score = score; }

  public int score() { return score; }
}
