package goormcoder.webide.dto.response;

public record BattleWaitFindDto(
        Long roomId,
        boolean isFull
) {
    public static BattleWaitFindDto of(Long roomId, boolean isFull) {
        return new BattleWaitFindDto(roomId, isFull);
    }
}
