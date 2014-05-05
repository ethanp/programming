#!/usr/bin/env bash

# 5/4/14
# afm -> values

inDir=~/Downloads/afm\ 2
cd "$inDir" # need quotes bc of the space

outDir=~/Downloads/afm_k
mkdir -p $outDir

ls | while read f; do
    grep "^KPX" "$f" > $outDir/"$f"
done
