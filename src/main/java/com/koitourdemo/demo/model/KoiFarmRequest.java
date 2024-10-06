package com.koitourdemo.demo.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KoiFarmRequest {

    String koiFarmName;
    String koiFarmAddress;
    String koiFarmPhone;
    String koiFarmEmail;
    String koiFarmDescription;
//  String koiFarmImageURL;

}
