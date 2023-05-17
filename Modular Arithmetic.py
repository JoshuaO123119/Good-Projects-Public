"""
    Note: Uses modular arithmetic to convert base 10 integers to base 2 (binary) integers
    Also, I'm bad at commenting because I don't know how to describe to set up the formula, SORRY!

    Ideas: Use modular arithmetic to convert any given num from any given base, to another num in specified base (Ex: Convert #81 from base 10 to base 5)
"""


def intToBinary(x):
    # Vars
    exponents = []
    maxEx = 0
    binaryResult = ""
    print(binaryResult)

    # While 2 to the power of maxEx is greater than base10 integer
    while 2**maxEx < x:
        exponents.append(maxEx)
        maxEx += 1

    # Reverse list
    exponents.sort(reverse=True)

    # Grab each exponent with the base of 2 is less then x, then convert to 1 or 0 according to formula
    for ex in exponents:
        if 2**ex <= x:
            x -= 2**ex
            binaryResult += "1"
        else:
            binaryResult += "0"
    return binaryResult

def binaryToInt(x):
    nums = []
    result = 0
    iteration = 0
    # Use method to set up formula (difficult to explain)
    for y in range(len(str(x))):
        nums.append(2**y)

    # Reverse nums list
    nums.sort(reverse=True)

    # Grab each 1 and 0, then use the information based on given num to use formula
    for z in str(x):
        if z == "1":
            result += nums[iteration]
        iteration += 1

    return result