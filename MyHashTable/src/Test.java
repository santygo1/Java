public class Test {

    public static void main(String[] args) {
        VisitorsFile file = new VisitorsFile("visitors.txt");
        file.generateRandomly(100);
        Visitor[] visitors = file.getVisitorsArray();

        HashTable<Integer,String> myHashTable = new HashTable<>();

        for (int i = 0; i < 100; i++){
            myHashTable.add(i, "stroka" + i);
        }
        myHashTable.print();

        for(int i = 0; i < 50;i++) myHashTable.remove(i);

        myHashTable.print();

        for(int i =50; i < 90; i++ ) myHashTable.remove(i);
        myHashTable.print();

        HashTable<Integer, Visitor> visitorHashTable = new HashTable<>();
        for(int i = 0; i < visitors.length; i++) visitorHashTable.add(i, visitors[i]);

        visitorHashTable.print();

    }

}


