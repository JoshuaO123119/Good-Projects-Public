x = int(input("Type a factorial: ").strip())
def factorial(num):
    result = 1

    for y in range(num):
        result*=y+1

    return result

print(factorial(x))