"""Shared argument parser and data loader for the shelf example tool.

Kept eager and tiny on purpose, so argparse stays a plain import in
every variant and the comparison below is only about the other nine
modules.
"""
import argparse
import json


def build_parser():
    parser = argparse.ArgumentParser(prog="shelf")
    parser.add_argument("--version", action="version", version="shelf 1.0")
    sub = parser.add_subparsers(dest="command")
    sub.add_parser("list")
    sub.add_parser("stats")
    sub.add_parser("csv")
    sub.add_parser("xml")
    sub.add_parser("backup")
    p_fetch = sub.add_parser("fetch")
    p_fetch.add_argument("url", nargs="?", default="https://example.com")
    for name in ("list", "stats", "csv", "xml", "backup"):
        sub.choices[name].add_argument("--file", default="books.json")
    return parser


def load(path):
    with open(path, encoding="utf-8") as fh:
        return json.load(fh)
