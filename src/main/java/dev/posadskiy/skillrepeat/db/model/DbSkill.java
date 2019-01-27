package dev.posadskiy.skillrepeat.db.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DbSkill {
    @Id
    private String id;
    private String name;
    private Date lastRepeat;
    private Integer level;
}
