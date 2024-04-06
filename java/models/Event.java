package com.incorpr.models;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title, type;

    @Column(length = 5000)
    private String description;

    private int day, month, year;

    public Event(String title, String type, String description, int day, int month, int year) {
        this.title = title;
        this.type = type;
        this.description = description;
        this.day = day;
        this.month = month;
        this.year = year;
    }
}

