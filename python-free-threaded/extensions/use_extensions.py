import sys

import legacy  # noqa: F401  (triggers the GIL re-enable warning)

print("After importing legacy, GIL enabled:", sys._is_gil_enabled())
