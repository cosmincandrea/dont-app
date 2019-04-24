package com.example.cosmin.dont;


import android.content.Intent;

import processing.core.*;

import java.util.Arrays;

import static android.text.TextUtils.split;
import static java.sql.DriverManager.println;

public class skBun extends PApplet {

    String[] seed;
    int offset = 45;
    String[] elemente;
    String[][] elementee;
    String prod;
    int[][] pereti;
    int plus = 0;
    int lungime = -1, inaltime = -1;
    String[] d;
    String intrare;
    int n = 0;
    boolean gataDrumuri = false;
    float inPlus;
    int intrY, intrX;

    punct[][] grid;
    punct start, goal;
    punct[] openSet, closedSet;
    punct[] path;
    punct current;
    String[] coord;

    class punct {
        int x, y;
        public float f = 0;
        public float g = 0;
        public float h = 0;
        public punct[] vecini = new punct[4];
        public int lVecini = 0;
        public punct prev = null;

        punct(int _x, int _y) {
            x = _x; y = _y;
        }

        public void addVecini(punct[][] grid) {
            int a = x; int b = y;
            if (a < lungime - 1 && pereti[a][b] != 1) vecini[lVecini++] = grid[a+1][b];
            if (a > 0 && pereti[a][b] != 1) vecini[lVecini++] = grid[a-1][b];
            if (b < inaltime - 1 && pereti[a][b] != 1) vecini[lVecini++] = grid[a][b+1];
            if (b > 0 && pereti[a][b] != 1) vecini[lVecini++] = grid[a][b-1];
        }
    }

    public void settings() {
        elemente = MarcActivity.elemente;
        seed = MarcActivity.seed;
        coord = MarcActivity.coord;

        lungime = PApplet.parseInt(seed[0]);
        inaltime = PApplet.parseInt(seed[1]);
        intrare = seed[2];
        elementee = new String[lungime][inaltime];
        for (String[] rand : elementee)
            Arrays.fill(rand, "");
        pereti = new int[lungime+1][inaltime+1];
        for (int[] rand : pereti)
            Arrays.fill(rand, 0);
        if (lungime > -1 && inaltime > -1) {
            fullScreen();
            offset = displayWidth/lungime;
            while (inaltime * offset > displayHeight) {
                offset -= 0.1;
            }
        }
        inPlus = (displayHeight - offset*inaltime) / 2;
        grid = new punct[lungime][inaltime];
        openSet = new punct[lungime*inaltime];
        closedSet = new punct[lungime*inaltime];
    }

    public void setup() {
        println("------------------------------------- Start -------------------------------------");
        seed = subset(seed, 3);
        for (int i = 0; i < elemente.length; i++) {
            String rez = citireElem(elemente[i]);
            String[] rez2 = split(rez, ".");
            elementee[PApplet.parseInt(rez2[0])][PApplet.parseInt(rez2[1])] = rez2[2];
        }


        for (int i = 0; i < seed.length; i++) {
            String[] d = split(seed[i], ' ');
            int x = PApplet.parseInt(d[0]);
            int y = PApplet.parseInt(d[1]);
            pereti[x][y] = 1;

        }

        for (int i = 0; i < lungime; i++) {
            for (int j = 0; j < inaltime; j++) {
                grid[i][j] = new punct(i, j);
            }
        }
        for (int i = 0; i < lungime; i++) {
            for (int j = 0; j < inaltime; j++) {
                grid[i][j].addVecini(grid);
            }    current = grid[0][0];

        }



        String ss[];
        ss = split(intrare, ' ');
        current = grid[PApplet.parseInt(ss[0])][PApplet.parseInt(ss[1])];
        intrX = PApplet.parseInt(ss[0]); intrY = PApplet.parseInt(ss[1]);

        int nn = MarcActivity.lCoord;
        for (int i = 0; i < nn-1; i++)
            for (int j = 0; j < nn-i-1; j++) {
                float d1 = di(current, punctLa(coord[j]));
                float d2 = di(current, punctLa(coord[j+1]));
                if (d1 > d2) {
                    String temp = coord[j];
                    coord[j] = coord[j+1];
                    coord[j+1] = temp;
                }
            }

        desen();
    }

    public float heur(punct a, punct b) {
        float d = abs(a.x-b.x) + abs(a.y-b.y);
        return d;
    }

    public void path(punct start, punct goal) {
        boolean gata = false;
        grid = new punct[lungime][inaltime];
        openSet = new punct[lungime*inaltime];
        closedSet = new punct[lungime*inaltime+1];

        int n = 0; int nc = 0;

        for (int i = 0; i < lungime; i++) {
            for (int j = 0; j < inaltime; j++) {
                grid[i][j] = new punct(i, j);
            }
        }

        for (int i = 0; i < lungime; i++) {
            for (int j = 0; j < inaltime; j++) {
                grid[i][j].addVecini(grid);
            }
        }
        openSet[n++] = start;

        while (n > 0) {
            int winner = 0;
            for (int i = 0; i < n; i++) {
                if (openSet[i].f < openSet[winner].f) {
                    winner = i;
                }
            }

            punct current = openSet[winner];
            if (current.equals(goal)) {

                path = new punct[lungime*inaltime];
                int np = 0;
                punct tmp = current;
                path[np++] = tmp;
                while (tmp.prev != null) {
                    path[np++] = tmp.prev;
                    tmp = tmp.prev;
                }
                fill(255, 252, 234, 200);
                for (int i = np - 1; i >= 0; i--) {
                    rect(path[i].x * offset + offset / 4, path[i].y * offset + offset / 4 + inPlus, offset / 2, offset / 2);
                }
                //println("gata!!!!");
                gata = true;
                break;
            }

            int index = -1;
            for (int i = 0; i < n; i++) {
                if (openSet[i].equals(current)) {
                    index = i;
                    break;
                }
            }
            for (int i = index; i < n; i++) {
                openSet[i] = openSet[i+1];
            }
            openSet[n] = null;

            n--;
            closedSet[nc++] = current;

            punct[] vecini = current.vecini;
            for (int i = 0; i < current.lVecini; i++) {
                punct vecin = vecini[i];
                boolean b = false;
                for (int j = 0; j < nc; j++)
                    if (closedSet[j].equals(vecin)) b = true;
                if (!b) {
                    float tmpG = current.g + 1;

                    boolean bb = false;
                    for (int jj = 0; jj < n; jj++) {
                        if (openSet[jj].equals(vecin)) bb = true;
                    }

                    if (!bb) {
                        openSet[n++] = vecin;
                    } else if (tmpG >= vecin.g) {
                        continue;
                    }
                    vecin.g = tmpG;
                    vecin.h = heur(vecin, goal);
                    vecin.f = vecin.g + vecin.h;
                    vecin.prev = current;
                }

            }
        }
        //if (gata) println("nu :(");


    }

    public boolean valid(int X, int Y) {
        return (X <= lungime*offset && X >= 0 && Y < inaltime*offset && Y >= 0);
    }

    public void draw() {
        fill(167, 244, 66);
        noStroke();
        rect(intrX * offset, intrY*offset + inPlus, offset, offset);
    }

    public String citireElem(String elem) {
        String[] el;
        el = split(elem, ' ');
        String[] nume = split(el[1], ':');
        return el[0] + '.' + nume[0] + '.' + nume[1];
    }

    @Override
    public void ambient(int rgb) {
        super.ambient(rgb);
    }

    public void desen() {
        background(71);
        stroke(255);
        strokeWeight(1);
        rectMode(CORNERS);
        rect(0, offset*inaltime-2 + inPlus, width, offset*inaltime-2 + inPlus);
        for (int i = 0; i < seed.length; i++) {
            String[] d = split(seed[i], ' ');
            int x = PApplet.parseInt(d[0]);
            int y = PApplet.parseInt(d[1]);
            if (pereti[x][y] == 1) {
                rectMode(CORNER);
                fill(73, 86, 123);
                noStroke();
                x*=offset;
                y*=offset;
                rect(x, y + inPlus, offset, offset);
            }
        }
    }

    public punct punctLa(String coord) {
        String[] d;
        d = split(coord, ' ');
        punct e;
        e = new punct(parseInt(d[0]), parseInt(d[1]));
        return e;
    }

    public float di(punct a, punct b) {
        return dist(a.x, a.y, b.x, b.y);
    }

    public void drumLa(String[] coord, int n) {
        if (n < MarcActivity.lCoord - 1) {
            String[] d;

            d = split(coord[n], ' ');
            path(current, grid[PApplet.parseInt(d[0])][PApplet.parseInt(d[1])]);
            current = grid[PApplet.parseInt(d[0])][PApplet.parseInt(d[1])];
        } else if (n == MarcActivity.lCoord - 1) {
            String[] d;
            d = split(coord[n], ' ');

            path(current, grid[PApplet.parseInt(d[0])][PApplet.parseInt(d[1])]);
            gataDrumuri = true;
        }
    }

    public void mousePressed() {

//    println("MARCx", mouseX, displayWidth, lungime*offset, n);
//    if (mouseX >= lungime*offset/2) {
//      println("MARC1");
//      if (!gataDrumuri) {
//        desen();
//        drumLa(coord, n++);
//      }
//    } else if (mouseX < displayWidth){
//      println("MARC2");
//       if (n > 0) {
//         desen();
//         n--;
//         drumLa(coord, n);
//       } else desen();
//    }
        if (!gataDrumuri) {
            desen();
            drumLa(coord, n++);
        } else {

            getActivity().finish();
            //Intent intent = new Intent(getActivity(), ProductsList.class);
            //intent.putExtra("nume", MarcActivity.nume);
            //startActivity(intent);
        }
    }

}