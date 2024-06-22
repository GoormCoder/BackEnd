package goormcoder.webide.dto.response;

public record ScoreDto(
        int score,
        int rank
) {
    public static ScoreDto of(int score, int rank) {
        return new ScoreDto(score, rank);
    }

    // rank 값을 설정하는 메서드
    public ScoreDto withRank(int rank) {
        return new ScoreDto(this.score(), rank);
    }
}
