package com.example.demo_api_movie.CommentService;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum AttachmentType {
    ICON,
    IMAGE,
    VIDEO;

    @JsonCreator
    public static AttachmentType fromString(String value) {
        if (value == null) {
            return null;
        }
        for (AttachmentType type : AttachmentType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid attachment type: " + value);
    }

    @JsonValue
    public String toValue() {
        return this.name().toLowerCase();
    }
}
