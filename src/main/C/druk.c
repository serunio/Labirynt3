#include <stdio.h>
#include "druk.h"
#include "labirynt.h"


void saveToFile(labirynt lab, int x, int y, char* filename) {
    FILE* f = fopen(filename, "w");
    komorka** l = lab.komorki;
    for(int i = 1; i <= x; i++) {
        fprintf(f, l[1][i].rodzaj == START ? "XP" : l[1][i].rodzaj == STOP ? "XK" : "XX");
    }
    fprintf(f, "X\n");
    for(int j = 1; j <= y; j++) {
        fprintf(f, "X");
        for (int i = 1; i < x; i++)
        {
            fprintf(f," ");
            if (l[j][i].prawo >  0)
                fprintf(f," ");
            else if (l[j][i].prawo == 0)
                fprintf(f,"X");
        }
        fprintf(f, " X\n");

        if(j == y) break;
        fprintf(f, "X");
        for(int i = 1; i <= x; i++)
        {
            if(l[j][i].dol > 0) {
                fprintf(f," X");
            }
            else if(l[j][i].dol == 0)
                fprintf(f,"XX");
        }
        fprintf(f,"\n");
    }
    for(int i = 1; i <= x; i++) {
        fprintf(f, l[y][i].rodzaj == START ? "XP" : l[y][i].rodzaj == STOP ? "XK" : "XX");
    }
    fprintf(f, "X\n");
}


