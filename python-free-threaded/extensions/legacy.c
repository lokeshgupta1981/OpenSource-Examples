#define PY_SSIZE_T_CLEAN
#include <Python.h>

static PyModuleDef_Slot slots[] = {
    {0, NULL}
};

static struct PyModuleDef legacy_module = {
    PyModuleDef_HEAD_INIT,
    .m_name = "legacy",
    .m_size = 0,
    .m_slots = slots,
};

PyMODINIT_FUNC
PyInit_legacy(void)
{
    return PyModuleDef_Init(&legacy_module);
}
