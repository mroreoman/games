import random
from time import time
from math import trunc
from enum import Enum
from util import inputNum, inputTwoNums
Markings = Enum("Markings", ["FILLED", "EMPTY"])

class Tile:
    def __init__(self):
        self.filled = random.choice((True, False))
        self.marking = None
        self.revealed = False

    def __str__(self):
        if self.revealed:
            return '■' if self.filled else '□'
        
        if self.marking is None:
            return '_'
        elif self.marking is Markings.FILLED:
            return '■'
        elif self.marking is Markings.EMPTY:
            return '□'

class Board:
    def __init__(self, size):
        self.tiles = [[Tile() for x in range(size)] for y in range(size)]
        self.size = size
        self.colLabels = self.countCols() 
        self.rowLabels = self.countRows()
        self.topMargin = max([len(col) for col in self.colLabels])
        self.leftMargin = max([len(row) for row in self.rowLabels])
        self.startTime = time()
        self.won = None
        print(self)

    def countCols(self):
        colLabels = []
        for x in range(self.size):
            label = []
            count = 0
            for tile in [row[x] for row in self.tiles]:
                if tile.filled:
                    count += 1
                else:
                    if count > 0:
                        label.append(count)
                    count = 0
            if count > 0:
                label.append(count)
            colLabels.append(label)
        return colLabels
    
    def countRows(self):
        rowLabels = []
        for row in self.tiles:
            label = ''
            count = 0
            for tile in row:
                if tile.filled:
                    count += 1
                else:
                    if count > 0:
                        label += str(count) + ' '
                    count = 0
            if count > 0:
                label += str(count) + ' '
            rowLabels.append(label[:-1])
        return rowLabels

    def __str__(self):
        text = ''
        
        for n in range(self.topMargin):
            text += ' ' * self.leftMargin
            for label in self.colLabels:
                i = n - self.topMargin + len(label)
                text += ' ' + (str(label[i]) if i > -1 else ' ')
            text += '\n'
        
        for label, row in zip(self.rowLabels, self.tiles):
            text += label.rjust(self.leftMargin, ' ')
            for tile in row:
                text += ' ' + str(tile)
            text += '\n'
        
        return text[:-1]

    def solved(self):
        for row in self.tiles:
            for tile in row:
                if tile.filled and tile.marking is not Markings.FILLED:
                    return False
        return True

    def revealTiles(self):
        for row in self.tiles:
            for tile in row:
                tile.revealed = True

    def mark(self, tile, marking):
        tile.marking = marking
        # check solved

    def markTile(self, x, y, marking):
        self.mark(self.tiles[self.size-y-1][x], marking)

    def markRow(self, y, marking):
        for tile in self.tiles[self.size-y-1]:
            if tile.marking is None:
                self.mark(tile, marking)

    def markCol(self, x, marking):
        for tile in [row[x] for row in self.tiles]:
            if tile.marking is None:
                self.mark(tile, marking)

    def submit(self):
        self.endTime = time()
        if self.solved():
            self.won = True
            print(f"you won! it took {trunc(self.endTime - self.startTime)} seconds!!\n")
        else:
            self.won = False
            print(f"you lost! it took {trunc(self.endTime - self.startTime)} seconds!!\n")
        self.revealTiles()

boards = []

def start():
    while True:
        try:
            size = int(input("board size: "))
        except:
            continue
        else:
            break
    boards.append(Board(size))
    play(boards[-1])

def play(board):
    while board.won is None:
        move = input("move: ")

        if move == 'x':
            break

        if move == 'f':
            x,y = inputTwoNums("x,y: ")
            board.markTile(x-1, y-1, Markings.FILLED)
        elif move == 'fr':
            y = inputNum("y: ")
            board.markRow(y-1, Markings.FILLED)
        elif move == 'fc':
            x = inputNum("x: ")
            board.markCol(x-1, Markings.FILLED)
        elif move == 'e':
            x,y = inputTwoNums("x,y: ")
            board.markTile(x-1, y-1, Markings.EMPTY)
        elif move == 'er':
            y = inputNum("y: ")
            board.markRow(y-1, Markings.EMPTY)
        elif move == 'ec':
            x = inputNum("x: ")
            board.markCol(x-1, Markings.EMPTY)
        elif move == 'u':
            x,y = inputTwoNums("x,y: ")
            board.markTile(x-1, y-1, None)
        elif move == 'ur':
            y = inputNum("y: ")
            board.markRow(y-1, None)
        elif move == 'uc':
            x = inputNum("x: ")
            board.markCol(x-1, None)
        elif move == 's':
            board.submit()
        else:
            continue

        print('\n' + board + '\n')

while True:
    choice = input("s to start, x to exit: ")
    if choice == 'x':
        break
    elif choice == 's':
        start()