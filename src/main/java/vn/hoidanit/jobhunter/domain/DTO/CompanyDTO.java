package vn.hoidanit.jobhunter.domain.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDTO {
    @NotBlank(message = "Name must be filled")
    private String name;

    private String description;

    private String address;

    private String logo;
}
