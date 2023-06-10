import json, os

def addContact(name, phoneNumber, address, email, notes=None):
    # Clear terminal
    os.system("cls")

# Convert all parameters to strings
    # Convert str to title format (john doe -> John Doe)
    name = str(name).title()
    phoneNumber = str(phoneNumber)
    address = str(address)
    email = str(email)

    # If notes parameter is not None type convert to string
    if type(notes) is not None:
        notes = str(notes)
    # Open data.json file in read/write mode
    with open("data.json", "r+") as dataFile:
        # Grab data from data.json in dict form
        data = dict(json.load(dataFile))
        # If can find data without throwing error means contact already exists
        try:
            if data[name]:
                print("That contact already exists, can't add a new contact")
        # If contact couldn't be found
        except KeyError:
            print("Creating contact...")
            # Append data to existing dictionary
            data[name] = [
                {
                    "phone": phoneNumber,
                    "address": address,
                    "email": email,
                    "notes": notes
                }
            ]
            # Delete file
            dataFile.truncate(0)
            dataFile.seek(0)
            # Write new data to json file
            dataFile.write(json.dumps(data, indent=4))
            print("Contact successfully created")
        # Close json file
        dataFile.close()
def removeContact(name):
    # Clear terminal
    os.system("cls")

    # Convert paramater to string and title format (john doe -> John Doe)
    name = str(name).title()

    # Try to remove contanct if found
    try:
        # Open data.json
        with open("data.json", "r+") as dataFile:
            print("Finding contact...")
            # Grab data from data.json file
            data = dict(json.load(dataFile))

            # Delete contact fully
            del data[name]

            # Ask for confirmation before writing change to file
            print(f"Are you sure you want to delete \"{name}\"?")
            x = input("(y/n): ").lower().strip()
            # If they confirm to change
            if x in ["y", "yes"]:
                print("Deleting contact...")
                # Deleting file completely
                dataFile.truncate(0)
                dataFile.seek(0)
                # Writing changes to file
                dataFile.write(json.dumps(data, indent=4))
                print(f"Successfully deleted \"{name}\" from your contact book")
            # If they didn't choose a valid option
            elif x not in ["n", "no"]:
                print("That is not a valid option")
                input("Type something to try again: ")
                removeContact(name=name)
            # Close file
            dataFile.close()
    # Couldn't find contact name
    except KeyError:
        # Clear terminal
        os.system("cls clear")
        # Close file
        dataFile.close()
        print(f"The contact \"{name}\" doesn't exist")

def accessContact(name):
    # Convert parameter to string and title format (john doe -> John Doe)
    name = str(name).title()
    # Open data.json file
    with open("data.json", "r+") as dataFile:
        # Grab data from data.json file
        data = dict(json.load(dataFile))
        # If successfully able to find contact
        try:
            print("Finding contact...")
            info = data[name][0]
            print("Getting info from contact...")

            # Grabbing contact info
            phone = info["phone"]
            address = info["address"]
            email = info["email"]
            notes = info["notes"]

            # Clear terminal
            os.system("cls")
            # Display info to user
            print(f"{name}:\n\tPhone Number: {phone}\n\tEmail: {email}\n\tHome Address: {address}\n\tExtra Notes: {notes}")
        # If it couldn't successfully find contact
        except KeyError:
            print(f"\"{name}\" doesn't exist in your contact book")
            input("Type something to continue: ")

def displayContacts():
    # Open data.json file
    with open("data.json", "r") as dataFile:
        # Grab data from data.json file
        data = list(json.load(dataFile))

        placement = 0 # Used for iterating through data
        showAmount = 20
        while True:
            # Display contacts in "showAmount" intervals
            for _ in range(showAmount):
                # If there's still contacts left to be displayed
                if placement != len(data):
                    print(data[placement])
                    placement += 1
                # If there aren't any contacts left to be displayed
                else:
                    # Leave the loop
                    break
            # Used to keep asking user if they want to display the next interval of contacts left
            def showNext():
                # If contacts are still left to be displayed
                if placement != len(data):
                    print(f"\nOn contact {placement}/{len(data)}")
                    y = input(f"Show next {showAmount} contacts? (y/n): ")
                    if y in ["y", "yes"]:
                        # Used to signal to continue
                        return True
                    elif y in ["n", "no"]:
                        # Used to signal to stop
                        return False
                    # If user didn't choose a valid option
                    else:
                        print("That's not a valid option")
                        input("Type something to retry: ")
                        showNext()
                # If no contacts are still left to be displayed
                else:
                    # Used to signal to stop
                    return False
            # Used to determine if user wants to keep displaying contacts
            value = showNext()
            if (placement != len(data)) and not value:
                break
        # Close file
        dataFile.close()

if __name__ == "__main__":
    # Used to keep program running constantly
    while True:
        # Clear terminal
        os.system("cls")
        # Display all options to user
        print("Options:\n\t1. Add Contact\n\t2. Get Contact Info\n\t3. Remove Contact\n\t4. Display Contacts")
        x = input("\nChoice (1-4): ").strip()
        # If they typed a valid option
        if x in ["1", "2", "3", "4"]:
            # Add contact
            if x == "1":
                name = input("Name of contact: ")
                phoneNumber = input("Phone Number: ")
                address = input("Address: ")
                email = input("Email: ")
                notes = input("Extra Notes: ")

                addContact(name=name, phoneNumber=phoneNumber, address=address, email=email, notes=notes)
            # Get Contact Info
            elif x == "2":
                name = input("Name of contact: ")
                accessContact(name=name)
            # Remove Contact
            elif x == "3":
                name = input("Name of contact: ")
                removeContact(name=name)
            # Display Contact
            elif x == "4":
                displayContacts()
        # If they didn't type a valid option
        else:
            print("That is not a valid option")
            input("Type something to continue: ")
            continue
        # Used to stop and let user see what is happening rather iterating through rapidly
        input("\nType something to continue: ")