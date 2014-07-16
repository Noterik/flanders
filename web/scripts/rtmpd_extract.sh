#!/bin/sh

echo "Rtmpdump : "$1
echo "Media stream : "$2
echo "Media file : "$3
echo "Metadata file : "$4

echo $1"/rtmpdump -r "$2" -m 15 -y mp4:"$3" -o /dev/null -A 0 -B 1 2>"$4
"$1"/rtmpdump -r $2 -m 15 -y mp4:$3 -o /dev/null -A 0 -B 1 2>"$4"

exit 0