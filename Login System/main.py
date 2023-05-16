import os, json

"""Todo: 
        1. Allow "Account Recovery" options
        2. Allow more fun projects user can play when logged into account
"""

def createAccount(name, password):
    # Open json file and convert json format to be used for python
    with open("data.json", "r+") as jsonFile:
        data = json.loads(jsonFile.read())
        try:
            if data[str(name)]:
                print("Account already exists!")
                input("Type something to continue: ")

        # If it can't find account name
        except KeyError:
            data.update({str(name):str(password)})
            jsonFile.truncate(0)
            jsonFile.seek(0)
            jsonFile.write(json.dumps(data, indent=4))
            print("Account successfully created!")
            input("Type something to continue: ")
    jsonFile.close()
def deleteAccount(name):
    f = open("data.json", "r+")
    data = dict(json.loads(f.read()))

    x = input(f"Type your username to confirm the deletion of your account {name}: ")

    if x == name:
        del data[name]
        f.seek(0)
        f.truncate(0)
        f.write(json.dumps(data, indent=4))
        input("Account successfully deleted.\nType something to continue: ")
        return True
    else:
        print("You did not confirm your account\nCancelling...")
        input("Type something to continue: ")

def login(name, password):
    f = open("data.json", "r")
    data = json.loads(f.read())
    try:
        if data[str(name)] == str(password):
            # Successfully found account and password is correct
            print("Successfully logged into your account")
            while True:
                os.system("cls")
                action = input("Actions\n\t1. Account Settings\n\t2. Log Out\n\t3. Delete Account\n\nInput number: ").lower().strip()
                if action in ["1", "account settings"]:
                    print("Nothing here at the moment")
                    input("Type something to continue: ")
                    continue
                elif action in ["2", "log out"]:
                    print("Logging out...")
                    break
                elif action in ["3", "delete account"]:
                    successfulDelete = deleteAccount(name)
                    if successfulDelete:
                        print("Logging out due to deletion of account...")
                        break
                else:
                    print("That's not an option or was typed incorrectly")
                    input("Please type something to try again: ")
                    continue
        else:
            # Successfully found account but password is incorrect
            print("Username or password is incorrect")
            input("Type something to continue: ")

    # Couldn't find account name (username doesn't exist)
    except KeyError:
        print("Username or password is incorrect")
        input("Type something to continue: ")

    f.close()
def main():
    while True:
        os.system("cls")
        print("Actions:\n\t1. Create Account (create)\n\t2. Log into account (login)\n\t3. Exit")
        x = input("\n\nInput number: ").lower().strip()

        if x in ["1", "create account", "create"]:
            name = input("Username: ").strip()
            password = input("Password: ").strip()
            createAccount(name, password)
        elif x in ["2", "login", "log in", "log into account"]:
            name = input("Username: ").strip()
            password = input("Password: ").strip()
            login(name, password)
        elif x in ["3", "exit", "quit", "stop"]:
            quit()
        elif x == "admin":
            f = open("data.json", "r")
            data = json.loads(f.read())

            os.system("cls")
            print("Existing accounts:")
            for y in data:
                print(f"name:{y} password:{data[y]}")
            input("Type something to continue: ")

        else:
            print("That's not an option or was typed incorrectly")
            input("Please type something to continue: ")
            continue

if __name__ == "__main__":
    # Check if data.json exists (database holding accounts)
    try:
        f = open("data.json", "r")
        f.close()
        main()
    except FileNotFoundError:
         def fileNotFound():
            print("data.json file not found! (database holding accounts)")
            x = input("Do you want to automatically create the database? (y/n): ").lower().strip()
            if x in ["yes", "y"]:
                f = open("data.json", "w")
                f.write("{}")
                f.close()
                try:
                    main()
                except FileNotFoundError:
                    fileNotFound()
            elif x not in ["no", "n"]:
                print("That was not understood...")
                input("Type something to try again: ")
                fileNotFound()
         fileNotFound()