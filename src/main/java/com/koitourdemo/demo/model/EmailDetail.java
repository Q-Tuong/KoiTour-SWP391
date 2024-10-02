package com.koitourdemo.demo.model;

import com.koitourdemo.demo.entity.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailDetail {
    User receiver;
    String subject;
    String link;
}
