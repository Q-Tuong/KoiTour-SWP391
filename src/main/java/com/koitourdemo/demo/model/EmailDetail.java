package com.koitourdemo.demo.model;

import com.koitourdemo.demo.entity.User;
import lombok.Data;

@Data
public class EmailDetail {
    User receiver;
    String subject;
    String link;
}
