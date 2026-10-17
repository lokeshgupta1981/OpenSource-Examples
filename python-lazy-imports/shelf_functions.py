"""Book catalog CLI, function-local import version. Each subcommand
imports the modules it needs, inside its own function.
"""
import sys

from shelf_common import build_parser, load


def cmd_list(args):
    for book in load(args.file):
        print(book["title"])


def cmd_stats(args):
    import statistics
    pages = [book["pages"] for book in load(args.file)]
    print(f"average pages: {statistics.mean(pages):.1f}")


def cmd_csv(args):
    import csv
    writer = csv.writer(sys.stdout)
    for book in load(args.file):
        writer.writerow([book["title"], book["author"], book["pages"]])


def cmd_xml(args):
    import xml.etree.ElementTree as ET
    root = ET.Element("books")
    for book in load(args.file):
        ET.SubElement(root, "book", title=book["title"])
    print(ET.tostring(root, encoding="unicode"))


def cmd_backup(args):
    import tarfile
    import zipfile
    from pathlib import Path
    out = Path("backup.zip")
    with zipfile.ZipFile(out, "w") as zf:
        zf.write(args.file)
    with tarfile.open("backup.tar.gz", "w:gz") as tf:
        tf.add(args.file)
    print(f"wrote {out} and backup.tar.gz")


def cmd_fetch(args):
    import urllib.request
    with urllib.request.urlopen(args.url, timeout=5) as resp:
        print(resp.status)


COMMANDS = {
    "list": cmd_list, "stats": cmd_stats, "csv": cmd_csv,
    "xml": cmd_xml, "backup": cmd_backup, "fetch": cmd_fetch,
}

if __name__ == "__main__":
    args = build_parser().parse_args()
    COMMANDS[args.command](args)
