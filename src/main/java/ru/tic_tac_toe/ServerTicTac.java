package ru.tic_tac_toe;
// ТЗ к игре крестики-нолики: написать сервер к-й через консоль будет принимать значение
// в виде цифры от одного из игроков и возвращать обновлённое поле
// вид поля:
// ------------- # ----------- # -----------
// | 1 | 2 | 3 | # |1️⃣|2️⃣|3️⃣| # |❌|🟢|3️⃣|
// ------------- # ----------- # -----------
// | 4 | 5 | 6 | # |4️⃣|5️⃣|6️⃣| # |4️⃣|❌|6️⃣|
// ------------- # ----------- # -----------
// | 7 | 8 | 9 | # |7️⃣|8️⃣|9️⃣| # |7️⃣|8️⃣|🟢|
// ------------- # ----------- # -----------
//
// вместо о - U+1F535 = 🔵 | U+1F7E2 = 🟢 | ⭕
// вместо х - U+274C = ❌
// цифры - 1️⃣ 2️⃣ 3️⃣ 4️⃣ 5️⃣ 6️⃣ 7️⃣ 8️⃣ 9️⃣

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTicTac {

    private final int PORT = 1111;
    private final Logger logger = LoggerFactory.getLogger(ServerTicTac.class);

    public static void main(String[] args) {
        new ServerTicTac().startServer();
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (!Thread.currentThread().isInterrupted()) {
                logger.info("waiting client 1");
                Socket clientSocket1 = serverSocket.accept();
                logger.info("waiting client 2");
                Socket clientSocket2 = serverSocket.accept();
                clientHandler(clientSocket1, clientSocket2);
            }
        } catch (IOException e) {
            logger.error("error", e);
        }
    }

    private void clientHandler(Socket socket1, Socket socket2) {
        try (InputStream in1 = socket1.getInputStream();
             OutputStream out1 = socket1.getOutputStream();
             InputStream in2 = socket2.getInputStream();
             OutputStream out2 = socket2.getOutputStream()) {

            DriverTicTac game = new DriverTicTac();
            game.startGameServer(in1, out1, in2, out2);

        } catch (IOException e) {
            logger.error("error", e);
        }
    }
}
