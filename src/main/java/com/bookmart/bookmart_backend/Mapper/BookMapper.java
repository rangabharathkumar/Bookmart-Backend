package com.bookmart.bookmart_backend.Mapper;

import com.bookmart.bookmart_backend.model.dto.response.BookResponse;
import com.bookmart.bookmart_backend.model.dto.response.UserResponse;
import com.bookmart.bookmart_backend.model.entity.Book;
import org.mapstruct.Mapper;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel= "spring")
public interface BookMapper {
    BookResponse toDto(Book book);

    default Date map(LocalDateTime value) {
        if (value == null) {
            return null;
        }
        return new Date(value.atZone(ZoneOffset.UTC).toInstant().toEpochMilli());
    }
}
