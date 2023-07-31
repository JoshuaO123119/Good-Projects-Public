class a:
    def __init__(self, index):
        self.index = index

vars = a(0)

def options(answer:str):
    answer = answer.lower().strip()
    if answer in ["1", "2", "3", "4", "5"]:
        if answer == "1":
            return 0
        elif answer == "2":
            return 1
        elif answer == "3":
            return 2
        elif answer == "4":
            return 3
        elif answer == "5":
            return 4
    else:
        return None

print("For each question, please TYPE the number that best describes your answer.")
print("Please rate the CURRENT (i.e. LAST 2 WEEKS) SEVERITY of your sleep problem(s).", end="\n\n")

def questions1_3(question):
    while True:
        print(f"{question}:\n\t1. None\n\t2. Mild\n\t3. Moderate\n\t4. Severe\n\t5. Very Severe")
        x = input("Choice (1-5): ")
        addPoint = options(x)
        if addPoint is not None:
            vars.index += addPoint
            break
        else:
            continue
def questions5_7(question):
    while True:
        print(f"{question}:\n\t1. Not at all\n\t2. A Little\n\t3. Somewhat\n\t4. Much\n\t5. Very Much")
        x = input("Choice (1-5): ")
        addPoint = options(x)
        if addPoint is not None:
            vars.index += addPoint
            break
        else:
            continue
# Question1
questions1_3("Difficulty falling asleep")
# Question 2
questions1_3("Difficulty staying asleep")
# Question 3
questions1_3("Problems waking up too early")
# Question 4
while True:
    print("How SATISFIED/DISSATISFIED are you with your CURRENT sleep pattern?:\n\t1. Very Satisfied\n\t2. Satisfied\n\t3. Moderately Satisfied\n\t4. Dissatisfied\n\t5. Very Dissatisfied")
    x = input("Choice (1-5): ")
    addPoint = options(x)
    if addPoint is not None:
        vars.index+=addPoint
        break
    else:
        continue

# Question 5
questions5_7("How NOTICEABLE to others do you think your sleep problem is in terms of impairing the quality of your life?")
# Question 6
questions5_7("How WORRIED/DISTRESSED are you about your current sleep problem?")
# Question 7
questions5_7("To what extent do you consider your sleep problem to INTERFERE with your daily functioning(e.g. daytime fatigue, mood, ability to function at work/daily chores, concentration, memory, mood, etc.) CURRENTLY?")

if vars.index <= 7:
    print("\nYou have no clinically significant insomnia")
elif vars.index <= 14:
    print("\nYou have sub-threshold insomnia")
elif vars.index <= 21:
    print("\nYou have clinical insomnia (moderate severity)")
elif vars.index <= 28:
    print("\nYou have clinical insomnia (severe)")
else:
    print(f"\nThere was an error with calculating your insomnia index...\nError Code: {vars.index}")
