#include <stdlib.h>

#include "wilson.h"
#include "labirynt.h"

void generacja_wilson(labirynt* lab, int seed)
{
    srand(seed);
    lista* lista = &lab->lista;
    lista->elementy = malloc(lab->x * lab->y * sizeof(komorka*)); //lista nieodwiedzonych elementów
    lista->rozmiar = -1;
    for(int i = 1; i <= lab->y; i++)
    {
        for(int j = 1; j <= lab->x; j++)
        {
            dodajdolisty(lista, &lab->komorki[i][j]);
            lab->komorki[i][j].status = 0;
        }
    }
    komorka* k = lista->elementy[rand() % (lista->rozmiar + 1)];
    k->status = 1;
    usunzlisty(lista, k);
    int r;
    while((r = lista->rozmiar) >= 0)
    {
        seed = rand();
        //losuj komorke z listy
        komorka* n = lista->elementy[rand() % (r + 1)];
        n->status = 3;
        usunzlisty(lista, n);
        //idz losowo az znajdziesz odwiedzona komorke
        randomwalk(lab, n->x, n->y, seed);

        n->status = 1;
    }
    free(lista->elementy);
}

void randomwalk(labirynt* lab, int x, int y, int seed)
{
    srand(seed);

    droga* d = malloc(sizeof(*d));
    komorka* k = &lab->komorki[y][x]; //aktualna komórka
    komorka* n; //nastepna komórka
    d->step = k;
    int* i;
    int a = 1000;
    int b = a;
    while(k->status != 1)
    {

            a--;
            if(a == 0) {srand(seed); a = b--;} //odświeżenie rand zapobiegające zapętleniom
            seed = rand();

            //wybranie kierunku
            i = losuj(seed);
            n = &lab->komorki[k->y+i[0]][k->x+i[1]];

            //sprawdzenie czy to nie bariera, jak tak to wybranie jeszcze raz
            if(n->status == -1)
            {
                continue;
            }

            //sprawdzenie czy to pętla
            if(n->status == 3)
            {

                //jeżeli to pętla to cofamy sie po drodze az do tej komorki i jedziemy dalej
                while(d->step->numer != n->numer)
                {
                    d->step->status = 0;
                    d = d->next;
                }
                k = n;
            }
            else {
                //jeżeli to nie pętla to dodajemy komorke do drogi i jedziemy od niej dalej
                droga* new = malloc(sizeof * new);
                k = n;
                if (k->status != 1)
                    k->status = 3;
                new->step = k;
                new->next = d;
                d = new;
            }
    }
   while(d->step->numer != lab->komorki[y][x].numer)
   {
       float waga = (float)(rand()%1000 +1)/100;    //losowa waga z zakresu 1-10
       komorka* a = d->step;
       komorka* b = d->next->step;
       a->status = 1;

       usunzlisty(&lab->lista, a);
       i[0] = b->y - a->y;
       i[1] = b->x - a->x;
       d = d->next;
       if(i[1] == 1) {
           a->prawo = b->lewo = waga;
       } else
       if(i[1] == -1) {
           a->lewo = b->prawo = waga;
       } else
       if(i[0] == 1) {
           a->dol = b->gora = waga;
       } else
       if(i[0] == -1) {
           a->gora = b->dol = waga;
       }
   }
   free(d);
}