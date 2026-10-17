import sys
import threading


class Playlist:
    def __init__(self):
        self._songs = []
        self._lock = threading.RLock()

    def add(self, song):
        with self._lock:
            if song not in self._songs:
                self._songs.append(song)

    def add_many(self, songs):
        with self._lock:          # holds the lock, then add() takes it again
            for song in songs:
                self.add(song)


playlist = Playlist()
ranges = [range(0, 4), range(3, 7), range(6, 10), range(9, 13)]
threads = [threading.Thread(target=playlist.add_many, args=(list(r),)) for r in ranges]

print("GIL enabled:", sys._is_gil_enabled())

for t in threads:
    t.start()
for t in threads:
    t.join()

print("Songs:", sorted(playlist._songs))
print("Count:", len(playlist._songs))
