package mx.conacyt.crip.ejemplos.openapi.web.api.mapper;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class DateMapper {

    public OffsetDateTime asOffsetDateTime(Instant date) {
        try {
            return date != null ? date.atOffset(ZoneOffset.UTC) : null;
        } catch (DateTimeException e) {
            throw new RuntimeException(e);
        }
    }

    public Instant asInstant(OffsetDateTime date) {
        try {
            return date != null ? date.toInstant() : null;
        } catch (UnsupportedOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
