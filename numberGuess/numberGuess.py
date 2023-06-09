import random, os

def main():
    # Clears terminal
    os.system("cls")

    # Show options for different difficulties
    print("Difficulty options:\n\t1. Easy\n\t2. Medium\n\t3. Hard\n\t4. Impossible\n")

    # Ask for their choice
    diff = input("Choice: ").lower().strip()

    # If they choose one of the options
    if diff in ["1", "2", "3", "4", "easy", "medium", "hard", "impossible"]:
        # Easy difficulty
        if diff in ["1", "easy"]:
            maxNum = 25
            maxTries = 7
        # Medium difficulty
        elif diff in ["2", "medium"]:
            maxNum = 35
            maxTries = 6
        # Hard difficulty
        elif diff in ["3", "hard"]:
            maxNum = 50
            maxTries = 6
        # Impossible difficulty
        elif diff in ["4", "impossible"]:
            maxNum = 50
            maxTries = 5
    # If they mistyped or typed in something that wasn't an option
    else:
        print("That wasn't an option!")
        input("Type something to retry: ")
        main()

    # Random number for them to guess
    randNum = random.randint(1, maxNum)

    triesLeft = maxTries

    # Keep game running as long as they have tries left
    while triesLeft > 0:
        print(f"\nTries left: {triesLeft}")

        # Asks user for input
        x = input(f"Guess a number (1-{maxNum}): ").strip()

        # Check if their guess is a valid number
        if x.isdigit():
            # Convert their guess from string to integer
            x = int(x)
            # If their guess is too high
            if x > randNum:
                print("Guess lower!")
            # If their guess is too low
            elif x < randNum:
                print("Guess higher!")
            # If they guess the number
            else:
                # Makes their guesses total more accurate
                triesLeft -= 1

                print(f"You guessed correctly in {maxTries-triesLeft} tries!")

                # Ends the function
                return False
            # They guessed, so subtract tries left by one
            triesLeft -= 1
    # The while loop ended, so they didn't guess the number
    print("\nYou didn't guess it fast enough!")
    print(f"The number was {randNum}")

# If being run in main script
if __name__ == "__main__":
    main()

    # Ask if they want to replay
    while True:
        # Ask if they want to replay
        x = input("\nWould you like to play again? (y/n): ").lower().strip()
        # If they want to replay
        if x in ["y", "yes"]:
            main()
        # If they don't want to replay
        elif x in ["n", "no"]:
            quit()
        # If they typed something wrong
        else:
            print("That's not a valid option! Please try again\n")