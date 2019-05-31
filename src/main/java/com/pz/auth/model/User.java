package com.pz.auth.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Data
@Getter
@Setter
@Entity(name = "USER6")
@NoArgsConstructor
public class User {
    @Id @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String login;
    private String password;
}
