package goormcoder.webide.dto.response;

public record BattleWaitSimpleDto(
        Long roomId,
        boolean isFull
) {
    public static BattleWaitSimpleDto of(Long roomId, boolean isFull) {
        return new BattleWaitSimpleDto(roomId, isFull);
    }
}
