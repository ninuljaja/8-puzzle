import java.util.*;

public class Main {
    // Define the goal state as global variable
    static int[][] goal = {
            {1,2,3},
            {8,0,4},
            {7,6,5}
    };

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        String input = "";
        int[][] initial = new int[3][3];
        int xBK = -1;
        int yBK = -1;
        while(true){
            // Display menu for user input
            System.out.println("""
                    \nChoose an option:
                    1. Enter initial state
                    2. Perform DFS
                    3. Perform UCS
                    4. Perform BFS
                    5. Perform A* search (Nilsson’s sequence)
                    6. Quit
                    """);
            input = scan.nextLine();
            System.out.println();

            switch (input){
                case "1": //Enter initial state
                    xBK = -1;
                    yBK = -1;
                    ArrayList<Integer> numbers = new ArrayList<>();
                    int num;
                    // Prompt user to input initial state
                    for(int i = 0; i < initial.length; i++){
                        for (int j = 0; j < initial[i].length; j++){
                            while(true){
                                try{
                                    System.out.print("Enter number in row " + (i+1) + ", column " + (j+1) + ": ");
                                    num = scan.nextInt();

                                    // Validate user input
                                    if(num < 0 || num > 8){
                                        System.out.println("\nInvalid input. Enter an integer between 0 and 8");
                                    } else if(numbers.contains(num)){
                                        System.out.println("\nThis number is already in the array. Enter different number between 0 and 8");
                                    } else {
                                        initial[i][j] = num;
                                        numbers.add(num);
                                        break;
                                    }
                                }catch (InputMismatchException e){
                                    System.out.println("\nInvalid input. Enter an integer between 0 and 8");
                                    scan.nextLine();
                                }
                            }
                        }
                    }
                    // Find the position of the blank tile
                    for(int i = 0; i < initial.length; i++){
                        for(int j = 0; j < initial[0].length; j++){
                            if(initial[i][j] == 0){
                                xBK = i;
                                yBK = j;
                            }
                        }
                    }
                    scan.nextLine();
                    // Display the initial state
                    System.out.println("Initial state:");
                    printState(initial);
                    break;
                case "2": //Perform DFS
                    if(xBK != -1) {
                        System.out.println("Initial state:");
                        printState(initial);

                        System.out.println("Goal:");
                        printState(goal);

                        System.out.println("DFS solution:\n");
                        DFS(initial, xBK, yBK);
                    } else {
                        System.out.println("Enter initial state first");
                    }
                    break;
                case "3": //Perform UCS
                    if(xBK != -1){
                        System.out.println("Initial state:");
                        printState(initial);

                        System.out.println("Goal:");
                        printState(goal);

                        System.out.println("UCS solution:\n");
                        UCS(initial, xBK, yBK);
                    } else {
                        System.out.println("Enter initial state first");
                    }
                    break;
                case "4": //Perform BFS
                    if(xBK != -1){
                        System.out.println("Initial state:");
                        printState(initial);

                        System.out.println("Goal:");
                        printState(goal);

                        System.out.println("BFS solution:\n");
                        BFS(initial, xBK, yBK);
                    } else {
                        System.out.println("Enter initial state first");
                    }
                    break;
                case "5": //Perform A* using Nilsson's sequence score
                    if(xBK != -1){
                        System.out.println("Initial state:");
                        printState(initial);

                        System.out.println("Goal:");
                        printState(goal);

                        System.out.println("A* (Nilsson's sequence) solution:\n");
                        aStar(initial, xBK, yBK);
                    } else {
                        System.out.println("Enter initial state first");
                    }
                    break;
                case "6": //Exit
                    System.exit(0);
                default:
                    System.out.println("Invalid input. Try again");
            }
        }
    }

    /**
     * Method that prints the current state of the puzzle
     * @param state as 2D array of integers
     */
    public static void printState(int [][] state){
        for (int i = 0; i < state.length; i++){
            for (int j = 0; j < state[0].length; j++){
                System.out.print(state[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Method that checks if the current state is the goal state
     * @param state as 2D array of integers
     * @return boolean
     */
    public static boolean isGoalState(int [][] state){
        for(int i = 0; i < state.length; i++){
            for (int j = 0; j < state[0].length; j++){
                if(state[i][j] != goal[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * prints the path from the initial state to the current state
     * @param state as 2D array of integers
     */
    public static void printPath(Node state){
        Stack<Node> path = new Stack<>();

        while(state != null){
            path.push(state);
            state = state.getParent();
        }
        int pathLength = path.size();
        System.out.println("Solution:\n");
        while (!path.isEmpty()) {
            printState(path.pop().getState());
        }
        System.out.println("Path length: " + pathLength);

    }

    /**
     * Method that finds and returns the next state for DFS
     * @param state as 2D array
     * @param visited set of visited states
     * @param level level of next state
     * @return a Node that represents the next state
     */
    public static Node moveDFS(Node state, HashSet<String> visited, int level){
        int[][] newState = new int[3][3];
        for (int i = 0; i < state.getState().length; i++) {
            newState[i] = Arrays.copyOf(state.getState()[i], state.getState()[i].length);
        }
        // move up
        if(state.getBlankX() != 0){ //if blank tile not in the first row
            newState[state.getBlankX()][state.getBlankY()] = newState[state.getBlankX()-1][state.getBlankY()];
            newState[state.getBlankX()-1][state.getBlankY()] = 0;
            // check if the node is already visited
            if (!visited.contains(Arrays.deepToString(newState))){
                return new Node(newState,state,state.getBlankX()-1,state.getBlankY(),level);
            }
            newState[state.getBlankX()-1][state.getBlankY()] = newState[state.getBlankX()][state.getBlankY()];
            newState[state.getBlankX()][state.getBlankY()] = 0;

        }
        // move left
        if(state.getBlankY() != 0){ //if blank tile not in the first column
            newState[state.getBlankX()][state.getBlankY()] = newState[state.getBlankX()][state.getBlankY()-1];
            newState[state.getBlankX()][state.getBlankY()-1] = 0;
            // check if the node is already visited
            if (!visited.contains(Arrays.deepToString(newState))){
                return new Node(newState,state,state.getBlankX(),state.getBlankY()-1, level);
            }
            newState[state.getBlankX()][state.getBlankY()-1] = newState[state.getBlankX()][state.getBlankY()];
            newState[state.getBlankX()][state.getBlankY()] = 0;

        }
        //move down
        if(state.getBlankX() != 2){ //if blank tile not in the last row
            newState[state.getBlankX()][state.getBlankY()] = newState[state.getBlankX()+1][state.getBlankY()];
            newState[state.getBlankX()+1][state.getBlankY()] = 0;
            // check if the node is already visited
            if (!visited.contains(Arrays.deepToString(newState))) {
                return new Node(newState, state, state.getBlankX() + 1, state.getBlankY(), level);
            }
            newState[state.getBlankX()+1][state.getBlankY()] = newState[state.getBlankX()][state.getBlankY()];
            newState[state.getBlankX()][state.getBlankY()] = 0;
        }
        // move right
        if(state.getBlankY() != 2){ //if blank tile not in the last column
            newState[state.getBlankX()][state.getBlankY()] = newState[state.getBlankX()][state.getBlankY()+1];
            newState[state.getBlankX()][state.getBlankY()+1] = 0;
            // check if the node is already visited
            if (!visited.contains(Arrays.deepToString(newState))){
                return new Node(newState,state,state.getBlankX(),state.getBlankY()+1,level);
            }
            newState[state.getBlankX()][state.getBlankY()+1] = newState[state.getBlankX()][state.getBlankY()] ;
            newState[state.getBlankX()][state.getBlankY()] = 0;
        }
         return null; //if all next possible states already visited
    }

    /**
     * Method that expands current state and adds next possible states to the queue
     * @param state as 2D array
     * @param visited set of visited states
     * @param level level of next state
     * @param queue queue with next possible states
     */
    public static void move(Node state, HashSet<String> visited, int level, Queue<Node> queue){
        int[][] newState = new int[3][3];
        int manhattanDistance;
        int misplacedTiles;
        int outOfOrder;
        int nilssonSequence;
        for (int i = 0; i < state.getState().length; i++) {
            newState[i] = Arrays.copyOf(state.getState()[i], state.getState()[i].length);
        }
        // move up
        if(state.getBlankX() != 0){ //if blank tile not in the first row
            newState[state.getBlankX()][state.getBlankY()] = newState[state.getBlankX()-1][state.getBlankY()];
            newState[state.getBlankX()-1][state.getBlankY()] = 0;
            // check if the node is already visited
            if (!visited.contains(Arrays.deepToString(newState))){
                //calculate heuristics, create next node, and add to the queue
                misplacedTiles = findNumberOfMisplacedItems(newState);
                manhattanDistance = findManhattanDistance(newState);
                outOfOrder = 2*findSuccessorsOutOfOrder(newState);
                //check if there is a tile in the middle
                if (newState[1][1] != 0){
                    outOfOrder++;
                }
                nilssonSequence = manhattanDistance + 3*outOfOrder;
                queue.add(new Node(newState,state,state.getBlankX()-1,state.getBlankY(),level, misplacedTiles, nilssonSequence));
            }
        }
        newState = new int[3][3];
        for (int i = 0; i < state.getState().length; i++) {
            newState[i] = Arrays.copyOf(state.getState()[i], state.getState()[i].length);
        }
        // move left
        if(state.getBlankY() != 0){ //if blank tile not in the first column
            newState[state.getBlankX()][state.getBlankY()] = newState[state.getBlankX()][state.getBlankY()-1];
            newState[state.getBlankX()][state.getBlankY()-1] = 0;
            // check if the node is already visited
            if (!visited.contains(Arrays.deepToString(newState))){
                //calculate heuristics, create next node, and add to the queue
                misplacedTiles = findNumberOfMisplacedItems(newState);
                manhattanDistance = findManhattanDistance(newState);
                outOfOrder = 2*findSuccessorsOutOfOrder(newState);
                //check if there is a tile in the middle
                if (newState[1][1] != 0){
                    outOfOrder++;
                }
                nilssonSequence = manhattanDistance + 3*outOfOrder;
                queue.add(new Node(newState,state,state.getBlankX(),state.getBlankY()-1, level, misplacedTiles, nilssonSequence));
            }
        }
        newState = new int[3][3];
        for (int i = 0; i < state.getState().length; i++) {
            newState[i] = Arrays.copyOf(state.getState()[i], state.getState()[i].length);
        }
        //move down
        if(state.getBlankX() != 2){  //if blank tile not in the last row
            newState[state.getBlankX()][state.getBlankY()] = newState[state.getBlankX()+1][state.getBlankY()];
            newState[state.getBlankX()+1][state.getBlankY()] = 0;
            // check if the node is already visited
            if (!visited.contains(Arrays.deepToString(newState))) {
                //calculate heuristics, create next node, and add to the queue
                misplacedTiles = findNumberOfMisplacedItems(newState);
                manhattanDistance = findManhattanDistance(newState);
                outOfOrder = 2*findSuccessorsOutOfOrder(newState);
                //check if there is a tile in the middle
                if (newState[1][1] != 0){
                    outOfOrder++;
                }
                nilssonSequence = manhattanDistance + 3*outOfOrder;
                queue.add(new Node(newState, state, state.getBlankX() + 1, state.getBlankY(), level, misplacedTiles, nilssonSequence));
            }
        }
        newState = new int[3][3];
        for (int i = 0; i < state.getState().length; i++) {
            newState[i] = Arrays.copyOf(state.getState()[i], state.getState()[i].length);
        }
        // move right
        if(state.getBlankY() != 2){  //if blank tile not in the last column
            newState[state.getBlankX()][state.getBlankY()] = newState[state.getBlankX()][state.getBlankY()+1];
            newState[state.getBlankX()][state.getBlankY()+1] = 0;
            // check if the node is already visited
            if (!visited.contains(Arrays.deepToString(newState))){
                //calculate heuristics, create next node, and add to the queue
                misplacedTiles = findNumberOfMisplacedItems(newState);
                manhattanDistance = findManhattanDistance(newState);
                outOfOrder = 2*findSuccessorsOutOfOrder(newState);
                //check if there is a tile in the middle
                if (newState[1][1] != 0){
                    outOfOrder++;
                }
                nilssonSequence = manhattanDistance + 3*outOfOrder;
                queue.add(new Node(newState,state,state.getBlankX(),state.getBlankY()+1,level, misplacedTiles, nilssonSequence));
            }
        }
    }

    /**
     * Method that performs DFS algorith
     * @param initial state as 2D array
     * @param xBK index of the row of blank tile
     * @param yBK index of the column of blank tile
     */
    public static void DFS(int [][] initial, int xBK, int yBK){
        Stack<Node> stack = new Stack<>();
        HashSet<String> visited = new HashSet<>();
        //create initial node
        Node root = new Node(initial, null, xBK,yBK, 0);
        stack.push(root);
        visited.add(Arrays.deepToString(root.getState()));
        //check if the current state is a goal
        if(isGoalState(root.getState())){
            printPath(root);
            System.out.println("Number of visited nodes: " + visited.size());
            System.out.println("Level of the goal node: " + root.getState());
            return;
        }

        while (!stack.isEmpty()){
            Node currentState = stack.peek();
            //find next state
            Node newState = moveDFS(currentState, visited, currentState.getLevel()+1);
            if(newState != null) {
                visited.add(Arrays.deepToString(newState.getState()));
                //check if the new state is a goal
                if(isGoalState(newState.getState())){
                    printPath(newState);
                    System.out.println("Number of visited nodes (DFS): " + visited.size());
                    System.out.println("Level of the goal node (DFS): " + newState.getLevel());
                    return;
                }
                stack.push(newState);
            } else {
                stack.pop();
            }
        }
        System.out.println("Number of visited notes (DFS): " + visited.size());
        System.out.println("No solution found.");

    }

    /**
     * Method that performs UCS algorithm (expands the node with the lowest path cost g(n))
     * @param initial state as 2D array
     * @param xBK index of the row of blank tile
     * @param yBK index of the column of blank tile
     */
    public static void UCS(int [][] initial, int xBK, int yBK){
        Queue<Node> queue = new LinkedList <>();
        HashSet<String> visited = new HashSet<>();
        //create initial node
        Node root = new Node(initial, null, xBK,yBK,0);
        queue.add(root);

        while (!queue.isEmpty()){
            Node currentState = queue.poll();
            if (!visited.contains(Arrays.deepToString(currentState.getState()))) {
                visited.add(Arrays.deepToString(currentState.getState()));
                //check if the current state is a goal
                if (isGoalState(currentState.getState())) {
                    printPath(currentState);
                    System.out.println("Number of visited nodes (UCS): " + visited.size());
                    System.out.println("Level of the goal node (UCS): " + currentState.getLevel());
                    return;
                }
                //expand current state
                move(currentState, visited, currentState.getLevel() + 1, queue);
            }
        }
        System.out.println("Number of visited notes (UCS): " + visited.size());
        System.out.println("No solution found.");
    }

    /**
     * Method that performs BFS algorithm
     * @param initial state as 2D array
     * @param xBK index of the row of blank tile
     * @param yBK index of the column of blank tile
     */
    public static void BFS(int [][] initial, int xBK, int yBK){
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(Node::getNumOfMisplacesTiles));
        HashSet<String> visited = new HashSet<>();
        int misplacedTiles = findNumberOfMisplacedItems(initial);
        Node root = new Node(initial, null, xBK,yBK,0, misplacedTiles);
        pq.add(root);

        while (!pq.isEmpty()){
            Node currentState = pq.poll();
            if (!visited.contains(Arrays.deepToString(currentState.getState()))) {
                visited.add(Arrays.deepToString(currentState.getState()));
                //check if the current state is a goal
                if (isGoalState(currentState.getState())) {
                    printPath(currentState);
                    System.out.println("Number of visited nodes (BFS): " + visited.size());
                    System.out.println("Level of the goal node (BFS): " + currentState.getLevel());
                    return;
                }
                //expand current state
                move(currentState, visited, currentState.getLevel() + 1, pq);
            }

        }
        System.out.println("Number of visited notes: " + visited.size());
        System.out.println("No solution found.");

    }

    /**
     * Method that performs A* using Nilsson's sequence algorithm
     * @param initial state as 2D array
     * @param xBK index of the row of blank tile
     * @param yBK index of the column of blank tile
     */
    public static void aStar(int [][] initial, int xBK, int yBK){
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(Node::getTotalCost));
        HashSet<String> visited = new HashSet<>();
        int misplacedTiles = findNumberOfMisplacedItems(initial);
        int manhattanDistance = findManhattanDistance(initial);
        int outOfOrder = 2*findSuccessorsOutOfOrder(initial);
        if (initial[1][1] != 0){
            outOfOrder++;
        }
        int nilssonSequence = manhattanDistance + 3*outOfOrder;


        Node root = new Node(initial, null, xBK,yBK,0, misplacedTiles, nilssonSequence);
        pq.add(root);

        while (!pq.isEmpty()){
            Node currentState = pq.poll();
            if (!visited.contains(Arrays.deepToString(currentState.getState()))) {
                visited.add(Arrays.deepToString(currentState.getState()));
                //check if the current state is a goal
                if (isGoalState(currentState.getState())) {
                    printPath(currentState);
                    System.out.println("Number of visited nodes (A*): " + visited.size());
                    System.out.println("Level of the goal node (A*): " + currentState.getLevel());
                    return;
                }
                //expand current state
                move(currentState, visited, currentState.getLevel() + 1, pq);
            }
        }

        System.out.println("Number of visited notes (A*): " + visited.size());
        System.out.println("No solution found.");

    }

    /**
     * Method that finds the number of misplaced tiles
     * @param state as 2D array
     * @return integer that represents the number of misplaces tiles
     */
    public static int findNumberOfMisplacedItems(int [][] state){
        int count = 0;
        for (int i = 0; i < state.length; i++){
            for (int j = 0; j < state[0].length; j++){
                if(state[i][j] != 0 && state[i][j] != goal[i][j]){
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Method that calculates the Manhattans distance
     * @param state as 2D array
     * @return integer that represents the Manhattans distance
     */
    public static int findManhattanDistance(int [][] state){
        int distance = 0;
        for (int i = 0; i < state.length; i++){
            for (int j = 0; j < state[0].length; j++){
                if(state[i][j] != 0 && state[i][j] != goal[i][j]){
                    for(int r = 0; r < goal.length; r++){
                        for (int c = 0; c < goal[0].length; c++){
                            if(state[i][j] == goal[r][c]){
                                distance += Math.abs(i-r) + Math.abs(j-c);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return distance;
    }

    /**
     * Method that fines the number of tiles out of order with the neighbor (clockwise) for Nillson's sequence
     * @param state as 2D array
     * @return integer that represents the number of tiles out of order
     */
    public static int findSuccessorsOutOfOrder(int [][] state){
        int numMisplace = 0;
        int[] state1D = {state[0][0],state[0][1],state[0][2],state[1][2],state[2][2],state[2][1],state[2][0],state[1][0]};
        for (int i = 0; i < state1D.length; i++){
            if(state1D[i] != 0 && (state1D[i]+1) != state1D[(i+1)%8]){
                numMisplace++;
            }
        }
        return numMisplace;
    }
}

/**
 * class Node that defines a state
 */
class Node {
    private Node parent;
    private int[][] state;
    private int level;
    private int blankX, blankY;
    private int aStarHeuristic = 0;
    private int totalCost = 0;
    private int numOfMisplacesTiles;

    public Node(int[][] state, Node parent, int blankX, int blankY, int level){
        this.state = state;
        this.level = level;
        this.parent = parent;
        this.blankX = blankX;
        this.blankY = blankY;

    }
    public Node(int[][] state, Node parent, int blankX, int blankY, int level, int numOfMisplacesTiles){
        this.state = state;
        this.level = level;
        this.parent = parent;
        this.blankX = blankX;
        this.blankY = blankY;
        this.numOfMisplacesTiles = numOfMisplacesTiles;


    }
    public Node(int[][] state, Node parent, int blankX, int blankY, int level, int numOfMisplacesTiles, int NilsSeq){
        this.state = state;
        this.level = level;
        this.parent = parent;
        this.blankX = blankX;
        this.blankY = blankY;
        this.numOfMisplacesTiles = numOfMisplacesTiles;
        aStarHeuristic = NilsSeq;
        totalCost = level + aStarHeuristic;
    }

    public int[][] getState(){
        return state;
    }

    public Node getParent() {
        return parent;
    }

    public int getLevel() {
        return level;
    }

    public int getBlankX() {
        return blankX;
    }

    public int getBlankY() {
        return blankY;
    }

    public int getNumOfMisplacesTiles() {
        return numOfMisplacesTiles;
    }

    public int getTotalCost() {
        return totalCost;
    }
}