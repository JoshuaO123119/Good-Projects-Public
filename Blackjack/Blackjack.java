import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Blackjack {
    private static final String[] CARDS = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    private static final int tokenAmount = 200;
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    public static void main(String[] args) {
    	while (true) {
            blackJackGame(tokenAmount);
            System.out.println("Do you wish to play again? (y/n): ");
            String choice = scanner.nextLine().toLowerCase().trim();
            if (!choice.equals("y") && !choice.equals("yes")) {
                break;
            }
            
        }
    }

    private static void blackJackGame(int tokens) {
    	clearConsole();
        while (tokens > 0) {
            clearConsole();
            List<String> humanHand = new ArrayList<>();
            List<String> aiHand = new ArrayList<>();

            for (int i = 0; i < 2; i++) {
                humanHand.add(CARDS[random.nextInt(CARDS.length)]);
                aiHand.add(CARDS[random.nextInt(CARDS.length)]);
            }

            int humanHandAmount = deckAmountBlackjack(humanHand);
            int aiHandAmount = deckAmountBlackjack(aiHand);
            	
            displayHandsBlackJack(humanHand, aiHand, humanHandAmount, aiHandAmount, tokens);
            int startBet = 0;
            while (true) {
            	clearConsole();
                System.out.println("Tokens: " + tokens);
                System.out.print("Starting bet: ");
                String input = scanner.nextLine().trim();
                if (input.matches("\\d+")) {
                    int bet = Integer.parseInt(input);
                    if (bet > 0 && bet <= tokens) {
                        startBet = bet;
                        break;
                    } else {
                        System.out.println("You can't bet more tokens than you have or nothing!");
                        waitForInput();
                    }
                } else {
                    System.out.println("That wasn't a valid integer");
                    waitForInput();
                }
            }
            tokens -= startBet;

            if (humanHandAmount == 21) {
                if (aiHandAmount == 21) {
                    tokens += startBet;
                    displayHandsBlackJack(humanHand, aiHand, humanHandAmount, aiHandAmount, tokens);
                    System.out.println("You both push! +" + startBet + " tokens");
                    waitForInput();
                    continue;
                } else {
                    int gainLoss = (int) Math.round(startBet * 2 * 1.25);
                    tokens += gainLoss;
                    displayHandsBlackJack(humanHand, aiHand, humanHandAmount, aiHandAmount, tokens);
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
                humanHandAmount = deckAmountBlackjack(humanHand);
                aiHandAmount = deckAmountBlackjack(aiHand);

                if (humanHandAmount <= 21) {
                	clearConsole();
                    System.out.println("Tokens: " + tokens);
                    System.out.println("Hand total: " + humanHandAmount);
                    System.out.println("Your hand: " + humanHand);
                    System.out.println("AI hand: [" + aiHand.get(0) + ", X]");
                    System.out.println("Options:\n\t1. Hit\n\t2. Double\n\t3. Stand\n");
                	//                    displayGameState(humanHand, aiHand, humanHandAmount, aiHandAmount, tokens);
                    
                    System.out.print("Choice: ");
                    String playerChoice = scanner.nextLine().toLowerCase().trim();
                    
                    String choice = doubleBet ? "3" : playerChoice;

                    if (choice.equals("1") || choice.equals("hit")) {
                        humanHand.add(CARDS[random.nextInt(CARDS.length)]);
                    } else if (choice.equals("2") || choice.equals("double")) {
                        if (tokens >= startBet) {
                            humanHand.add(CARDS[random.nextInt(CARDS.length)]);
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
                            aiHand.add(CARDS[random.nextInt(CARDS.length)]);
                            aiHandAmount = deckAmountBlackjack(aiHand);
                            displayHandsBlackJack(humanHand, aiHand, humanHandAmount, aiHandAmount, tokens);
                            sleep(1000);
                        }

                        if (aiHandAmount <= 21) {
                            if (aiHandAmount > humanHandAmount) {
                                gainLoss = startBet * mult;
                                displayHandsBlackJack(humanHand, aiHand, humanHandAmount, aiHandAmount, tokens);
                                System.out.println("AI wins this round! -" + gainLoss + " tokens");
                                waitForInput();
                                running = false;
                            } else if (aiHandAmount < humanHandAmount) {
                                gainLoss = (startBet * 2) * mult;
                                tokens += gainLoss;
                                displayHandsBlackJack(humanHand, aiHand, humanHandAmount, aiHandAmount, tokens);
                                System.out.println("You win this round! +" + gainLoss + " tokens");
                                waitForInput();
                                running = false;
                            } else {
                                gainLoss = startBet * mult;
                                tokens += gainLoss;
                                displayHandsBlackJack(humanHand, aiHand, humanHandAmount, aiHandAmount, tokens);
                                System.out.println("Push! +" + gainLoss + " tokens");
                                waitForInput();
                                running = false;
                            }
                        } else {
                            gainLoss = (startBet * 2) * mult;
                            tokens += gainLoss;
                            displayHandsBlackJack(humanHand, aiHand, humanHandAmount, aiHandAmount, tokens);
                            System.out.println("You win this round! +" + gainLoss + " tokens");
                            waitForInput();
                            running = false;
                        }
                    }
                } else {
                    gainLoss = startBet * mult;
                    displayHandsBlackJack(humanHand, aiHand, humanHandAmount, aiHandAmount, tokens);
                    System.out.println("AI wins this round! -" + gainLoss + " tokens");
                    waitForInput();
                    running = false;
                }
            }
        }
        System.out.println("Game Over!\nYou ran out of tokens");
    }


    private static void displayHandsBlackJack(List<String> humanHand, List<String> aiHand, int humanHandAmount, int aiHandAmount, int tokens) {
        clearConsole();
        System.out.println("Tokens: " + tokens);
        System.out.println("Your hand total: " + humanHandAmount);
        System.out.println("Your hand: " + humanHand);
        System.out.println("AI hand total: " + aiHandAmount);
        System.out.println("AI hand: " + aiHand);
    }

    private static int deckAmountBlackjack(List<String> hand) {
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

    private static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void waitForInput() {
        System.out.print("Press enter to continue: ");
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
