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
if [ ! -f ./pesGUI.java ]; then
   echo "ERROR! pesGUI.java file not found!"
   kill -SIGINT $$
fi
if [ ! -f ./deser.java ]; then
   echo "ERROR! deser.java file not found!"
   kill -SIGINT $$
fi
if [ ! -f ./okListener.java ]; then
   echo "ERROR! okListener.java file not found!"
   kill -SIGINT $$
fi

javac derListener.java
javac pesder.java
javac helpListener.java
javac okListener.java
javac plot.java
javac pesGUI.java
javac deser.java
