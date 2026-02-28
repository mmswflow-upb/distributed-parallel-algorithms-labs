"""
files_demo.py
------------
Small demo of file I/O basics shown in the slides:
- open modes: r/w/a
- read/write
- tell/seek
"""

from pathlib import Path

DEMO_FILE = Path("demo_file.txt")


def demo_write_read():
    print("== demo_write_read ==")
    with open(DEMO_FILE, "w", encoding="utf-8") as f:
        f.write("Hello from Lab 6!\n")
        f.write("Second line.\n")

    with open(DEMO_FILE, "r", encoding="utf-8") as f:
        contents = f.read()
        print(contents.rstrip("\n"))


def demo_tell_seek_binary():
    """
    Mirrors the 'rb+' example in the slides (seek/tell in bytes).
    """
    print("\n== demo_tell_seek_binary ==")
    bin_file = Path("workfile.bin")
    with open(bin_file, "wb") as f:
        f.write(b"0123456789abcdef")  # 16 bytes

    with open(bin_file, "rb+") as f:
        print("Initial tell:", f.tell())
        f.seek(5)  # 6th byte
        print("After seek(5), tell:", f.tell())
        b = f.read(1)
        print("Read 1 byte:", b)

        f.seek(-3, 2)  # 3 bytes before the end
        print("After seek(-3, 2), tell:", f.tell())
        b = f.read(1)
        print("Read 1 byte:", b)


if __name__ == "__main__":
    demo_write_read()
    demo_tell_seek_binary()
