package io.tnmma.ezwallet.util;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.DateType;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;

public class DateTimeUtil {

    private static final String TAG = DateTimeUtil.class.getSimpleName();

    public static final LocalDate MIN_DATE = LocalDate.parse("1000-01-01");

    public static final LocalDate MAX_DATE = LocalDate.parse("3000-12-31");

    public static Instant getCurrentTime() {
        return Instant.now();
    }

    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    public static Pair<LocalDate, LocalDate> getRange(DateType dateType) {
        if (dateType == null) {
            return null;
        }

        switch (dateType) {
            case TODAY:
                return new Pair<>(getCurrentDate(), getCurrentDate());
            case THIS_WEEK:
                return getCurrentWeekRange();
            case THIS_MONTH:
                return getCurrentMonthRange();
            case THIS_YEAR:
                return getCurrentYearRange();
            default:
                return null;
        }
    }

    private static Pair<LocalDate, LocalDate> getCurrentYearRange() {
        LocalDate first = LocalDate.now().withDayOfYear(1);
        LocalDate last = first.withDayOfYear(first.lengthOfYear());
        return new Pair<>(first, last);
    }

    private static Pair<LocalDate, LocalDate> getCurrentMonthRange() {
        LocalDate first = LocalDate.now().withDayOfMonth(1);
        LocalDate last = first.withDayOfMonth(first.lengthOfMonth());
        return new Pair<>(first, last);
    }

    private static Pair<LocalDate, LocalDate> getCurrentWeekRange() {
        LocalDate first = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate last = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        return new Pair<>(first, last);
    }

    public static String getFormattedDate(LocalDate date, boolean showFullMonth) {
        if (date == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(
                        date.getMonth()
                                .getDisplayName(
                                        showFullMonth ? TextStyle.FULL : TextStyle.SHORT,
                                        CommonUtil.getDefaultAppLocale()))
                .append(
                        String.format(
                                CommonUtil.getDefaultAppLocale(), " %02d", date.getDayOfMonth()));
        if (getCurrentDate().getYear() != date.getYear()) {
            builder.append(", ").append(date.getYear());
        }
        return builder.toString();
    }

    public static String getFormattedDayOfWeek(LocalDate date) {
        if (date == null) {
            return "";
        }

        int stringResId = ResourcesCompat.ID_NULL;
        if (Objects.equals(getCurrentDate(), date)) {
            stringResId = R.string.label_day_of_week_today;
        } else if (Objects.equals(getCurrentDate().plusDays(1), date)) {
            stringResId = R.string.label_day_of_week_tomorrow;
        } else if (Objects.equals(getCurrentDate().minusDays(1), date)) {
            stringResId = R.string.label_day_of_week_yesterday;
        }
        return stringResId != ResourcesCompat.ID_NULL
                ? StringUtil.getStringFromResId(stringResId)
                : date.getDayOfWeek()
                        .getDisplayName(TextStyle.FULL, CommonUtil.getDefaultAppLocale());
    }
}
