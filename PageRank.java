import java.util.*;

public class PageRank {
    private static float dampingFactor = 0.85f;
    private static int iterationLimit = 1000;
    private static float errorLimit = 0.00f;
    private static PageRankGraph graph;

    static private class PageRankGraph {
        private int pageCount;
        private HashMap<Integer, Page> adjList;

        public PageRankGraph() {
            this.pageCount = 0;
            this.adjList = new HashMap<>();
        }

        // Run page rank algorithm for a given amount of iterations
        public void PageRank(int iteration) {
            int maxItr = iteration;

            // Initialize page rank of each page to 1/N
            Set<Map.Entry<Integer, Page>> entrySet = adjList.entrySet();;
            Iterator<Map.Entry<Integer, Page>> itr = entrySet.iterator();
            while(itr.hasNext()) {
                Map.Entry<Integer, Page> currPageEntry = itr.next();
                Page currPage = currPageEntry.getValue();
                currPage.setTempPageRank(1.0f/pageCount);
                currPage.setPageRank();
                // Add links from sink node to all nodes
                if(currPage.outLinkCount == 0)
                {
                    Set<Map.Entry<Integer, Page>> pageSet = adjList.entrySet();;
                    Iterator<Map.Entry<Integer, Page>> pageItr = pageSet.iterator();
                    while(pageItr.hasNext()) {
                        Map.Entry<Integer, Page> pageOutEntry = pageItr.next();
                        Page currOutPage = pageOutEntry.getValue();
                        if(currOutPage.id != currPage.id)
                            graph.addLink(currPage.id, currOutPage.id);
                    }
                }
            }
            //System.out.println("Executing Page Rank Algorithm...");
            while(iteration > 0) {
                Set<Map.Entry<Integer, Page>> itrSet = adjList.entrySet();;
                Iterator<Map.Entry<Integer, Page>> prItr = itrSet.iterator();
                double totalChange = 0.0d;
                while(prItr.hasNext()) {
                    Map.Entry<Integer, Page> currPageEntry = prItr.next();
                    Page currPage = currPageEntry.getValue();
                    double pr = 0.00d;
                    for(int i = 0; i < currPage.inLinkCount; i++) {
                        Page currInLink = currPage.inLinks.get(i);
                        pr += (currInLink.getPageRank() / currInLink.outLinkCount);
                    }
                    currPage.setTempPageRank(((1-dampingFactor)/pageCount)+(pr*dampingFactor));
                    totalChange += Math.abs(currPage.pageRank-currPage.tempPageRank);
                }
                if(totalChange <= (pageCount*errorLimit)) {
                    graph.commitPageRanks();
                    break;
                }
                graph.commitPageRanks();
                iteration--;
            }
            // System.out.println("Finished at iteration " + (maxItr - iteration) +"! Print graph to see results.");
        }


        public void printLinks()
        {
            System.out.println("Total Pages : " + pageCount);
            for(Map.Entry<Integer, Page> k : adjList.entrySet()) {
                Page currPage = k.getValue();
                System.out.print("Page " + currPage.id + " (PR: " + currPage.getPageRank() + "): ");
                System.out.println(currPage.outLinks.toString());
            }
        }

        public void addLink(int src, int dest) {
            if(!adjList.containsKey(src)) {
                Page newPage = new Page(src);
                adjList.put(src, newPage);
                pageCount++;
            }
            if(!adjList.containsKey(dest)) {
                Page newDestPage = new Page(dest);
                adjList.put(dest, newDestPage);
                pageCount++;
            }

            adjList.get(src).addOutLink(adjList.get(dest));
            adjList.get(dest).addInLink(adjList.get(src));
        }

        public void addPage(int src) {
            if(!adjList.containsKey(src)) {
                Page newPage = new Page(src);
                adjList.put(src, newPage);
                pageCount++;
            }
        }

        public void commitPageRanks() {
            Set<Map.Entry<Integer, Page>> entrySet = adjList.entrySet();;
            Iterator<Map.Entry<Integer, Page>> itr = entrySet.iterator();
            while(itr.hasNext()) {
                Map.Entry<Integer, Page> currPageEntry = itr.next();
                Page currPage = currPageEntry.getValue();
                currPage.setPageRank();
            }
        }

        class Page {
            private int id;
            private double pageRank;
            private double tempPageRank;
            private ArrayList<Page> inLinks;
            private ArrayList<Page> outLinks;
            private int outLinkCount;
            private int inLinkCount;

            public Page(int id){
                this.id = id;
                this.inLinks = new ArrayList<>();
                this.outLinks = new ArrayList<>();
                this.outLinkCount = 0;
                this.inLinkCount = 0;
            }


            public void setPageRank() {
                this.pageRank = this.tempPageRank;
            }

            public void addInLink(Page src) {
                inLinks.add(src);
                inLinkCount++;
            }

            public void addOutLink(Page dest) {
                outLinks.add(dest);
                outLinkCount++;
            }

            public String toString() {
                return String.valueOf(id);
            }

            public void setTempPageRank(double pr) {
                this.tempPageRank = pr;
            }

            public double getPageRank() {
                return pageRank;
            }
        }
    }


    public static void main(String args[]) {
        graph = new PageRankGraph();
        /*
        graph.addLink(1, 2);
        graph.addLink(2, 3);
        graph.addLink(3, 4);
        graph.addLink(3, 5);
        graph.addLink(4, 2);
        */
        graph.addLink(0, 1);
        graph.addLink(1, 0);
        graph.addLink(1, 2);
        graph.addLink(1, 3);
        graph.addLink(2, 0);
        graph.addLink(3, 0);
        graph.addLink(3, 1);
        graph.addLink(3, 2);


        Scanner input = new Scanner(System.in);

        while(true) {
            System.out.println("Page Rank Algorithm");
            System.out.println("Damping Factor: " + dampingFactor
                    + " Iteration Limit: " + iterationLimit
                    + " Error Limit: " + errorLimit);
            System.out.println();
            System.out.println("Select an option:");
            System.out.println("1. Print current graph");
            System.out.println("2. Input a graph manually");
            System.out.println("3. Generate a graph");
            System.out.println("4. Change page rank parameters");
            System.out.println("5. Run Page Rank");
            System.out.println("6. Exit");
            int command = input.nextInt();
            switch (command) {
                case 1:
                    graph.printLinks();
                    break;
                case 2:
                    manualGraphInput();
                    break;
                case 3:
                    System.out.print("Number of pages: ");
                    int pageCount = input.nextInt();
                    System.out.print("Outdegree % (# from 0 to 100): ");
                    int outDegree = input.nextInt();
                    generateGraph(pageCount, outDegree);
                    break;
                case 4:
                    changeParameters();
                    break;
                case 5:
                    runPageRank(iterationLimit);
                    System.out.println("Page rank complete, print current graph to see results");
                    break;
                case 6:
                    System.exit(0);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + command);
            }
        }

    }

    public static void manualGraphInput() {
        graph = new PageRankGraph();
        System.out.print("Number of edges: ");
        Scanner input = new Scanner(System.in);
        int edgeCount = input.nextInt();
        int src, dest;
        System.out.println("Enter first the source of the edge, then enter the destination. (Pages can be any number >= 0)");
        for(int i = 0; i < edgeCount; i++) {
            System.out.print("Edge " + i + " from : ");
            src = input.nextInt();
            System.out.print("To : ");
            dest = input.nextInt();
            graph.addLink(src, dest);
        }
    }

    public static void generateGraph(int pageCount, int outDegree) {
        graph = new PageRankGraph();
        float outDegreePercent = outDegree/100.0f;
        for(int i = 0; i < pageCount; i++) {
            graph.addPage(i);
            int linkCount = 0;
            for(int j = 0; j < pageCount; j++) {
                if(Math.random() <= outDegreePercent && i != j) {
                    linkCount++;
                    graph.addLink(i, j);
                    if(linkCount >= outDegreePercent*(float)pageCount) {
                        break;
                    }
                }
            }
        }
    }

    public static void runPageRank(int iterationLimit) {
        graph.PageRank(iterationLimit);
    }


    public static void changeParameters() {
        System.out.print("Damping factor (0.00 to 1.00): ");
        Scanner input = new Scanner(System.in);
        dampingFactor = input.nextFloat();
        System.out.print("Iteration limit: ");
        iterationLimit = input.nextInt();
        System.out.print("Error limit: ");
        errorLimit = input.nextFloat();
        System.out.println();
    }

    public static float getDampingFactor() {
        return dampingFactor;
    }

    public void setDampingFactor(float dampingFactor) {
        this.dampingFactor = dampingFactor;
    }

    public int getIterationLimit() {
        return iterationLimit;
    }

    public void setIterationLimit(int iterationLimit) {
        this.iterationLimit = iterationLimit;
    }

    public float getErrorLimit() {
        return errorLimit;
    }

    public void setErrorLimit(float errorLimit) {
        this.errorLimit = errorLimit;
    }
}