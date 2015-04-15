#!/bin/sh

echo "Ffprobe : "$1
echo "Media file : "$2
echo "Metadata file : "$3

"$1"/ffprobe  -show_streams -i "$2" > "$3"

exit 0