package goormcoder.webide.dto.response;

import goormcoder.webide.domain.FriendRequest;

import java.util.List;

public record FriendRequestFindAllDto(
        Long requestId,
        MemberFindDto requester,
        MemberFindDto receiver
) {
    public static List<FriendRequestFindAllDto> listOf(List<FriendRequest> requests) {
        return requests
                .stream()
                .map(request -> new FriendRequestFindAllDto(
                        request.getId(),
                        MemberFindDto.from(request.getRequestId()),
                        MemberFindDto.from(request.getReceivedId())
                )).toList();
    }
}
