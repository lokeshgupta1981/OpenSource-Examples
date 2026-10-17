"""Book catalog CLI, PEP 810 'lazy import' version. Requires Python
3.15+. Same top-level imports as shelf_eager.py, with 'lazy' added to
every import a run might not need.
"""
import argparse
lazy import csv
lazy import json
lazy import statistics
import sys
lazy import tarfile
lazy import urllib.request
lazy import xml.etree.ElementTree as ET
lazy import zipfile
lazy from pathlib import Path

from shelf_common import build_parser, load


def cmd_list(args):
    for book in load(args.file):
        print(book["title"])


def cmd_stats(args):
    pages = [book["pages"] for book in load(args.file)]
    print(f"average pages: {statistics.mean(pages):.1f}")


def cmd_csv(args):
    writer = csv.writer(sys.stdout)
    for book in load(args.file):
        writer.writerow([book["title"], book["author"], book["pages"]])


def cmd_xml(args):
    root = ET.Element("books")
    for book in load(args.file):
        ET.SubElement(root, "book", title=book["title"])
    print(ET.tostring(root, encoding="unicode"))


def cmd_backup(args):
    out = Path("backup.zip")
    with zipfile.ZipFile(out, "w") as zf:
        zf.write(args.file)
    with tarfile.open("backup.tar.gz", "w:gz") as tf:
        tf.add(args.file)
    print(f"wrote {out} and backup.tar.gz")


def cmd_fetch(args):
    with urllib.request.urlopen(args.url, timeout=5) as resp:
        print(resp.status)


COMMANDS = {
    "list": cmd_list, "stats": cmd_stats, "csv": cmd_csv,
    "xml": cmd_xml, "backup": cmd_backup, "fetch": cmd_fetch,
}

if __name__ == "__main__":
    args = build_parser().parse_args()
    COMMANDS[args.command](args)
