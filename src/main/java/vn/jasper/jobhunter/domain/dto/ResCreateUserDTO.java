package vn.jasper.jobhunter.domain.dto;

import lombok.Getter;
import lombok.Setter;
import vn.jasper.jobhunter.utils.enums.GenderEnum;

import java.time.Instant;

@Getter
@Setter
public class ResCreateUserDTO extends ResUpdateUserDTO {
    private String email;
    private Instant createdAt;
}
