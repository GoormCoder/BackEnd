package goormcoder.webide.dto.request;

import goormcoder.webide.domain.enumeration.Gender;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MyPageDto {
    private String loginId;
    private String nick;
    private String name;
    private LocalDate birth;
    private String email;
    private String info;
    private Gender gender;
    private int praiseScore;
    private int battleScore;
}
