package goormcoder.webide.dto.request;

import goormcoder.webide.domain.enumeration.Language;

public record SolveCreateDto(
        String code,
        Language language
) {

}
