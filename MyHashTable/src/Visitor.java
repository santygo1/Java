/**
 * Класс записи посетителя
 *
 * @author Danil Spirin
 */
public class Visitor {

    private final String FIO;
    private final String roomType;              // Тип забронированной посетителем комнаты
    private final BookingPeriod bookingPeriod;    // Период бронирования комнаты посетителем
    private final long phoneNumber;
    private final int fileIndex;                // Индекс посетителя в файле
    

    Visitor(BookingPeriod bookingPeriod, String roomType, long phoneNumber,String FIO,int fileIndex){
        this.bookingPeriod = bookingPeriod;
        this.roomType = roomType;
        this.phoneNumber = phoneNumber;
        this.FIO = FIO;
        this.fileIndex = fileIndex;
    }

    public long getPhoneNumber(){
        return phoneNumber;
    }

    public int getFileIndex() {
        return fileIndex;
    }

    @Override
    public String toString() {
        return bookingPeriod + ", " + roomType + ", " + phoneNumber + ", " + FIO;
    }
}
