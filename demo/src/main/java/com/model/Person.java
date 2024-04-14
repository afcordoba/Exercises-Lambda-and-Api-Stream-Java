package com.model;

import java.time.LocalDate;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Person {
    private Integer id;
    private String name;
    private LocalDate birthDate;
}
