package com.bookmart.bookmart_backend.Mapper;

import ch.qos.logback.core.model.ComponentModel;
import com.bookmart.bookmart_backend.model.dto.response.UserResponse;
import com.bookmart.bookmart_backend.model.entity.User;
import org.mapstruct.Mapper;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel= "spring")
public interface UserMapper {

    UserResponse toDto(User user);


    default Date map(LocalDateTime value) {
        if (value == null) {
            return null;
        }
        return new Date(value.atZone(ZoneOffset.UTC).toInstant().toEpochMilli());
    }
}
