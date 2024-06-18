local age = math.random(100)
print(age)
local guesses = 0

print("hi welcome to age guess game!")
print("guess my age")
while true do
    guesses = guesses + 1
    local guess = tonumber(io.read())
    if guess < age then
        print("too low! guess again")
    elseif guess > age then
        print("too high! guess again")
    else
        break
    end
    print("")
end
print("congrats, u guessed my age! it took " .. guesses .. " guesses!")