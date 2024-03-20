import random
from time import time
from math import trunc
from enum import Enum
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
        print(self)

    def markRow(self, y, marking):
        for tile in self.tiles[self.size-y-1]:
            if tile.marking is None:
                self.mark(tile, marking)
        print(self)

    def markCol(self, x, marking):
        for tile in [row[x] for row in self.tiles]:
            if tile.marking is None:
                self.mark(tile, marking)
        print(self)

    def submit(self):
        self.endTime = time()
        if self.solved():
            self.won = True
            print(f"\nyou won! it took {trunc(self.endTime - self.startTime)} seconds!!\n")
        else:
            self.lost = False
            print(f"\nyou lost! it took {trunc(self.endTime - self.startTime)} seconds!!\n")
        self.revealTiles()
        print(self)

boards = []

def start():
    while(True):
        try:
            size = int(input("board size: "))
        except:
            continue
        else:
            break
    boards.append(Board(size))
    play(boards[-1])

def play(board):
    while(True):
        try:
            move = input("move: ")
            coords = input("coords (x,y): ")
            x = int(coords[:coords.find(",")])
            y = int(coords[coords.find(",") + 1:])
        except:
            continue

        if move == 'x':
            break

def n(size):
    boards.append(Board(size))

def f(x, y):
    boards[-1].markTile(x-1, y-1, Markings.FILLED)
    
def fr(y):
    boards[-1].markRow(y-1, Markings.FILLED)
    
def fc(x):
    boards[-1].markCol(x-1, Markings.FILLED)

def e(x, y):
    boards[-1].markTile(x-1, y-1, Markings.EMPTY)

def er(y):
    boards[-1].markRow(y-1, Markings.EMPTY)

def ec(x):
    boards[-1].markCol(x-1, Markings.EMPTY)

def u(x, y):
    boards[-1].markTile(x-1, y-1, None)

def ur(y):
    boards[-1].markRow(y-1, None)

def uc(x):
    boards[-1].markCol(x-1, None)

def s():
    boards[-1].submit()

n(5)