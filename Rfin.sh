#!/bin/bash
if [ ! -f ./ls.ls ]; then
  ls *.out > ls.ls
fi
mkdir -p deser
java main H2 1 2
