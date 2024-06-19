package goormcoder.webide.dto.response;

import goormcoder.webide.domain.Member;

public record MessageSenderFindDto(

        Long id,
        String longinId,
        String name

) {

    public static MessageSenderFindDto from(Member member) {
        return new MessageSenderFindDto(
                member.getId(),
                member.getLoginId(),
                member.getName()
        );
    }

}
