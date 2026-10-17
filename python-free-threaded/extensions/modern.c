#define PY_SSIZE_T_CLEAN
#include <Python.h>

static PyModuleDef_Slot slots[] = {
#ifdef Py_GIL_DISABLED
    {Py_mod_gil, Py_MOD_GIL_NOT_USED},
#endif
    {0, NULL}
};

static struct PyModuleDef modern_module = {
    PyModuleDef_HEAD_INIT,
    .m_name = "modern",
    .m_size = 0,
    .m_slots = slots,
};

PyMODINIT_FUNC
PyInit_modern(void)
{
    return PyModuleDef_Init(&modern_module);
}
