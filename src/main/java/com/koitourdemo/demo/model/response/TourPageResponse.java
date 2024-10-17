package com.koitourdemo.demo.model.response;

import com.koitourdemo.demo.entity.Tour;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourPageResponse {
    List<Tour> content;
    int pageNumber;
    long totalElements;
    int totalPages;
}
