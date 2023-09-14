package com.local.vincent.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Testentity {
    @Id
    private long id;

    private String title;

}
