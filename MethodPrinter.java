/**
 * Created by matech on 2017. 03. 15..
 */
public class MethodPrinter {
    private static int tabCount;
    public MethodPrinter() {
        tabCount = -1;
    }

    public static void enterMethod() {
        tabCount++;
        printStackInfo('>');
    }

    public static void leaveMethod() {
        printStackInfo('<');
        tabCount--;
    }

    private static void printStackInfo(char sign) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement element = stackTraceElements[3];
        for (int i = 0; i < tabCount; i++) {
            System.out.print("\t");
        }
        System.out.println(sign + "[" + element.getClassName() + "]." + element.getMethodName());
    }
}
