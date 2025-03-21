import java.util.Random;
import java.util.Scanner;

class SnakeAndLadderGame {
    static final int BOARD_SIZE = 100;
    static final int[] snakePositions = { 99, 70, 52, 25 }; // Example snake positions
    static final int[] snakeEnds = { 10, 50, 30, 5 }; // Snake tails
    static final int[] ladderPositions = { 3, 22, 58, 75 }; // Example ladder positions
    static final int[] ladderEnds = { 25, 41, 77, 95 }; // Ladder tops
    static Scanner sc = new Scanner(System.in);

    static class Player {
        String name;
        int position;
        boolean hasStarted;
        boolean hasWon;

        Player(String name) {
            this.name = name;
            this.position = 1; // Initial position
            this.hasStarted = false;
            this.hasWon = false;
        }
    }

    public static void main(String[] args) {
        System.out.println("Enter the number of players (Max 6): ");
        int numPlayers = sc.nextInt();
        sc.nextLine(); // Consume newline

        if (numPlayers > 6 || numPlayers < 1) {
            System.out.println("Number of players should be between 1 and 6.");
            return;
        }

        Player[] players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            System.out.println("Enter name for player " + (i + 1) + ": ");
            players[i] = new Player(sc.nextLine());
        }

        playGame(players);
    }

    public static void playGame(Player[] players) {
        Random dice = new Random();
        boolean gameWon = false;
        int turn = 0;
        int[] playerRankings = new int[players.length]; // To store ranks
        int rankCount = 0;
        int playersInGame = players.length; // Track how many players are still playing

        while (playersInGame > 1) {
            Player currentPlayer = players[turn];

            if (!currentPlayer.hasWon) {
                System.out.println(currentPlayer.name + "'s turn. Press Enter to roll the dice.");
                sc.nextLine();
                int diceRoll = dice.nextInt(6) + 1;
                System.out.println(currentPlayer.name + " rolled a " + diceRoll);

                if (!currentPlayer.hasStarted) {
                    if (diceRoll == 1 || diceRoll == 6) {
                        currentPlayer.hasStarted = true;
                        System.out.println(currentPlayer.name + " has started the game!");
                    } else {
                        System.out.println(currentPlayer.name + " needs a 1 or 6 to start.");
                    }
                } else {
                    movePlayer(currentPlayer, diceRoll);
                    if (currentPlayer.position == BOARD_SIZE) {
                        System.out.println(currentPlayer.name + " has reached 100!");
                        currentPlayer.hasWon = true;
                        playerRankings[rankCount++] = turn; // Record the rank of the player
                        playersInGame--; // Reduce the count of active players
                        if (playersInGame == 1) {
                            break; // If only one player is left, end the game
                        }
                    }
                }
            }

            turn = (turn + 1) % players.length;
        }

        // Rank the last player as the loser
        for (int i = 0; i < players.length; i++) {
            if (!players[i].hasWon) {
                playerRankings[rankCount] = i; // The last player who didn't reach 100 is the loser
            }
        }

        announceResults(players, playerRankings);
    }

    public static void movePlayer(Player player, int diceRoll) {
        player.position += diceRoll;

        // Ensure player doesn't exceed 100
        if (player.position > BOARD_SIZE) {
            player.position -= diceRoll; // Revert the move if it exceeds 100
            System.out.println(player.name + " rolled too high and stays at position " + player.position);
            return;
        }

        // Check for snakes or ladders
        for (int i = 0; i < snakePositions.length; i++) {
            if (player.position == snakePositions[i]) {
                System.out.println(player.name + " got bitten by a snake! Moved down to " + snakeEnds[i]);
                player.position = snakeEnds[i];
            }
        }

        for (int i = 0; i < ladderPositions.length; i++) {
            if (player.position == ladderPositions[i]) {
                System.out.println(player.name + " climbed a ladder! Moved up to " + ladderEnds[i]);
                player.position = ladderEnds[i];
            }
        }

        System.out.println(player.name + " is now at position " + player.position);
    }

    public static void announceResults(Player[] players, int[] playerRankings) {
        System.out.println("\nGame Over! Here are the final rankings:");
        for (int i = 0; i < playerRankings.length - 1; i++) {
            if (i == 0) {
                System.out.println(players[playerRankings[i]].name + " is the Winner!");
            } else {
                System.out.println(players[playerRankings[i]].name + " is Rank " + (i + 1));
            }
        }
        System.out.println(players[playerRankings[playerRankings.length - 1]].name + " is the Looser.");
    }
}
