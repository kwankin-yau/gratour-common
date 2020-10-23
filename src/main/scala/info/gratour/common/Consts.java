package info.gratour.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import info.gratour.common.types.json.*;
import scala.Option;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class Consts {

    public static GsonBuilder defaultGsonBuilder() {
        return new GsonBuilder()
                .registerTypeHierarchyAdapter(LocalDate.class, new LocalDateMaterializer())
                .registerTypeHierarchyAdapter(LocalTime.class, new LocalTimeMaterializer())
                .registerTypeHierarchyAdapter(LocalDateTime.class, new LocalDateTimeMaterializer())
                .registerTypeHierarchyAdapter(OffsetDateTime.class, new OffsetDateTimeMaterializer())
                .registerTypeHierarchyAdapter(ZoneId.class, new ZoneIdMaterializer())
                .registerTypeHierarchyAdapter(Option.class, new OptionMaterializer())
                .registerTypeHierarchyAdapter(byte[].class, new ByteArrayBase64Materializer())
                ;

    }

    public static final Gson GSON = defaultGsonBuilder().create();
    public static final Gson GSON_PRETTY = defaultGsonBuilder().setPrettyPrinting().create();

    public static final ZoneId ZONE_ID_Z = ZoneId.of("Z");

    public static final ZoneOffset ZONE_OFFSET_BEIJING = ZoneOffset.ofHours(8);

//    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
//    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;
//    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static final DateTimeFormatter CONVENIENT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter CONVENIENT_DATETIME_FORMATTER_WITH_MILLIS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    public static final int[] EMPTY_INT_ARRAY = new int[0];
}
