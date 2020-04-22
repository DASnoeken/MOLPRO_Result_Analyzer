#!/bin/bash
if [ ! -f ./addressPotMap.ser ]; then
  echo "ERROR! Serialized potential map not found!"
  kill -SIGINT $$
fi
if [ ! -f ./addressKey.ser ]; then
  echo "ERROR! Serialized keys not found!"
  kill -SIGINT $$
fi
if [ ! -f ./addressGeoms.ser ]; then
  echo "ERROR! Serialized geometries not found!"
  kill -SIGINT $$
fi
if [ ! -f ./pesGUI.class ]; then
   echo "ERROR! pesGUI.class file not found!"
   echo "Recompiling"
   ./Cdes.sh
   ./Rdes.sh
   kill -SIGINT $$
fi
if [ ! -f ./deser.class ]; then
   echo "ERROR! deser.class file not found!"
   echo "Recompiling"
   ./Cdes.sh
   ./Rdes.sh
   kill -SIGINT $$
fi

java deser
