import subprocess

ps = [0.5, 1]
nodes = [10, 25, 40, 55, 70]
W = [1, 25, 50, 75, 100]

c = 0
for n in nodes:
    for w in W:
        for p in ps:
            subprocess.call(['java', 'GraphGenerator', '%d' % n, '%s' % p, '%d' % w, '%d' % c])
            # c += 1
