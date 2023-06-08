import os, random, time

"""
    Future ideas:
        1. Allow saving token progress and load on boot
        2. Add local multiplayer (difficult challenge)
        3. Allow player to go against more AI
"""
def main():
    # Clear terminal (Windows)
    os.system("cls")

    # Sets starting token amount
    tokens = 200

    # Runs loop to keep game going until player loses all tokens
    while tokens > 0:
        # Clear terminal (Windows)
        os.system("cls")

        # Card deck
        cards = ["A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"]

        # Clear empty hands for human and AI
        humanHand = []
        aiHand = []

        # Assign new hands
        for _ in range(2):
            humanHand.append(random.choice(cards))
            aiHand.append(random.choice(cards))

        # Check how much each hand is worth
        def deckAmount(hand):
            handAmount = 0
            numAces = 0

            # Calculates how many aces are in hand
            for y in hand:
                if y == "A":
                    numAces += 1

            # Assigns each card in hand accordingly to normal black jack game
            for x in hand:
                if x in ["A", "J", "Q", "K"]:
                    if x == "A":
                        handAmount += 11
                    else:
                        handAmount += 10
                else:
                    handAmount += int(x)
            # If no aces, then return hand total
            if numAces == 0:
                return handAmount
            # If the total of the hand is greater than 21 and has aces, turn aces 11 -> 1
            else:
                while handAmount > 21 and numAces > 0:
                    handAmount -= 10
                    numAces -= 1
                return handAmount

        # Assigns variable to hand totals
        humanHandAmount = deckAmount(humanHand)
        aiHandAmount = deckAmount(aiHand)

        # Displays in a clean format to user of tokens, and hands
        def displayHands():
            print(f"Tokens: {tokens}\n")
            print(f"Your hand total: {humanHandAmount}")
            print(f"Your hand: {humanHand}\n")
            print(f"AI hand total: {aiHandAmount}")
            print(f"AI hand: {aiHand}\n")

        # Check if player has 21
        if humanHandAmount == 21:
            # Push
            if aiHandAmount == 21:
                tokens += 10
                displayHands()
                print("You both push! +10 tokens")
                input("Type something to continue: ")
                continue
            # If human has 21 and ai has lower (Blackjack)
            else:
                tokens += 25
                displayHands()
                print("Blackjack! +25 tokens")
                input("Type something to continue: ")
                continue
        # If starting hand is not 21
        else:
            running = True
            # Set a boolean to check if they used double option
            double = False
            # Used to help multiplier if use double option
            gainLoss = 0
            # Multiplier used to double the token gain/loss if use double option
            mult = 1

            # Run loop to continue giving cards everytime user uses hit option
            while running:
                # Clears terminal
                os.system("cls")

                # Assigns hand totals
                humanHandAmount = deckAmount(humanHand)
                aiHandAmount = deckAmount(aiHand)

                # If player has less than 20 on start
                if humanHandAmount <= 20:
                    os.system("cls")

                    # Shows tokens, your hand, and only the first card of the AI
                    print(f"Tokens: {tokens}\n")
                    print(f"Hand total {humanHandAmount}")
                    print(f"Your hand: {humanHand}")
                    print(f"AI hand: [{aiHand[0]}, X]")
                    # Prints options and let user choose
                    print("Options:\n\t1. Hit\n\t2. Double\n\t3. Stand\n")

                    if not double:
                        x = input("Choice: ").lower().strip()
                    # If they used double option
                    else:
                        x = "3"

                    # Check if they typed in any available options
                    if x in ["1", "2", "3" ,"hit", "stand", "double"]:
                        # Hit option
                        if x in ["1", "hit"]:
                            # Adds card to human hand and re-calculates hand total
                            humanHand.append(random.choice(cards))
                            humanHandAmount = deckAmount(humanHand)
                        # Double option
                        elif x in ["2", "double"]:
                            # Adds only 1 card to human hand, and re-calculates hand total
                            humanHand.append(random.choice(cards))
                            humanHandAmount = deckAmount(humanHand)
                            # Set double so it only gives one card before its automatically "stands"
                            double = True
                            # Multiply the win/loss rate by 2
                            mult = 2
                        # Stand option
                        elif x in ["3", "stand"]:
                            # AI turn, AI stands at 17 or above
                            while aiHandAmount <= 16:
                                # Clears terminal
                                os.system("cls")
                                aiHand.append(random.choice(cards))
                                aiHandAmount = deckAmount(aiHand)
                                displayHands()
                                time.sleep(1)
                            # Clears terminal
                            os.system("cls")

                        # Check for winners
                            # If AI didn't go over 21
                            if aiHandAmount <= 21:
                                # If AI has higher hand total than human (loss)
                                if aiHandAmount > humanHandAmount:
                                    gainLoss = 20 * mult
                                    tokens -= gainLoss
                                    displayHands()
                                    print(f"AI wins this round! -{gainLoss} tokens")
                                    input("Type something to continue: ")
                                    running = False
                                # If AI has lower hand total than human (win)
                                elif aiHandAmount < humanHandAmount:
                                    gainLoss = 20 * mult
                                    tokens += gainLoss
                                    displayHands()
                                    print(f"You win this round! +{gainLoss} tokens")
                                    input("Type something to continue: ")
                                    running = False
                                # If AI and human have same hand total (tie/push)
                                else:
                                    gainLoss = 10 * mult
                                    tokens += gainLoss
                                    displayHands()
                                    print(f"Push! +{gainLoss} tokens")
                                    input("Type something to continue: ")
                                    running = False
                            # If AI went over 21 (busted) (win)
                            else:
                                gainLoss = 20 * mult
                                tokens += gainLoss
                                displayHands()
                                print(f"You win this round! +{gainLoss} tokens")
                                input("Type something to continue: ")

                                running = False
                # If human busted (loss)
                elif humanHandAmount > 21:
                    gainLoss = 20 * mult
                    tokens -= gainLoss
                    displayHands()
                    print(f"AI wins this round! -{gainLoss} tokens")
                    input("Type something to continue: ")

                    running = False
                # If player got 21
                else:
                    # If both human and AI got 21 (tie/push)
                    if humanHandAmount == aiHandAmount:
                        # Push
                        gainLoss = 10 * mult
                        tokens += gainLoss
                        displayHands()
                        print(f"Push, +{gainLoss} tokens")
                        input("Type something to continue: ")
                        running=False
                    # If only the human got 21 (win)
                    else:
                        # If AI hand total less than 17, keep adding cards
                        if aiHandAmount <= 16:
                            # AI turn, AI stands at 17 or above
                            while aiHandAmount <= 16:
                                os.system("cls")
                                aiHand.append(random.choice(cards))
                                aiHandAmount = deckAmount(aiHand)
                                displayHands()
                                time.sleep(1)

                            # If AI and human have same hand total (tie/push)
                            if aiHandAmount == humanHandAmount:
                                # Push
                                gainLoss = 10 * mult
                                tokens += gainLoss
                                displayHands()
                                print(f"Push, +{gainLoss} tokens")
                                input("Type something to continue: ")
                                running = False
                            # If ai didn't get 21 while human does (win)
                            else:
                                os.system("cls")
                                gainLoss = 25 * mult
                                tokens += gainLoss
                                displayHands()
                                print(f"You win this round! +{gainLoss} tokens")
                                input("Type something to continue: ")
                                running = False
                        # If AI already has hand total 17 or above, and human has 21 (win)
                        else:
                            os.system("cls")
                            gainLoss = 25 * mult
                            tokens += gainLoss
                            displayHands()
                            print(f"You win this round! +{gainLoss} tokens")
                            input("Type something to continue: ")
                            running=False

    # Outputs game over to the user when they run out of tokens
    print("Game Over!\nYou ran out of tokens")

# Execute code when file is ran as a script
if __name__ == "__main__":
    # Run game once
    main()
    # Run loop so if they lose all tokens again, they can restart the program
    while True:
        os.system("cls")
        print("Do you wish to play again?")
        # Allow user to choose to continue
        x = input("(y/n): ").lower().strip()
        # If user wants to play again
        if x in ["y", "yes"]:
            main()
        # If user doesn't want to play again
        elif x in ["n", "no"]:
            quit()
