package com.koitourdemo.demo.model.response;

import com.koitourdemo.demo.entity.Koi;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KoiPageResponse {
    List<Koi> content;
    int pageNumber;
    long totalElements;
    int totalPages;
}
