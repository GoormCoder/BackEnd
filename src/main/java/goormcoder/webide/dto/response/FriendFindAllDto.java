package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Friend;

import java.util.List;

public record FriendFindAllDto(
        MemberFindDto friend
) {
    public static List<FriendFindAllDto> listOf(List<Friend> friends) {
        return friends
                .stream()
                .map(friend -> new FriendFindAllDto(
                        MemberFindDto.from(friend.getFriendId())
                )).toList();
    }
}
