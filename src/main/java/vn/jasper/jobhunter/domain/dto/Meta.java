package vn.jasper.jobhunter.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Meta {
    private int current;
    private int pageSize;
    private int pages;
    private long total;
}
