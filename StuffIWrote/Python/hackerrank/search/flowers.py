'''
7/12/14, 12:36 PM
Ethan Petuchowski
flowers

https://www.hackerrank.com/challenges/flowers
'''

# code snippet illustrating input/output methods
flowers_to_buy, ppl = map(int, raw_input().split())
flower_costs = sorted(map(int, raw_input().split()), reverse=True)

flowers_bought = -1
total = 0
for i, cost in enumerate(flower_costs):
    person = i % ppl

    if person == 0:
        flowers_bought += 1

    total += (flowers_bought+1)*cost

print total
