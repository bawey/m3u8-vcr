#!/bin/bash

# A crude proof of concept

HOST='ddh1.cdndac.lol'
ROOT="https://$HOST/ddh1/premium75/tracks-v1a1"
LIST='mono.m3u8'
ORIGIN='https://superwebplay.xyz/'

# For a brute-force channels scan, do:
#for i in {01..99}; do URL="${ROOT/75/$i}/$LIST"; echo $URL; curl $URL --output -; done


ALL_SEGMENTS=()
while :
do
  CHUNKS=''
  ((REPEATED_COUNT=0))
  for SEG in $(curl "$ROOT/$LIST" -H "Origin: $ORIGIN"  -H "Host: $HOST" -H "Referer: $ORIGIN" --output - | grep ".ts"); do
    if [[ ${ALL_SEGMENTS[@]} =~ $SEG ]]
    then
      echo "segment $SEG seen already"
      ((REPEATED_COUNT+=1))
    else
      CHUNK=$(curl -L -m 3 "$ROOT/$SEG" -H "Origin: $ORIGIN" -H "Referer: $ORIGIN" -H "Host: $HOST" --output - | base64)
      ALL_SEGMENTS+=("$SEG")
      CHUNKS="$CHUNKS$CHUNK"
    fi
  done
  #To watch each fetched piece as soon as it lands:
  #mpv <(echo "$CHUNKS" | base64 -d) &
  OUTPUT_FILE="/tmp/dumped-stream.ts"
  echo "$CHUNKS" | base64 -d >> $OUTPUT_FILE
  if [[ $(pgrep -fa $OUTPUT_FILE) =~ $OUTPUT_FILE ]]
  then
    echo "mpv is running already."
  else
    mpv $OUTPUT_FILE &
  fi
  echo "Will sleep for $REPEATED_COUNT seconds"
  sleep "$REPEATED_COUNT"
done
