import java.util.Scanner;

public class PageRankPerformanceTester {
    private static PageRank pr;
    private static int pageCountTable[] = { 100, 500, 1000, 2000, 5000, 10000 };
    private static int degreeTable[] = { 20, 40, 60, 80, 90 };
    private static float dampingFactor = 0.85f;
    private static int iterationLimit = 1000;
    private static float errorLimit = 0.00f;

    public static void main(String args[]) {
        pr = new PageRank();
        Scanner input = new Scanner(System.in);
        while(true) {

            System.out.println("Page Rank Algorithm Performance Analyser");
            System.out.println("Damping Factor: " + dampingFactor
                    + " Iteration Limit: " + iterationLimit
                    + " Error Limit: " + errorLimit);
            System.out.println("Select an option:");
            System.out.println("1. Run performance test");
            System.out.println("2. Change page rank parameters");
            System.out.println("3. Exit");
            int command = input.nextInt();

            switch (command) {
                case 1:
                    performance();
                    break;
                case 2:
                    changeParameters();
                    break;
                case 3:
                    System.exit(0);
                default:
                    throw new IllegalStateException("Unexpected value: " + command);
            }
        }
    }

    public static void performance() {
        System.out.print("Maximum Out Degree ");
        for(int i = 0; i < degreeTable.length; i++) {
            System.out.print(degreeTable[i]+", ");
        }
        System.out.println("\b\b");
        for(int i = 0; i < pageCountTable.length; i++) {
            System.out.print("PageCount [" + pageCountTable[i] + "] \t");
            for(int j = 0; j < degreeTable.length; j++) {
                pr.generateGraph(pageCountTable[i], degreeTable[j]);
                long start = System.currentTimeMillis();
                pr.runPageRank(10000);
                long finish = System.currentTimeMillis();
                long timeElapsed = finish - start;
                System.out.print(timeElapsed+" ms, ");
            }
            System.out.println("\b\b");
        }
    }

    public static void changeParameters() {
        System.out.print("Damping factor (0.00 to 1.00): ");
        Scanner input = new Scanner(System.in);
        dampingFactor = input.nextFloat();
        pr.setDampingFactor(dampingFactor);
        System.out.print("Iteration limit: ");
        iterationLimit = input.nextInt();
        pr.setIterationLimit(iterationLimit);
        System.out.print("Error limit:  ");
        errorLimit = input.nextFloat();
        pr.setErrorLimit(errorLimit);
        System.out.println();
    }
}