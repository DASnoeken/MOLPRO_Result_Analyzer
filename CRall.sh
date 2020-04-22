#!/bin/bash
if [ ! -f ./ls.ls ]; then
  ls *.out > ls.ls
fi
mkdir -p deser
if [ ! -f ./main.class ]; then
  echo "Main program not compiled yet."
  echo "Compiling..."
  Cfin.sh
  echo "Done!"
fi
java main H2 1 2
cd deser
if [ -f ./deser.class ]; then
  Rdes.sh
else
  echo "deser.java not compiled yet."
  echo "Compiling..."
  Cdes.sh
  echo "Done!"
  Rdes.sh
fi
echo "All done!"
