import java.io.*;
import java.util.Random;

public class VisitorsFile {

    private String filePath;

    VisitorsFile(String filePath){
        this.filePath = filePath;
    }

    /**
     * Процедура генерирования файла с данными посетителей рандомным образом
     *
     * @param countOfNotes - количество записей
     */
    public void generateRandomly(int countOfNotes){
        try{
            File inputFile = new File(filePath);
            FileWriter writer = new FileWriter(inputFile);
            writer.write(countOfNotes + "\n");

            String line = null;
            final Random random = new Random();

            for(int i = 0; i < countOfNotes; i++){

                // Генерируем поля
                int day = random.nextInt(30)+1;
                int month = random.nextInt(12)+1;
                int year = random.nextInt(23)+2000;

                int countOfDays = random.nextInt(30)+1;

                /*
                    Типы комнат: 1-комнатная(SGL), 1-комнатная для двоих(DBL, DBL TWN - с разделенной кроватью),
                                    1-комнатная для троих(TRPL)
                 */
                String[] roomTypes = {"SGL", "DBL" , "DBL TWN", "TRPL"};

                /*
                    Тип комнаты составляется путем выбора рандомным образом типа комнаты из массива тип комнат,
                    а также рандомным образом добавляются дополнения к комнате
                    (место для одного ребенка, для двух детей, и дополнительное спальное место)
                 */
                String typeOfRoom = roomTypes[random.nextInt(roomTypes.length)]
                        + (random.nextBoolean()?(random.nextBoolean()?"+1 CHD":"+2 CHD"):"")
                        +(random.nextBoolean()?"+EXB":"");

                long megaphoneCode = 8924000000L;
                long phoneNumber = random.nextInt(1000000)+megaphoneCode;

                String[] names = {"Павел", "Алексей", "Виталий", "Виктор", "Георгий", "Максим", "Данил",
                        "Илья" , "Григорий" , "Никита", "Денис" , "Александр", "Филипп", "Кирилл", "Генадий", "Михаил"};
                String[] surnames = {"Сапогов","Исаков","Криулин","Коши", "Иванов", "Спирин", "Дзюба", "Трунов",
                        "Рыжов","Андреев", "Плюта", "Богданов", "Падалко", "Пестряк", "Дьячек", "Жаров", "Дрозд", "Гагарин"};
                String[] patronymics = {"Олегович", "Алексеевич", "Витальевич", "Георгевич", "Максимович", "Денисович",
                        "Данилович", "Викторович", "Александрович", "Михайлович", "Сергеевич", "Борисович"};

                String FIO = surnames[random.nextInt(surnames.length)] + " " + names[random.nextInt(names.length)] + " "
                        + patronymics[random.nextInt(patronymics.length)];

                // Собираем строку из полей для записи
                line = day+"."+month+"."+year+","+countOfDays+","+typeOfRoom+","+phoneNumber+","+FIO;

                // Записываем строку
                writer.write(line+"\n");
            }
            writer.flush();
            writer.close();
        }catch (IOException ex){
            System.out.println("Ошибка вывода");
        }
    }


    /**
     * Функция очистки файла
     */
    public void clear(){
        try{
            new FileWriter(filePath,false).close();
        }catch (IOException ex){
            System.out.println("Файл отсутствует!");
        }
    }

    /**
     * Функция добавления данных о посетителе в файл
     * @param visitor
     */
    public void addVisitor(Visitor visitor){
        try{
            File inputFile = new File(filePath);
            FileWriter writer = new FileWriter(inputFile,true);

            writer.write(visitor.toString()+"\n");
            writer.flush();
            writer.close();
        }catch (IOException ex){
            System.out.println("Посетитель не был добавлен в файл " + filePath);
        }
    }

    /**
     * Процедура записи массива посетителей в указанный файл
     *
     * @param visitors - массив данных посетителей для записи
     */
    public void writeArray(Visitor[] visitors){
        try {
            File inputFile = new File(filePath);
            FileWriter writer = new FileWriter(inputFile);

            for(Visitor current: visitors){
                writer.write(current.toString() + "\n");
            }

            writer.flush();
            writer.close();
        }catch (IOException ex){
            System.out.println("Ошибка вывода");
        }
    }

    /**
     * Функция парсинга текстового файла, состоящего из данных о посетителях, в массив посетителей.
     *
     * @return массив данных посетителей
     * @exception IOException Не найден файл по пути filePath
     * @exception NegativeArraySizeException Первое поле в файле - количество записей массива
     */
     public Visitor[] getVisitorsArray(){
        Visitor[] visitorsFromFile = null;
        try {
            File visitorsFile = new File(filePath);// Путь к файлу
            BufferedReader fileReader = new BufferedReader(new FileReader(visitorsFile)); // буферизированный поток для чтения файла

            int countOfVisitors = Integer.parseInt(fileReader.readLine()); // определяем количество записей
            visitorsFromFile = new Visitor[countOfVisitors];

            for(int currentVisitor = 0; currentVisitor < countOfVisitors; currentVisitor++){
                visitorsFromFile[currentVisitor] = getVisitor(fileReader.readLine(),currentVisitor+2);
            }

        }catch (IOException pathError){
            System.out.println("Не удалось найти файл по пути " + filePath + "!");
        }catch (NegativeArraySizeException indexError){
            System.out.println("Количество записей указано некорректно!");
        }catch (NumberFormatException formatError){
            System.out.println("Записи не были считаны: Не удалось считать количество данных!");
        }

        return visitorsFromFile;
    }

    /**
     * Функция парсинга строки с данными о посетителе в объект класса Visitor(данные о посетителе)
     *
     * @return обьект Visitor с заполненными данными
     */
    private Visitor getVisitor(String lineToParse,int lineNumber){
        String[] tokens = lineToParse.split(","); // делим строку на поля
        String[] dateTokens = tokens[0].split("\\."); // делим поле дата
        Date date = new Date(Integer.parseInt(dateTokens[0]),Integer.parseInt(dateTokens[1]),
                Integer.parseInt(dateTokens[2]));
        return new Visitor(new BookingPeriod(date,Integer.parseInt(tokens[1])),tokens[2],Long.parseLong(tokens[3]),tokens[4],lineNumber);
    }

}
