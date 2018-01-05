import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RodneCislo {

    public static RodneCislo parseOrThrow(String rodneCislo) throws IllegalArgumentException {
        RodneCislo result = parseOrNull(rodneCislo);
        if (result == null) {
            throw new IllegalArgumentException("Unable to parse string: " + rodneCislo);
        }
        return result;
    }

    public static RodneCislo parseOrNull(String rodneCislo) {
        if (rodneCislo == null) {
            return null;
        }

        String normalizedValue = rodneCislo.replaceAll("[^\\d]", "");

        if (!normalizedValue.matches("^[\\d]{9,10}$")) {
            return null;
        }

        int year = Integer.parseInt(normalizedValue.substring(0, 2));
        int month = Integer.parseInt(normalizedValue.substring(2, 4));
        int day = Integer.parseInt(normalizedValue.substring(4, 6));

        if (normalizedValue.length() == 9) {
            year += 1900;
        } else {
            int currentYear = new GregorianCalendar().get(Calendar.YEAR);
            year += (currentYear / 100) * 100;

            if (year > currentYear) {
                year -= 100;
            }
        }

        if ((year > 1953 || normalizedValue.length() == 10) && !isChecksumValid(normalizedValue)) {
            return null;
        }

        boolean male = true;

        if (month > 70 && year > 2003) {
            month -= 70;
            male = false;
        } else if (month > 50) {
            month -= 50;
            male = false;
        } else if (month > 20 && year > 2003) {
            month -= 20;
        }

        Date dateOfBirth = getValidDateOrNull(year, month, day);

        if (dateOfBirth == null) {
            return null;
        }

        String formattedValue = normalizedValue.substring(0, 6) + "/" + normalizedValue.substring(6);

        return new RodneCislo(formattedValue, normalizedValue, dateOfBirth, male);
    }

    private static boolean isChecksumValid(String normalizedValue) {
        if (normalizedValue.length() != 10) {
            return false;
        }

        int mod = Integer.parseInt(normalizedValue.substring(0, 9)) % 11;

        if (mod == 10) {
            mod = 0;
        }

        if (mod != Integer.parseInt(normalizedValue.substring(9))) {
            return false;
        }

        return true;
    }

    private static Date getValidDateOrNull(int year, int month, int dayOfMonth) {
        try {
            GregorianCalendar date = new GregorianCalendar(year, month - 1, dayOfMonth);
            date.setLenient(false);
            return date.getTime();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private String formattedValue;

    private String normalizedValue;

    private Date dateOfBirth;

    private boolean male;

    private RodneCislo(String formattedValue, String normalizedValue, Date dateOfBirth, boolean male) {
        this.formattedValue = formattedValue;
        this.normalizedValue = normalizedValue;
        this.dateOfBirth = dateOfBirth;
        this.male = male;
    }

    public String getFormattedValue() {
        return formattedValue;
    }

    public String getFormattedValue(String separator) {
        return normalizedValue.substring(0, 6) + separator + normalizedValue.substring(6);
    }

    public String getNormalizedValue() {
        return normalizedValue;
    }

    public Date getDateOfBirth() {
        return new Date(dateOfBirth.getTime());
    }

    public boolean isMale() {
        return male;
    }

    public boolean isFemale() {
        return !male;
    }

    @Override
    public String toString() {
        return formattedValue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((normalizedValue == null) ? 0 : normalizedValue.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RodneCislo other = (RodneCislo) obj;
        if (normalizedValue == null) {
            if (other.normalizedValue != null)
                return false;
        } else if (!normalizedValue.equals(other.normalizedValue))
            return false;
        return true;
    }

}
