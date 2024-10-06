package com.koitourdemo.demo.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KoiRequest {

    String koiName;
    String koiColor;
    String koiWeight;
    String koiSize;
    String koiOrigin;
    String koiDescription;

}
