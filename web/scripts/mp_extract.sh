#!/bin/sh

echo "Mplayer : "$1
echo "Media file : "$2
echo "Metadata file : "$3

"$1"/mplayer -vo null -ao null -frames 1 -identify "$2" | grep ID_ > "$3"

exit 0