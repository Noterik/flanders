#!/bin/sh

echo "idt_raw : "$1
echo "Media file : "$2
echo "Metadata file : "$3

"idt_raw "$2" -metadata > "$3"

exit 0