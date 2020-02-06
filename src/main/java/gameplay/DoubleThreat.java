package gameplay;

public enum DoubleThreat {
  NONE(0),
  RED(PositionConsts.DOUBLE_THREAT_SCORE),
  YELLOW(-PositionConsts.DOUBLE_THREAT_SCORE);

  private int score;

  private DoubleThreat(int score) { this.score = score; }

  public int score() { return score; }
}
