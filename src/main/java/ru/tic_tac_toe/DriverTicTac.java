package ru.tic_tac_toe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class DriverTicTac {

    private static final byte[] DRAW_BYTES = "Draw".getBytes();
    private static final byte[] WIN_BYTES = "Win".getBytes();
    private static final byte[] LOSE_BYTES = "Lost".getBytes();
    private static final byte[] YR_TURN_BYTES = "Your turn:\n\r".getBytes();
    private static final byte[] WAIT_BYTES = "Wait...\n\r".getBytes();
    private static final byte[] CHECK_BYTES = "Enter a number from 1 to 9 corresponding to the empty field:\n\r".getBytes();

    private final Logger logger = LoggerFactory.getLogger(DriverTicTac.class);
    private List<Values> field;
    private Values current = Values.CROSS;
    private boolean server;

    public static void main(String[] args) {
        new DriverTicTac().startGameLocal();
    }

    public DriverTicTac() {
        startBoard();
    }

    public void startGameLocal() {
        server = false;
        showBoard();
        System.out.println("Введите число от 1 до 9 соответсвующее незанятому полю:");
        try (Scanner scanner = new Scanner(System.in)) {
            for (int i = 0; i < field.size(); i++) {

                int in = getCorrectValue(scanner);
                changeField(in);
                showBoard();
                if (checkWin()) {
                    System.out.println("Победа");
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Ничья");
    }

    public void startGameServer(InputStream in1, OutputStream out1, InputStream in2, OutputStream out2) {
        logger.info("start game");
        server = true;
        String tmpStr = getBoardToString();
        byte[] boardToByte = tmpStr.getBytes();
        try (Scanner scanner1 = new Scanner(in1);
             Scanner scanner2 = new Scanner(in2)) {
            out1.write(boardToByte);
            out1.write(YR_TURN_BYTES);
            out2.write(boardToByte);
            out2.write(WAIT_BYTES);
            for (int i = 0; i < field.size(); i++) {
                int in = getCorrectValue(i % 2 == 0 ? scanner1 : scanner2, i % 2 == 0 ? out1 : out2);
                changeField(in);
                boardToByte = getBoardToString().getBytes();
                out1.write(boardToByte);
                out1.write(i % 2 == 1 ? YR_TURN_BYTES: WAIT_BYTES);
                out2.write(boardToByte);
                out2.write(i % 2 == 0 ? YR_TURN_BYTES: WAIT_BYTES);
                if (checkWin()) {
                    if (i % 2 == 0) {
                        out1.write(WIN_BYTES);
                        out2.write(LOSE_BYTES);
                    } else {
                        out1.write(LOSE_BYTES);
                        out2.write(WIN_BYTES);
                    }
                    return;
                }
            }
            out1.write(DRAW_BYTES);
            out2.write(DRAW_BYTES);
        } catch (IOException e) {
            logger.error("error",e);
        } finally {
            logger.info("end game");
        }
    }

    private int getCorrectValue(Scanner scanner) throws IOException {
        return getCorrectValue(scanner, null);
    }

    private int getCorrectValue(Scanner scanner, OutputStream out) throws IOException {
        int number = -1;
        boolean goodValue = false;
        while (!goodValue) {
            try {
                number = scanner.nextInt();
                if (number < 1 || number > 9)
                    throw new InputMismatchException();
                if (field.get(number - 1) == Values.CROSS || field.get(number - 1) == Values.CIRCLE)
                    throw new InputMismatchException();
                goodValue = true;
            } catch (InputMismatchException e) {
                if (!server) {
                    System.out.println("Введите число от 1 до 9 соответсвующее незанятому полю:");
                } else if (out != null) {
                    out.write(CHECK_BYTES);
                }
                scanner.nextLine();
            }
        }
        return number;
    }

    private boolean checkWin() {
        for (int i = 0; i < field.size(); i=i+3)
            if (field.get(i) == field.get(i+1) && field.get(i+1) == field.get(i+2) && field.get(i) == field.get(i+2))
                return true;
        for (int i = 0; i < field.size() / 3; i++)
            if (field.get(i) == field.get(i + 3) && field.get(i + 3) == field.get(i + 6) && field.get(i) == field.get(i + 6))
                return true;
        if ((field.get(0) == field.get(4) && field.get(4) == field.get(8) && field.get(0) == field.get(8))
            || (field.get(2) == field.get(4) && field.get(4) == field.get(6) && field.get(2) == field.get(6))
        )
            return true;
        return false;
    }

    private void changeField(int in) {
        field.set(in-1,current);
        if (current == Values.CROSS) current = Values.CIRCLE;
        else current = Values.CROSS;
    }

    private void startBoard() {
        field = new ArrayList<>(9);
        field.add(Values.ONE);
        field.add(Values.TWO);
        field.add(Values.THREE);
        field.add(Values.FOUR);
        field.add(Values.FIVE);
        field.add(Values.SIX);
        field.add(Values.SEVEN);
        field.add(Values.EIGHT);
        field.add(Values.NINE);
    }

    private void showBoard() {
        System.out.println(getBoardToString());
    }

    private String getBoardToString() {
        StringBuilder sb = new StringBuilder().append("\n\r").append(Values.LINE.getString(server)).append("\n\r");
        for (int i = 0; i < field.size(); i++) {
            sb.append("|").append(field.get(i).getString(server));
            if ((i + 1) % 3 == 0)
                sb.append("|").append("\n\r").append(Values.LINE.getString(server)).append("\n\r");
        }
        return sb.toString();
    }
}