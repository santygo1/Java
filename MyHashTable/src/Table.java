import java.util.LinkedList;
import java.util.List;

public class Table{

    String[] header; // Шапка таблицы
    int[] maxColumnLengths;  // Максимальные размеры ячеек
    List<String[]> rows;     // Строки

    /**
     * Создает новую таблицу по заданным коллонкам
     * @param columns название коллонок в таблице
     */
    Table(String ... columns){
        header = columns;
        maxColumnLengths = new int[columns.length];
        rows = new LinkedList<>();
        rows.add(header);

        // Определяем размеры ячеек
        for (int i = 0; i < columns.length;i++)
            maxColumnLengths[i] = columns[i].length();
    }

    /**
     * Функция добавления строки
     * @param columns
     * @exception RowsCountOutOfBoundsException количество указанных столбцов не соответствует количеству установленных в таблице столбцов
     */
    public void addRow(String ... columns){
        if (columns.length != header.length)
            throw new RowsCountOutOfBoundsException("Количество указанных столбцов не соответствует " +
                    "установленному количеству столбцов в этой таблице");

        for (int i = 0; i < columns.length; i++) {
            if (maxColumnLengths[i] < columns[i].length())
                maxColumnLengths[i] = columns[i].length();
        }
        rows.add(columns);
    }

    /**
     * Функция вывода таблицы в консоль
     */
    public void print(){
        int headerWholeSize = 0; // длина всех столбцов
        for (int columnLength :maxColumnLengths) headerWholeSize+=columnLength;
        StringBuilder delimiter = new StringBuilder();
        delimiter.append("-------------"); // все остальные элементы кроме названий столбцов(делиметры, пробелы)
        delimiter.append("-".repeat(Math.max(0, headerWholeSize)));

        System.out.println(delimiter);
        boolean hasHeaderPrinted = false;
        for (String[] row : rows){
            System.out.print("| ");
            for (int i = 0; i < row.length; i++){
                int weight = maxColumnLengths[i] - row[i].length(); //Высчитываем вес для того, чтобы поместить строку посередине
                int spacesEachSides = weight/2; // сколько нужно добавить с каждой стороны, чтобы был посередине
                int extraSpace = weight%2; // если нечетная длина

                // Помещаем посередине
                for (int left = 1; left <= spacesEachSides; left++) System.out.print(" ");
                if (extraSpace != 0) System.out.print(" ");
                System.out.print(row[i]);
                for(int right = 1; right <= spacesEachSides; right++) System.out.print(" ");
                System.out.print(" | ");
            }
            // Вывод делиметра снизу для хедера
            if (!hasHeaderPrinted){
                System.out.println();
                System.out.println(delimiter);
                hasHeaderPrinted = true;
            }else System.out.println();
        }
        System.out.println(delimiter);
    }

}

class RowsCountOutOfBoundsException extends ArrayIndexOutOfBoundsException{
    RowsCountOutOfBoundsException(String message){
        super(message);
    }
}
