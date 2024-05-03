package io.tnmma.ezwallet.data.db;

import android.util.Log;

import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.UUID;

public class IvyTypeConverter {

    private static final String TAG = IvyTypeConverter.class.getSimpleName();

    @TypeConverter
    public static UUID toUUID(String string) {
        try {
            return UUID.fromString(string);
        } catch (NullPointerException | IllegalArgumentException e) {
            Log.w(TAG, "UUID parsing error: String = " + string + " -> " + e);
            return null;
        }
    }

    @TypeConverter
    public static String fromUUID(UUID uuid) {
        return uuid != null ? uuid.toString() : null;
    }

    @TypeConverter
    public static LocalDate toLocalDate(String string) {
        try {
            return LocalDate.parse(string);
        } catch (NullPointerException | DateTimeParseException e) {
            Log.w(TAG, "LocalDate parsing error: String = " + string + " -> " + e);
            return null;
        }
    }

    @TypeConverter
    public static String fromLocalDate(LocalDate localDate) {
        return localDate != null ? localDate.toString() : null;
    }

    @TypeConverter
    public static Instant toInstant(String string) {
        try {
            return Instant.parse(string);
        } catch (NullPointerException | DateTimeParseException e) {
            Log.w(TAG, "Instant parsing error: String = " + string + " -> " + e);
            return null;
        }
    }

    @TypeConverter
    public static String fromInstant(Instant instant) {
        return instant != null ? instant.toString() : null;
    }
}
