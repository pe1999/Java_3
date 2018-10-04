// Java 3
// Lesson 6
// Homework
// Автор: Евгений Пермяков

//------------------------------------
// Тестирование

// Main.java
package ru.geekbrains.pe1999.java3.hw6;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        System.out.println(Arrays.toString(arrayCutAfter4(new int[] {1, 4})));
        System.out.println(arrayCheck1And4(new int[] {1, 4, 2, 1, 1}));
    }

    public static int[] arrayCutAfter4 (int[] arr) {
        if(arr[arr.length - 1] == 4) return new int[] {};
        for (int i = arr.length - 1; i > 0 ; i--) {
            if(arr[i - 1] == 4) {
                return Arrays.copyOfRange(arr, i, arr.length);
            }
        }
        throw new RuntimeException("Входной массив не содержит ни одной 4.");
    }

    public static boolean arrayCheck1And4 (int[] arr) {
        boolean isHave1 = false;
        boolean isHave4 = false;
        boolean isHaveAnotherDigit = false;

        for (int anArr : arr) {
            isHave1 |= anArr == 1;
            isHave4 |= anArr == 4;
            isHaveAnotherDigit |= anArr != 1 & anArr != 4;
        }

        return isHave1 & isHave4 & !isHaveAnotherDigit;
    }
}
// End of Main.java

// ArrayCutAfter4Tests.java
import org.junit.Assert;
import org.junit.Test;
import ru.geekbrains.pe1999.java3.hw6.Main;

public class ArrayCutAfter4Tests {
    @Test
    public void test1() {
        Assert.assertArrayEquals(new int[] {1, 2}, Main.arrayCutAfter4(new int[] {14, 1, 4, 1, 2}));
    }

    @Test(expected = RuntimeException.class)
    public void  runtimeExceptionTest() {
        Main.arrayCutAfter4(new int[] {1, 2});
    }
}
// End of ArrayCutAfter4Tests.java

// ArrayCutAfter4MassTest.java
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.geekbrains.pe1999.java3.hw6.Main;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ArrayCutAfter4MassTest {
    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][] {
            {new int[] {4, 1, 2}, new int[] {1, 2}},
            {new int[] {4}, new int[] {}},
            {new int[] {4, 1, 2, 4}, new int[] {}},
            {new int[] {4, 4, 1, 2}, new int[] {1, 2}},
            {new int[] {4, 1, 2, 3, 4, 5, 4, 3}, new int[] {3}}
        });
    }

    private int[] inputArray;
    private int[] outputArray;

    public ArrayCutAfter4MassTest(int[] inputArray, int[] outputArray) {
        this.inputArray = inputArray;
        this.outputArray = outputArray;
    }

    @Test
    public void massTest() {
        Assert.assertArrayEquals(outputArray, Main.arrayCutAfter4(inputArray));
    }
}
// End of ArrayCutAfter4MassTest.java

// ArrayCheck1And4MassTest.java
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.geekbrains.pe1999.java3.hw6.Main;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ArrayCheck1And4MassTest {
    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][] {
                {true, new int[] {1, 4, 4, 4}},
                {true, new int[] {1, 1, 1, 1, 4, 4}},
                {true, new int[] {4, 1}},
                {true, new int[] {1, 4}},
                {false, new int[] {2}},
                {false, new int[] {1, 2, 4}},
                {false, new int[] {1, 4, 1, 4, 0}},
                {false, new int[] {}}
        });
    }

    private boolean result;
    private int[] inputArray;

    public ArrayCheck1And4MassTest(boolean result, int[] inputArray) {
        this.result = result;
        this.inputArray = inputArray;
    }

    @Test
    public void massTest() {
        Assert.assertEquals(result, Main.arrayCheck1And4(inputArray));
    }
}
// End of ArrayCheck1And4MassTest.java

//------------------------------------
// Логирование

// Server.java
package ru.geekbrains;

// ...

public class Server {
// ...
    private final Logger logger = Logger.getLogger(this.getClass().getName());
// ...
    public Logger getLogger() {
        return logger;
    }

    public Server() {
        serverExecutor = Executors.newCachedThreadPool();
        logger.setLevel(Level.ALL);
        try {
            authHandler = new DBAuthHandler(); //SimpleAuthHandler();
            authHandler.start();
            serverSocket = new ServerSocket(8189);
            clients = new Vector<ClientHandler>();
            logger.log(Level.INFO, "Сервер запущен");
            while (true) {
                Socket socket = serverSocket.accept();
                logger.log(Level.INFO, "Клиент подключился");
                new ClientHandler(this, socket);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
//...
}
// End of Server.java

// ClientHandler.java
// ...
    public void startWorkerThread() {
        server.getServerExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String msg = in.readUTF();
                        if (msg.startsWith("/auth ")) {
                            // /auth login1 pass1
                            String[] tokens = msg.split(" ");
                            ClientHandler.this.login = tokens[1];
                            String nick = server.getAuthHandler().getNickByLoginPass(tokens[1], tokens[2]);
                            if (nick != null) {
                                if (server.isNickBusy(nick)) {
                                    out.writeUTF("Учетная запись уже используется");
                                    server.getLogger().log(Level.WARNING, "Учетная запись уже используется");
                                    continue;
                                }
                                out.writeUTF("/authok " + nick);
                                ClientHandler.this.nick = nick;
                                server.subscribe(ClientHandler.this);
                                server.getLogger().log(Level.INFO, "Пользователь " + nick + " авторизовался");
                                break;
                            } else {
                                out.writeUTF("Неверный логин/пароль");
                                server.getLogger().log(Level.WARNING, "Неверный логин/пароль");
                            }
                        }
                    }
                    while (true) {
                        String msg = in.readUTF();
                        if (msg.startsWith("/")) {
                            if (msg.startsWith("/w ")) {
                                // /w nick2 hello hello
                                String[] tokens = msg.split(" ", 3);
                                server.sendPrivateMsg(ClientHandler.this, tokens[1], tokens[2]);
                                server.getLogger().log(Level.INFO, "Пользователь " + ClientHandler.this.nick + " отправил приватное сообщение");
                            }
                            if (msg.startsWith("/changenick ")) {
                                String[] tokens = msg.split(" ");
                                if (tokens.length == 2) {
                                    if (server.getAuthHandler().changeNick(ClientHandler.this.login, tokens[1])) {
                                        server.getLogger().log(Level.INFO, "Пользователь " + ClientHandler.this.nick + " сменил ник на " + tokens[1]);
                                        server.broadcastMsg(ClientHandler.this, "Пользователь " + ClientHandler.this.nick + " сменил ник на " + tokens[1] + ".");
                                        ClientHandler.this.nick = tokens[1];
                                        server.broadcastClientsList();
                                    } else {
                                        server.getLogger().log(Level.WARNING, "Ник " + tokens[1] + " уже используется");
                                        ClientHandler.this.sendMessage("Ник уже используется.");
                                    }
                                } else {
                                    server.getLogger().log(Level.WARNING, "Пользователь " + ClientHandler.this.nick + " использовал неправильный формат команды /changenick");
                                    ClientHandler.this.sendMessage("Неправильный формат команды /changenick.");
                                }
                            }
                        } else {
                            server.getLogger().log(Level.INFO, "Пользователь " + ClientHandler.this.nick + " отправил широковещательное сообщение");
                            server.broadcastMsg(ClientHandler.this, msg);
                        }
                        //System.out.println(msg);
                    }
                } catch (IOException e) {
                    //e.printStackTrace();
                } finally {
                    ClientHandler.this.closeConnection();
                    server.getLogger().log(Level.INFO, (ClientHandler.this.nick != null ? "Пользователь " + ClientHandler.this.nick : "Клиент") + " отключился");
                }
            }
        });
    }
// ...
}
// End of ClientHandler.java
