#!/bin/bash
ls *.out > ls.ls

if [ ! -s ls.ls ]; then
  echo "WARNING! No .out files found! Quitting compilation!"
  kill -SIGINT $$
fi

mkdir -p deser
javac mpErrException.java
javac findE.java
javac main.java
