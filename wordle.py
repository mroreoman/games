import random
wordleTa = []
wordleLa = []
with open(r"wordleDictionary\wordle-La.txt", "r") as file:
    for i in range(2315):
        wordleLa.append(file.readline().strip())
with open(r"wordleDictionary\wordle-Ta.txt", "r") as file:
    for i in range(10657):
        wordleTa.append(file.readline().strip())

word = wordleLa[random.randrange(2315)].strip()
print(word)

guesses = 0
guess = ""
while guess != word:
    while True:
        guess = input("guess: ")
        if len(guess) != 5:
            print("invalid input")
        elif not guess.isalpha():
            print("invalid input")
        elif not (guess in wordleLa or guess in wordleTa):
            print("not in word list")
        else:
            break
    
    guesses += 1

print("you got it! it took " + str(guesses) + (" guess!" if guesses == 1 else " guesses!"))
