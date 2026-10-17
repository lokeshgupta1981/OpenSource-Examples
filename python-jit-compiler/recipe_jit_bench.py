"""Hot-loop benchmark: scaling ingredient quantities for a recipe box order.

A JIT-friendly hot loop: a tight, numeric, type-stable loop that runs the same
few bytecode instructions millions of times. This is the shape of workload the
copy-and-patch JIT targets first.
"""
import time
import sys

RECIPE_YIELDS = {
    "flour_g": 480,
    "sugar_g": 220,
    "butter_g": 227,
    "eggs": 4,
    "vanilla_ml": 10,
}

ITERATIONS = 20_000_000


def scale_recipe_loop(iterations):
    """Scale one recipe's flour amount by a varying batch factor, iterations times.

    This is the hot loop: one multiplication and one addition, on plain
    floats, repeated millions of times with no function calls and no
    attribute lookups inside the loop body.
    """
    grams = RECIPE_YIELDS["flour_g"]
    total = 0.0
    batch_factor = 1.0
    for i in range(iterations):
        batch_factor = (batch_factor + 0.0000001) % 2.0
        total += grams * batch_factor
    return total


def main():
    jit_info = "unknown"
    if hasattr(sys, "_jit"):
        jit_info = f"available={sys._jit.is_available()}, enabled={sys._jit.is_enabled()}"

    start = time.perf_counter()
    result = scale_recipe_loop(ITERATIONS)
    elapsed = time.perf_counter() - start

    print(f"python: {sys.version.split()[0]}")
    print(f"sys._jit: {jit_info}")
    print(f"iterations: {ITERATIONS:,}")
    print(f"result: {result:.4f}")
    print(f"elapsed_seconds: {elapsed:.4f}")


if __name__ == "__main__":
    main()
