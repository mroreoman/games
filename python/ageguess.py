import random
from util import inputNum
num = random.randrange(100)
guesses = 1
print(f"guess my age!")
while (guess:=inputNum("your guess: ")) != num:
    if guess > num:
        print("too high!")
    else:
        print("too low!")
    guesses += 1
print(f"you got it! it took {guesses} tries.")