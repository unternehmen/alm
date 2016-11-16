#!/bin/sh

rm -i *_big.png

for f in *.png
do
    convert "$f" -scale 256x256 "${f%%.png}_big.png"
done
