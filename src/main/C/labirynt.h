#ifndef LABIRYNT_GENERACJA_H
#define LABIRYNT_GENERACJA_H
#define START 1
#define STOP 2
typedef struct
{
    int status; // -1 - bariera, 0 - nieodwiedzony, 1 - odwiedzony przez funkcje generującą, 2 - odwiedzony przez solver(), 3 - nalezy do sciezki, 4 - jest na liscie(prim)
    int rodzaj; //1 - start, 2 - stop
    int numer;
    float gora, dol, prawo, lewo; //wagi przejsc (0 to brak przejscia czyli sciana)
    int x, y; //wspolrzedne komorki
    int numernaliscie;
}komorka;

typedef struct
{
    komorka** elementy;
    int rozmiar;
}lista;

typedef struct
{
    komorka** komorki;
    komorka *start, *stop;
    lista lista;
    int x, y; //wymiary
}labirynt;

typedef struct droga
{
    komorka* step;
    struct droga* next;
    float waga;
}droga;

labirynt tworzl(int x, int y); //tworzy labirynt bez przejsc o wymiarach x i y

int* losuj(int seed); //losowy kierunek przy generacji przejsc

void usunzlisty(lista*, komorka*);
void dodajdolisty(lista*, komorka*);
#endif //LABIRYNT_GENERACJA_H
