// Java 3
// Lesson 4
// Homework
// Автор: Евгений Пермяков

//------------------------------------
// Задание 1

// Main.java

public class Main {
    private static final char[] SYMBOLS = {'A', 'B', 'C'};
    private static final int NUMBER_OF_SYMBOLS = 5;
    private static Object monitor = new Object();
    private static volatile MyChar currentSymbol = new MyChar(SYMBOLS[0]);

    public static void main(String[] args) {
        for (int i = 0; i < SYMBOLS.length; i++) {
            new Thread(new SymbolPrint(SYMBOLS[i], SYMBOLS[(i + 1) % SYMBOLS.length], NUMBER_OF_SYMBOLS, monitor, currentSymbol)).start();
        }
    }
}

// End of Main.java

// MyChar.java

public class MyChar {
    public char value;

    public MyChar(char value) {
        this.value = value;
    }
}

// End of MyChar.java

// SymbolPrint.java

public class SymbolPrint implements Runnable {
    private char symbol;
    private char nextSymbol;
    private int numberOfSymbols;
    private Object monitor;
    private MyChar currentSymbol;

    public SymbolPrint(char symbol, char nextSymbol, int numberOfSymbols, Object monitor, MyChar currentSymbol) {
        this.symbol = symbol;
        this.nextSymbol = nextSymbol;
        this.numberOfSymbols = numberOfSymbols;
        this.monitor = monitor;
        this.currentSymbol = currentSymbol;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < numberOfSymbols; i++) {
                synchronized (monitor) {
                    while(currentSymbol.value != symbol) {
                        monitor.wait();
                    }
                    System.out.print(symbol);
                    currentSymbol.value = nextSymbol;
                    monitor.notifyAll();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// End of SymbolPrint.java

//------------------------------------
// Задание 2

// ClientHandler.java
// ...
    public void startWorkerThread() {
        server.getServerExecutor().execute(new Runnable() {
            @Override
            public void run() {...}
        });
    }
// ...

// End of ClientHandler.java

// Server.java

// ...
    private ExecutorService serverExecutor;
// ...
    public ExecutorService getServerExecutor() {
        return serverExecutor;
    }
// ...
    public Server() {
        serverExecutor = Executors.newCachedThreadPool();
        try {...}
// ...
        catch ...{...  
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            serverExecutor.shutdown();
            authHandler.stop();
        }
    }
// ...

// End of Server.java
