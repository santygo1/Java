/**
 * Класс периода брони
 *
 * @author Danil Spirin
 */
public class BookingPeriod {

    private final int countOfDays;        // Количество дней брони
    private final Date dateOfBeginning;   // Дата начала брони

    BookingPeriod(Date dateOfBeginning, int countOfDays){
        this.dateOfBeginning = dateOfBeginning;
        this.countOfDays = countOfDays;
    }
    @Override
    public String toString() {
        return this.dateOfBeginning.toString() + ", " + countOfDays;
    }
}
