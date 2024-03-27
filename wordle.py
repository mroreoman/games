import random
from util import color
from colorama import Fore

wordleTa = []
wordleLa = []

with open(r"wordleDictionary\wordle-La.txt", "r") as file:
    while True:
        line = file.readline()
        if not line:
            break
        wordleLa.append(line.strip())

with open(r"wordleDictionary\wordle-Ta.txt", "r") as file:
    while True:
        line = file.readline()
        if not line:
            break
        wordleTa.append(line.strip())

word = wordleLa[random.randrange(2315)].strip()

wordLetters = {}
for letter in word:
    if letter not in wordLetters:
        wordLetters.update({letter:0})
    wordLetters[letter] += 1

guesses = 0

guess = ""
while guess != word:
    while True:
        guess = input("guess: ")
        if not guess.isalpha():
            print("invalid characters")
        elif len(guess) != 5:
            print("invalid length")
        elif not (guess in wordleLa or guess in wordleTa):
            print("not in word list")
        else:
            break
    
    guesses += 1
    wordLettersC = wordLetters.copy()

    for i, letter in enumerate(guess):
        if letter not in word or wordLettersC[letter] == 0:
            print(color(letter, Fore.WHITE), end='')
        elif letter == word[i]:
            wordLettersC[letter] -= 1
            print(color(letter, Fore.GREEN), end='')
        else:
            wordLettersC[letter] -= 1
            print((color(letter, Fore.YELLOW)), end='')
    print()

print("you got it! it took " + str(guesses) + (" guess!" if guesses == 1 else " guesses!"))