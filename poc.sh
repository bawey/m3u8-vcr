#!/bin/bash

# A crude proof of concept

HOST='ddh1.cdndac.lol'
CHANNEL="${1:-75}"
ROOT="https://$HOST/ddh1/premium$CHANNEL/tracks-v1a1"
LIST='mono.m3u8'
ORIGIN='https://superwebplay.xyz/'
OUTPUT_FILE="/tmp/dumped-stream-$CHANNEL.ts"

# For a brute-force channels scan, do:
#for i in {01..99}
#do
#  URL="${ROOT/75/$i}/$LIST";
#  PLAYLIST=$(curl $URL -s --output -);
#  if [[ $PLAYLIST =~ ".ts" ]]
#  then
#     echo $URL;
#  fi
#done


ALL_SEGMENTS=()
while :
do
  CHUNKS=''
  ((REPEATED_COUNT=0))
  for SEG in $(curl --retry 3 "$ROOT/$LIST" -H "Origin: $ORIGIN"  -H "Host: $HOST" -H "Referer: $ORIGIN" --output - | grep ".ts"); do
    if [[ ${ALL_SEGMENTS[@]} =~ $SEG ]]
    then
      echo "segment $SEG seen already"
      ((REPEATED_COUNT+=1))
    else
      CHUNK=$(curl -L -m 5 --retry 3 --retry-all-errors "$ROOT/$SEG" -H "Origin: $ORIGIN" -H "Referer: $ORIGIN" -H "Host: $HOST" --output - | base64)
      ALL_SEGMENTS+=("$SEG")
      CHUNKS="$CHUNKS$CHUNK"
    fi
  done
  #To watch each fetched piece as soon as it lands:
  #mpv <(echo "$CHUNKS" | base64 -d) &

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
