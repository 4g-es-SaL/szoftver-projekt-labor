
import java.io.File;
import java.util.Scanner;

/**
 * Created by matech on 2017. 02. 20..
 */
public class Program {

    /**
     *
     */
    protected Playground playground;

    /**
     * MAP:
     *              0      sw|6|
     *          ________  ________
     *         |      /           |
     *     5   |    /             |   1
     *         |  /  (tunnel)     |
     *          /
     *         |                  |
     *  sw|7|  |                  |   2
     *         |                  |
     *          _______  _________
     *             4         3
     */


    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO implement here
        Program p = new Program();
        Scanner scanner = new Scanner(System.in);
        int userInput = -1;

        while (userInput != 0) {
//            printInfo();
            userInput = scanner.nextInt();
            switch (userInput) {
                case 1:
                    p.initialization();
                    break;
                case 2:
                    p.ATrainMoves();
            }
        }

//        System.out.println(playground1);
//
//        playground1.buildTunnelEnd(0);
//        playground1.buildTunnelEnd(1);
//        for (int i = 0; i < 20; i++) {
//            playground1.runTurn();
//            if(i == 11)
//                playground1.changeSwitch(1);
//        }

    }

    private static void printInfo() {
        System.out.println("A pályán két vonat található.\n" +
                "“A” vonat 1 mozdonyból és 3 kocsiból áll. A kocsik színe a vonat elejéről: kék, piros, kék. Sebessége 1.\n" +
                "“B” vonat 1 mozdonyból áll. Sebessége 2.\n" +
                "Válasszon jelenetet:\n" +
                "0 - Kilépés\n" +
                "1 - Inicializáció\n" +
                "2 - “A” előre halad.\n" +
                "3 - “B” előre halad.\n" +
                "4 - Váltó átállítása.\n" +
                "5 - “B” vonat áthaladása váltón.\n" +
                "6 - Alagút építés.\n" +
                "7 - “B” vonat áthaladása alagúton.\n" +
                "8 - Alagút lebontása.\n" +
                "9 - “A” áthalad egy piros színű állomáson miután már egyszer áthaladt kéken.\n" +
                "10 - “A” áthalad a kék színű állomáson, mikor még egy kocsija sem ürült.\n" +
                "11 - Ütközés\n");
    }

    private void initialization() {
        File f = new File("map.txt");
        playground = new Playground(f);
    }

    private void ATrainMoves() {
        initialization();
        playground.initializeA();
        playground.runTurn();
    }
}