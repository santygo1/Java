public class HashTable<K,V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 1<<4; // Первоначальный размер таблицы (16).
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;     // Коэффициент, при котором произойдет увеличение таблицы.

    private static class Node<K,V>{
        K key;
        V value;
        boolean state; //Статус элемента ,false - был удален (deleted)

        Node(K key, V value){
            this.key = key;
            this.value = value;
            state = true;
        }
    }

    private Node<K,V>[] table; // Сама таблица.
    private int realSize;    // Кол-во элементов в массиве, без учета deleted.
    private int imagineSize;       // Кол-во элементов в массиве, с учетом deleted.
    private int capacity;       // размер самого массива, сколько памяти выделено под хранение нашей таблицы

    HashTable(){
        capacity = DEFAULT_INITIAL_CAPACITY;
        realSize = 0;
        imagineSize = 0;
        table = new Node[capacity];
    }

    //Приватный конструктор для изменения размера таблицы
    private HashTable(int capacity){
        this.capacity = capacity;
        realSize = 0;
        imagineSize = 0;
        table = new Node[this.capacity];
    }


    /**
     * Функция изменения размера текущей таблицы
     * @param capacity новый размер таблицы
     */
    private void resize(int capacity){
        int prevCapacity = this.capacity; // Сохраняем размер текущей таблицы

        this.capacity = capacity;   // Новый размер таблицы (сужение или расширение)

        //Выделяем место для новой таблицы
        HashTable<K,V> tmp = new HashTable<>(this.capacity);

        // Перекидываем реальные элементы из таблицы до расширения размера таблицы, в нашу новую
        for(int i = 0; i < prevCapacity; i++){
            if (table[i] != null && table[i].state){
                tmp.add(table[i].key,table[i].value);
            }
        }

        // Делаем ее нашей новой таблицей
        table = tmp.table;
        realSize = tmp.realSize;
        imagineSize = tmp.imagineSize;
    }


    /**
     * Функция добавления элемента в таблицу
     * @param key ключ
     * @param value значение
     * @exception SameKeyException элемент с данным ключом уже присутствует в таблице
     */
    public void add(K key, V value){
        // Если количество настоящих элементов c учетом будущего добавленного
        // больше чем LF*m переформировываем таблицу и увеличиваем её
        if (realSize + 1 > DEFAULT_LOAD_FACTOR* capacity) resize(capacity *2);
        // Иначе если меньше LF*m и мнимых больше чем реальных в два раза переформировываем таблицу
        else if (imagineSize > 2* realSize) resize(capacity);

        // Получаем хеш для добавления
        int keyHash = getHashForAdd(key);

        // Если не добавлен то добавляем
        if (keyHash != -1){
            table[keyHash] = new Node<>(key, value);
            ++realSize;
            ++imagineSize;
        }else throw new SameKeyException("В данной таблице уже есть ключ " + key);
    }

    /**
     * Функция, которая возвращает правильный хеш ключа для добавления ключа(решение коллизий)
     * @param key - ключ
     * @return хеш ключ для добавления в таблицу
     *          (-1) если элемент с ключом key уже присутствует в таблице
     */
    private int getHashForAdd(K key){
        int h1 = hash1(key);
        int deletedIndex = -1; // (-1) - элемент со статусом deleted не был встречен

        for (int j = 0; j < capacity; j++){
            int h;
            if ( j == 0) h = h1;
            else h = hash2(h1,j);

            if (table[h] == null) return h; // Нашли место вставки
            if (table[h].key == key && table[h].state) return -1; // Элемент уже есть в таблице
            if (!table[h].state && deletedIndex == -1) deletedIndex = h; // Найдено первое вхождение deleted элемента
        }
        // В противном случае вернет либо что элемент существует либо первое вхождение deleted элемента
        return deletedIndex;
    }


    /**
     * Функция удаления элемента из таблицы по заданному ключу
     * @param key - ключ
     * @exception KeyException элемент с заданным ключом отсутствует в таблице
     */
    public void remove(K key){
        // Получаем правильный хеш для ключа
        int keyHash = getHashForRemove(key);

        // Если ключ есть удаляем
        if (keyHash != -1){
            table[keyHash].state = false;
            realSize--;
        }else throw new KeyException("Ключ " + key + " не был удален: Такого ключа нет");


        if(realSize - 1 < (1-DEFAULT_LOAD_FACTOR)* capacity && capacity >DEFAULT_INITIAL_CAPACITY){
            resize(capacity /2); // Урезаем и переформировываем
        }else if(imagineSize > 2* realSize){
            resize(capacity); // Переформировываем
        }
    }


    /**
     * Функция, которая возвращает правильный хеш для удаления ключа
     * @param key - ключ
     * @return  хеш ключа для удаления из таблицы
     *          (-1) ключ отсутствует или удален
     */
    public int getHashForRemove(K key){
        int h1 = hash1(key);
        for (int j = 0; j < capacity; j++){
            int h;
            if (j == 0) h = h1;
            else h = hash2(h1, j);
            if (table[h] == null) return -1;
            if (table[h].key == key && table[h].state) return h;
        }
        return -1;
    }


    /**
     * Функция, возвращающая значение по ключу
     * @param key - ключ
     * @return значение по ключу
     */
    public V get(K key){
        int h1 = hash1(key);

        for (int j = 0; j < capacity; j++){
            int h;
            if (j == 0) h = h1;
            else h = hash2(h1,j);
            if (table[h] == null) break; //Встретили пустой элемент - выход
            if (table[h].key == key && table[h].state) return table[h].value; // Нашли по вторичной - возвращаем значение
        }
        // Количество попыток превысило размер таблицы - элемента нет
        throw new KeyException("Ключ : " + key + " отсутствует");
    }


    /**
     * Функция вывода хэш-таблицы в консоль
     */
    public void print(){
        Table consoleTable = new Table("ID", "KEY","VALUE","STATUS");
        for (int i = 0; i< capacity; i++){
            if (table[i]!=null)
                consoleTable.addRow(String.valueOf(i), table[i].key.toString(),
                        table[i].value.toString(), String.valueOf(table[i].state));
            else
                consoleTable.addRow(String.valueOf(i),"null","null","null");
        }

        consoleTable.print();
    }

    /**
     * Первичная хеш-функция,
     * @param key - ключ
     * @return индекс элемента в данной таблицы
     */
    private int hash1(K key){
        int oldHash = ((Object)key).hashCode(); // Получаем хеш код ключа, в случае с int это то же значение
        int hash = 0;

        //Сумма разрядов по два
        for (int i = oldHash;i != 0; i /= 100) hash += (i % 100); //12456 = 1+24+56
        hash = hash % capacity; // hash(1232) = (12+32) mod 2 = 44 mod m
        return hash;
    }

    /**
     * Вторичная хеш-функция, методом квадратичного зондирования (hash1(key) + j*k1 + (j^2)*k)mod m
     * @param hash1 - первичная хеш-функция для элемента
     * @param j - номер попытки разрешить коллизию
     * @return индекс элемента в данной таблице
     */
    private int hash2(int hash1, int j){
        //Вспомогательные константы k1,k2 >=1
        int k1 = 1;
        int k2 = 1;

        return (hash1 + j*k1 + j*j*k2)% capacity;
    }
}

class KeyException extends ArrayIndexOutOfBoundsException{
    KeyException(String message){
        super(message);
    }
}

class SameKeyException extends KeyException{
    SameKeyException(String message){
        super(message);
    }
}

