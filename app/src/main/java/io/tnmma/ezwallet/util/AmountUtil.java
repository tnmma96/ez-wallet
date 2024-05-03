package io.tnmma.ezwallet.util;

public class AmountUtil {

    private static final String TAG = AmountUtil.class.getSimpleName();

    private static final double ONE_THOUSAND = 1_000;

    private static final double ONE_MILLION = 1_000_000;

    private static final double ONE_BILLION = 1_000_000_000;

    private static final char THOUSAND_SUFFIX = 'k';

    private static final char MILLION_SUFFIX = 'm';

    private static final char BILLION_SUFFIX = 'b';

    public static final int DEFAULT_AMOUNT = 0;

    public static final double SMALLEST_AMOUNT_DIFF = 0.01;

    public static final long MIN_AMOUNT = -9_999_999_999L;

    public static final long MAX_AMOUNT = 9_999_999_999L;

    public static String format(long amount) {
        return String.format(CommonUtil.getDefaultAppLocale(), "%,d", amount);
    }

    public static String format(double amount) {
        return String.format(CommonUtil.getDefaultAppLocale(), "%,.2f", amount);
    }

    public static String formatToThousand(double amount) {
        if (Math.abs(amount) < ONE_THOUSAND) {
            return format(amount);
        }
        return String.format(CommonUtil.getDefaultAppLocale(), "%,.2f", amount / ONE_THOUSAND)
                + THOUSAND_SUFFIX;
    }

    public static String formatToMillion(double amount) {
        if (Math.abs(amount) < ONE_MILLION) {
            return format(amount);
        }
        return String.format(CommonUtil.getDefaultAppLocale(), "%,.2f", amount / ONE_MILLION)
                + MILLION_SUFFIX;
    }

    public static String formatToBillion(double amount) {
        if (Math.abs(amount) < ONE_BILLION) {
            return format(amount);
        }
        return String.format(CommonUtil.getDefaultAppLocale(), "%,.2f", amount / ONE_BILLION)
                + BILLION_SUFFIX;
    }

    public static String formatWithSuffixFromThousand(double amount) {
        if (Math.abs(amount) < ONE_THOUSAND) {
            return format(amount);
        } else if (Math.abs(amount) < ONE_MILLION) {
            return formatToThousand(amount);
        }
        return formatWithSuffixFromMillion(amount);
    }

    public static String formatWithSuffixFromMillion(double amount) {
        if (Math.abs(amount) < ONE_MILLION) {
            return format(amount);
        } else if (Math.abs(amount) < ONE_BILLION) {
            return formatToMillion(amount);
        }
        return formatToBillion(amount);
    }
}
