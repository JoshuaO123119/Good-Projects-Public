import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Blackjack {
    private static final String[] CARDS = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    private static int tokens = 200;
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    public static void main(String[] args) {
        while (true) {
            mainGame();
            System.out.println("Do you wish to play again? (y/n): ");
            String choice = scanner.nextLine().toLowerCase().trim();
            if (!choice.equals("y") && !choice.equals("yes")) {
                break;
            }
        }
    }

    private static void mainGame() {
        clearConsole();
        while (tokens > 0) {
            clearConsole();
            List<String> humanHand = new ArrayList<>();
            List<String> aiHand = new ArrayList<>();

            for (int i = 0; i < 2; i++) {
                humanHand.add(randomCard());
                aiHand.add(randomCard());
            }

            int humanHandAmount = deckAmount(humanHand);
            int aiHandAmount = deckAmount(aiHand);

            displayHands(humanHand, aiHand, humanHandAmount, aiHandAmount);

            int startBet = getStartingBet();
            tokens -= startBet;

            if (humanHandAmount == 21) {
                if (aiHandAmount == 21) {
                    tokens += startBet;
                    displayHands(humanHand, aiHand, humanHandAmount, aiHandAmount);
                    System.out.println("You both push! +" + startBet + " tokens");
                    waitForInput();
                    continue;
                } else {
                    int gainLoss = (int) Math.round(startBet * 2 * 1.25);
                    tokens += gainLoss;
                    displayHands(humanHand, aiHand, humanHandAmount, aiHandAmount);
                    System.out.println("Blackjack! +" + gainLoss + " tokens");
                    waitForInput();
                    continue;
                }
            }

            boolean running = true;
            boolean doubleBet = false;
            int gainLoss = 0;
            int mult = 1;

            while (running) {
                clearConsole();
                humanHandAmount = deckAmount(humanHand);
                aiHandAmount = deckAmount(aiHand);

                if (humanHandAmount <= 20) {
                    displayGameState(humanHand, aiHand, humanHandAmount, aiHandAmount);

                    String choice = doubleBet ? "3" : getPlayerChoice();

                    if (choice.equals("1") || choice.equals("hit")) {
                        humanHand.add(randomCard());
                    } else if (choice.equals("2") || choice.equals("double")) {
                        if (tokens >= startBet) {
                            humanHand.add(randomCard());
                            doubleBet = true;
                            mult = 2;
                            tokens -= startBet;
                        } else {
                            System.out.println("You don't have enough tokens to double!");
                            waitForInput();
                            continue;
                        }
                    } else if (choice.equals("3") || choice.equals("stand")) {
                        while (aiHandAmount <= 16) {
                            aiHand.add(randomCard());
                            aiHandAmount = deckAmount(aiHand);
                            displayHands(humanHand, aiHand, humanHandAmount, aiHandAmount);
                            sleep(1000);
                        }

                        if (aiHandAmount <= 21) {
                            if (aiHandAmount > humanHandAmount) {
                                gainLoss = startBet * mult;
                                if (doubleBet) {
                                    tokens -= startBet;
                                }
                                displayHands(humanHand, aiHand, humanHandAmount, aiHandAmount);
                                System.out.println("AI wins this round! -" + gainLoss + " tokens");
                                waitForInput();
                                running = false;
                            } else if (aiHandAmount < humanHandAmount) {
                                gainLoss = (startBet * 2) * mult;
                                tokens += gainLoss;
                                displayHands(humanHand, aiHand, humanHandAmount, aiHandAmount);
                                System.out.println("You win this round! +" + gainLoss + " tokens");
                                waitForInput();
                                running = false;
                            } else {
                                gainLoss = startBet * mult;
                                tokens += gainLoss;
                                displayHands(humanHand, aiHand, humanHandAmount, aiHandAmount);
                                System.out.println("Push! +" + gainLoss + " tokens");
                                waitForInput();
                                running = false;
                            }
                        } else {
                            gainLoss = (startBet * 2) * mult;
                            tokens += gainLoss;
                            displayHands(humanHand, aiHand, humanHandAmount, aiHandAmount);
                            System.out.println("You win this round! +" + gainLoss + " tokens");
                            waitForInput();
                            running = false;
                        }
                    }
                } else {
                    gainLoss = startBet * mult;
                    if (doubleBet) {
                        tokens -= startBet;
                    }
                    displayHands(humanHand, aiHand, humanHandAmount, aiHandAmount);
                    System.out.println("AI wins this round! -" + gainLoss + " tokens");
                    waitForInput();
                    running = false;
                }
            }
        }
        System.out.println("Game Over!\nYou ran out of tokens");
    }

    private static void displayGameState(List<String> humanHand, List<String> aiHand, int humanHandAmount, int aiHandAmount) {
        clearConsole();
        System.out.println("Tokens: " + tokens);
        System.out.println("Hand total: " + humanHandAmount);
        System.out.println("Your hand: " + humanHand);
        System.out.println("AI hand: [" + aiHand.get(0) + ", X]");
        System.out.println("Options:\n\t1. Hit\n\t2. Double\n\t3. Stand\n");
    }

    private static String getPlayerChoice() {
        System.out.print("Choice: ");
        return scanner.nextLine().toLowerCase().trim();
    }

    private static int getStartingBet() {
        while (true) {
            clearConsole();
            System.out.println("Tokens: " + tokens);
            System.out.print("Starting bet: ");
            String input = scanner.nextLine().trim();
            if (input.matches("\\d+")) {
                int bet = Integer.parseInt(input);
                if (bet > 0 && bet <= tokens) {
                    return bet;
                } else {
                    System.out.println("You can't bet more tokens than you have or nothing!");
                    waitForInput();
                }
            } else {
                System.out.println("That wasn't a valid integer");
                waitForInput();
            }
        }
    }

    private static void displayHands(List<String> humanHand, List<String> aiHand, int humanHandAmount, int aiHandAmount) {
        clearConsole();
        System.out.println("Tokens: " + tokens);
        System.out.println("Your hand total: " + humanHandAmount);
        System.out.println("Your hand: " + humanHand);
        System.out.println("AI hand total: " + aiHandAmount);
        System.out.println("AI hand: " + aiHand);
    }

    private static int deckAmount(List<String> hand) {
        int handAmount = 0;
        int numAces = 0;

        for (String card : hand) {
            if (card.equals("A")) {
                numAces++;
            }
        }

        for (String card : hand) {
            if (card.equals("A")) {
                handAmount += 11;
            } else if (card.equals("J") || card.equals("Q") || card.equals("K")) {
                handAmount += 10;
            } else {
                handAmount += Integer.parseInt(card);
            }
        }

        while (handAmount > 21 && numAces > 0) {
            handAmount -= 10;
            numAces--;
        }
        return handAmount;
    }

    private static String randomCard() {
        return CARDS[random.nextInt(CARDS.length)];
    }

    private static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void waitForInput() {
        System.out.print("Type something to continue: ");
        scanner.nextLine();
    }

    private static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}