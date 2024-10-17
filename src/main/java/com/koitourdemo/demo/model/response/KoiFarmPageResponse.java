package com.koitourdemo.demo.model.response;

import com.koitourdemo.demo.entity.KoiFarm;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KoiFarmPageResponse {
    List<KoiFarm> content;
    int pageNumber;
    long totalElements;
    int totalPages;
}
