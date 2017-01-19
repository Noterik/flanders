#!/bin/sh

echo "dcraw : "$1
echo "Media file : "$2
echo "Metadata file : "$3

"dcraw -i -v "$2" > "$3"

exit 0