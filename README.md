Program do generowania, edytowania i rozwiązywania labiryntu

[Generator](https://github.com/serunio/Labirynt3/blob/master/app/generator.exe) jest napisany w C i używa algorytmu Wilsona (loop-erased random walk).\
Może on działać jak samodzielny program, jako argumenty przyjmuje: wymiar x,  wymiar y, nazwę pliku wyjściowego.\
Brak argumentów oznacza losowe wymiary.

Resztę projektu napisano w Javie z użyciem biblioteki Swing. \
Do rozwiązywania labiryntu używany jest algorytm A*.\
Możliwe jest zapisanie labiryntu do formy tekstowej (ściana to X, przejście to spacja)\
lub binarnej opisanej w pliku [opis pliku binarnego.pdf](https://github.com/serunio/Labirynt3/blob/master/opis%20pliku%20binarnego.pdf) (pierwsze dwie sekcje)

Aby cały program działał poprawnie wymagana jest konfiguracja z folderu [App](https://github.com/serunio/Labirynt3/tree/master/app) (generator musi mieć nazwę "generator.exe", folder na labirynty musi mieć nazwę "files")

