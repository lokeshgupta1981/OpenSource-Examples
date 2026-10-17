import sys
import sysconfig

print(sys.version)
print("Free-threaded build:", sysconfig.get_config_var("Py_GIL_DISABLED") == 1)
print("GIL enabled now:", sys._is_gil_enabled())
