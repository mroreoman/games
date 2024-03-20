import random
import time
import math
from enum import Enum
from colorama import Fore, Style

State = Enum("State", ["HIDDEN", "SHOWN", "FLAGGED"])
colors = (Fore.WHITE, Fore.BLUE, Fore.GREEN, Fore.YELLOW, Fore.MAGENTA, Fore.RED, Fore.CYAN, Fore.WHITE, Fore.BLACK)

class Tile:
    def __init__(self):
        self.isMine = None
        self.neighbors = None
        self.state = State.HIDDEN
        
    def __str__(self):
        if self.state == State.HIDDEN:
            return '□'
        elif self.state == State.FLAGGED:
            return Fore.RED + 'F' + Style.RESET_ALL
        elif self.isMine:
            return Fore.BLACK + 'x' + Style.RESET_ALL
        else:
            return colors[self.neighbors] + str(self.neighbors) + Style.RESET_ALL

class Board:
    def __init__(self, width, height, mines):
        self.width = width
        self.height = height
        self.mines = mines
        self.flags = 0
        self.tiles = None
        self.won = None
        self.startTime = time.time()

    def initTiles(self, click_x, click_y):
        self.tiles = [[Tile() for x in range(self.width)] for y in range(self.height)]

        nums = list(range(self.width * self.height))
        nums.pop(click_y * self.width + click_x)
        for mine in range(self.mines):
            rand = random.randint(0, len(nums)-1)
            self.tiles[nums[rand] // self.width][nums[rand] % self.height].isMine = True
            nums.pop(rand)
        
        for y, row in enumerate(self.tiles):
            for x, tile in enumerate(row):
                tile.neighbors = self.tileNeighbors(x, y)       

    def tileNeighbors(self, x, y):
        neighbors = 0
        for yy in range(-1, 2):
            for xx in range(-1, 2):
                if xx == 0 and yy == 0:
                    continue
                if x+xx < 0 or x+xx >= self.width or y+yy < 0 or y+yy >= self.height:
                    continue
                if self.tiles[y+yy][x+xx].isMine:
                    neighbors += 1
        return neighbors

    def tileFull(self, x, y):
        if self.tiles[y][x].state == State.HIDDEN:
            return False
        flags = 0
        for yy in range(-1, 2):
            for xx in range(-1, 2):
                if yy == 0 and xx == 0:
                    continue
                if self.outOfRange(x+xx, y+yy):
                    continue
                if self.tiles[y+yy][x+xx].state == State.FLAGGED:
                    flags += 1
        return self.tiles[y][x].neighbors == flags

    def outOfRange(self, x, y):
        return x < 0 or x >= self.width or y < 0 or y >= self.height
    
    def __str__(self):
        text = '----' + '-' * self.width * 2 + '\n'
        text += Fore.RED + str(self.mines - self.flags) + Style.RESET_ALL
        text += '\t'*3 + (':)' if self.won is None else ':D' if self.won else ':(') + '\t'*3
        text += Fore.RED + str(math.trunc(time.time() - self.startTime)) + Style.RESET_ALL
        text += '\n\n\t'
        for x in range(self.width):
            text += str(x % 10) + ' '
        text += '\n'
        if self.tiles == None:
            for y in range(self.height):
                text += '\n' + str(y % 10) + '\t'
                for x in range(self.width):
                    text += '_ '
        else:
            for y, row in enumerate(self.tiles):
                text += '\n' + str(y % 10) + '\t'
                for tile in row:
                    text += str(tile) + ' '
        text += '\n' + '----' + '-' * self.width * 2
        return text

    def isSolved(self):
        solved = True
        for row in self.tiles:
            for tile in row:
                if not tile.isMine and tile.state != State.SHOWN:
                    solved = False
        return solved

    def finish(self, won):
        print(self)
        self.endTime = time.time()
        self.won = won
        print("you won!" if won else "you lost.", end = " ")
        print(f"it took {math.trunc(self.endTime - self.startTime)} seconds!!")
        self.showAll()

    def showAll(self):
        for row in self.tiles:
            for tile in row:
                tile.state = State.SHOWN

    def click(self, x, y):
        if self.outOfRange(x, y):
            return
        
        if self.tiles is None:
            self.initTiles(x, y)
        
        if self.tiles[y][x].state != State.HIDDEN:
            return
            
        self.tiles[y][x].state = State.SHOWN
        
        if self.tiles[y][x].isMine:
            self.finish(False)
        elif self.isSolved():
            self.finish(True)
        elif self.tiles[y][x].neighbors == 0:
            Board.onNeighbors(self.click, x, y)
    
    def flag(self, x, y):
        if x < 0 or x > self.width - 1 or y < 0 or y > self.height - 1:
            return
        if self.tiles[y][x].state != State.HIDDEN:
            return
            
        self.tiles[y][x].state = State.FLAGGED
        self.flags += 1
    
    def unflag(self, x, y):
        if x < 0 or x > self.width - 1 or y < 0 or y > self.height - 1:
            return
        if self.tiles[y][x].state != State.FLAGGED:
            return
        
        self.tiles[y][x].state = State.HIDDEN
        self.flags -= 1
    
    def onNeighbors(func, x, y):
        for yy in range(-1, 2):
            for xx in range(-1, 2):
                if xx == 0 and yy == 0:
                    continue
                func(x=x+xx, y=y+yy)

    def onCol(func, x, start, end):
        for y in range(start, end+1):
            func(x, y)
    
    def onRow(func, y, start, end):
        for x in range(start,end+1):
            func(x, y)

boards = []

def start():
    while(True):
        mode = input("easy, medium, hard, custom: ")
        if mode in ("e", "easy"):
            boards.append(Board(9,9,10))
        elif mode in ("m", "medium"):
            boards.append(Board(16,16,40))
        elif mode in ("h", "hard"):
            boards.append(Board(30,16,99))
        elif mode in ("c", "custom"):
            width = int(input("width: "))
            height = int(input("height: "))
            mines = int(input("mines: "))
            boards.append(Board(width, height, mines))
        else:
            continue
        break

    print(boards[-1])
    play(boards[-1])

def play(board):
    while(board.won == None):
        try:
            move = input("move: ")
            coords = input("coords (x,y): ")
            x = int(coords[:coords.find(",")])
            y = int(coords[coords.find(",") + 1:])
        except:
            continue

        if move == 'x':
            break
        elif move == 'c':
            board.click(x, y)
        elif move == 'f':
            board.flag(x, y)
        elif move == 'u':
            board.unflag(x, y)
        elif move == 'cn':
            if board.tileFull(x, y):
                Board.onNeighbors(board.click, x, y)
        elif move == 'fn':
            Board.onNeighbors(board.flag, x, y)
        elif move == 'un':
            Board.onNeighbors(board.unflag, x, y)
        else:
            continue
        
        print(board)

print("""oreo minesweeper
    
controls
    c - click tile
    f - flag tile
    u - unflag tile
    n suffix - do action to neighbors
    x - exit board

records
    easy: oreo 01/20/23 - 42
    medium: oreo 01/13/23 - 785
    hard:
    
have fun !!
""")

while(True):
    choice = input("s to start, x to exit: ")
    if choice == 'x':
        break
    elif choice == 's':
        start()