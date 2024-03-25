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
        if not guess.isalpha():
            print("guess must be letters")
        elif len(guess) != 5:
            print("not enough letters")
        elif not (guess in wordleLa or guess in wordleTa):
            print("not in word list")
        else:
            break
    
    guesses += 1
    hints = []
    for i, letter in enumerate(guess):
        if letter not in word:
            hints.append("grey")
        elif letter == word[i]:
            hints.append("green")
        elif guess.count(letter) > word.count(letter):
            #if its the extra repeating letter it should be grey
        else:
            hints.append("yellow")

print("you got it! it took " + str(guesses) + (" guess!" if guesses == 1 else " guesses!"))
