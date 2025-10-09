package com.Library.SpringShelf.Utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class EmailMaskingSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null || !value.contains("@")) {
            gen.writeString(value);
            return;
        }
        String maskedEmail = value.replaceAll("(^.)(.*?)(.@)", "$1***$3");

        gen.writeString(maskedEmail);
    }
}
