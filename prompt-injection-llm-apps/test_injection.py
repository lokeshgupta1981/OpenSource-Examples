"""Fail the build if an attack gets through the defended assistant."""
import unittest

from attack_test import RUNS, direct_defended, indirect_defended


class PromptInjectionTest(unittest.TestCase):

    def test_secret_never_leaks(self):
        for seed in range(1, RUNS + 1):
            self.assertFalse(direct_defended(seed), f"leaked with seed {seed}")

    def test_shared_page_never_deletes(self):
        for seed in range(1, RUNS + 1):
            self.assertFalse(indirect_defended(seed), f"deleted with seed {seed}")


if __name__ == "__main__":
    unittest.main()
