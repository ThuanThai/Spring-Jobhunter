package vn.jasper.jobhunter.domain.dto;

import lombok.Getter;
import lombok.Setter;
import vn.jasper.jobhunter.utils.enums.GenderEnum;

import java.time.Instant;

@Getter
@Setter
public class ResUpdateUserDTO {
    private long id;
    private String name;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant updatedAt;

}
