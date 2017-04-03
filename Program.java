
import java.util.Scanner;

/**
 * Communicates with the user and the {@link Playground}.
 */
public class Program {

    protected Playground playground;

    /**
     * Starts the program.
     * @param args Ignored.
     */
    public static void main(String[] args) {
        // TODO implement here
        Program p = new Program();
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            String userInput = scanner.nextLine();
            String[] userInputSplit = userInput.split(" ");
            try {
                switch (userInputSplit[0]) {
                    case "init":
                        //TODO
                        break;
                    case "switch":
                        //TODO
                        break;
                    case "build":
                        //TODO
                        break;
                    case "destroy":
                        //TODO
                        break;
                    case "passenger":
                        //TODO
                        break;
                    case "move":
                        //TODO
                        break;
                    case "exit":
                        exit = true;
                        break;
                    default:
                        printHelp();
                }
            } catch (Exception e) {
                e.printStackTrace();
                printHelp();
            }
        }
    }

    protected static void printHelp() {
        System.out.println("Your input is not recognizable as an existing command!");
        System.out.println("init <file>\n" +
                "Leírás: beolvassa a megadott file-t és ez alapján felépíti a játékterepet\n" +
                "Opciók: file: a beolvasandó file\n" +
                "\n" +
                "switch <switch id>\n" +
                "Leírás: A <switch id>-val azonosított Switch objektum állását átállítja\n" +
                "Opciók: switch id: az állítandó Switch azonosítója\n" +
                "\n" +
                "build <rail id1> <rail id2>\n" +
                "Leírás: A <rail id1>-val, illetve <rail id2>-vel azonosított Rail objektumok között felépít egy alagutat, ha lehetséges.\n" +
                "Opciók: <rail id1> <rail id2>: A Rail objektumok azonosítói\n" +
                "\n" +
                "destroy <rail id1> <rail id2>\n" +
                "Leírás: A <rail id1>-val, illetve <rail id2>-vel azonosított Rail objektumok között felépített alagutat lebontja, ha az létezik.\n" +
                "Opciók: <rail id1> <rail id2>: A Rail objektumok azonosítói\n" +
                "\n" +
                "passenger <color> <rail id>\n" +
                "Leírás: A <rail id>-val azonosított állomáshoz(ha az állomás) hozzáad color színű utasokat\n" +
                "Opciók: <color>: a felszállítandó utasok színe\n" +
                "\t\t  <rail id>: az állomás azonosítója\n" +
                "move\n" +
                "Leírás: Egy kör lejátszása\n" +
                "Opciók: N/A\n" +
                "exit\n" +
                "Leírás: Kilépés" +
                "Opciók: N/A");
    }
}