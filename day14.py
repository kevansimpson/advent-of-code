from functools import reduce
from itertools import count, takewhile
from operator import mul
from pathlib import Path

input_text = Path('/Users/kevan/code/advent-of-code/src/test/resources/2024/input14.txt').read_text('utf-8').splitlines()
input_data = [[tuple(map(int, eq.split('=')[1].split(','))) for eq in line.split()] for line in input_text]

height = 103
width = 101

def forward(time: int):
    return [((p[0] + v[0] * time) % width, (p[1] + v[1] * time) % height) for p, v in input_data]

def part_1() -> int:
    u = width // 2
    v = height // 2
    quadrants = [0, 0, 0, 0]
    for x, y in forward(100):
        for i, c in enumerate([x < u and y < v, x > u and y < v, x < u and y > v, x > u and y > v]):
            quadrants[i] += c

    return reduce(mul, quadrants)

def part_2() -> int:
    return len(list(takewhile(lambda t: len(input_data) != len(set(forward(t))), count())))

print("Part 1:", part_1())
print("Part 2:", part_2())