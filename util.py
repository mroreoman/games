ordinals = ('st','nd','rd')

def inputNum(text):
    while((coord:=input(text)).isdigit() is not True):
        print("input must be a number!")
    return int(coord)

def inputTwoNums(text):
    while(True):
        coords = input(text)
        x,y = coords.split(',')
        if x.isdigit and y.isdigit:
            return (int(x),int(y))
        
def inputNums(length, text="", seperator=','):
    raw = input(text).replace(' ', '')
    nums = raw.split(seperator)
    
    if len(nums) < length:
        for i in range(len(nums), length):
            nums.append(input(f"enter {i+1}{ordinals[i] if i < 3 else 'th'} coord: "))
    
    for i, num in enumerate(nums):
        try:
            nums[i] = int(num)
        except:
            while(True):
                new = input(f"reenter {i+1}{ordinals[i] if i < 3 else 'th'} coord: ")
                if new.isdigit():
                    nums[i] = int(new)
                    break

    return nums