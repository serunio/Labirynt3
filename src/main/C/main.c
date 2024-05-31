#include <stdlib.h>
#include <time.h>
#include <stdio.h>
#include <string.h>

#include "druk.h"
#include "wilson.h"
#include "labirynt.h"

int main(int argc, char** argv)
{
    if(argc < 4)
    {
        argv[1] = "0";
        argv[2] = "0";
        argv[3] = "maze";
    }
    int seed = (int)time(NULL);
    srand(seed);
    int x, y;
    x = atoi(argv[1]);  //wymiary
    y = atoi(argv[2]);  //labiryntu

    if (x < 2 || y < 2 || x > 100 || y > 100) {
        srand(time(NULL));
        x = rand() % 99 + 2; //losowe
        y = rand() % 99 + 2; //wymiary
    }
    char* suffix = malloc(20);
    char* name = malloc(100);
    strcpy(name, argv[3]);
    sprintf(suffix, "%dx%d.txt", x, y);
    strcat(name, suffix); //nazwa pliku
    labirynt lab = tworzl(x + 2, y + 2);
    generacja_wilson(&lab, seed);
    saveToFile(lab, x, y, name);

    for(int i = y+1; i >= 0; i--)
        free(lab.komorki[i]);
    free(lab.komorki);
    return 0;
}
