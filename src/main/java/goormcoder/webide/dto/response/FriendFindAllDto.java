package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Friend;

import java.util.List;

public record FriendFindAllDto(
        FriendFindDto friend
) {
    public static List<FriendFindAllDto> listOf(List<Friend> friends) {
        return friends
                .stream()
                .map(friend -> new FriendFindAllDto(
                        FriendFindDto.from(friend.getFriendId())
                )).toList();
    }
}
