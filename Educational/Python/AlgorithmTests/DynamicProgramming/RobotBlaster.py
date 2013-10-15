## 10/14/13
## Algorithm Design, Problem 6.8(b)

x     = [0, 1, 10, 10, 1]
f     = [0, 1,  2,  4, 8]
g     = [0, 0,  0,  0, 0]
trueG = [0, 1,  2,  4, 5]

for i in range(1, 5):
    for j in range(0, i):
        newScore = g[j] + min(f[i-j],x[i])
        if g[i] < newScore:
            g[i] = newScore

print trueG
print g
