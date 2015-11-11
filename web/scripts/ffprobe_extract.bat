@ECHO OFF


ECHO Ffprobe: %1

ECHO Video file: %2

ECHO MetaData file: %3


set ffprobePath=%1
set inputFile=%2
set outputFile=%3


echo "%ffprobePath%\ffprobe.exe -show_streams -show_format %inputFile% > %outputFile%"
%ffprobePath%\ffprobe.exe -show_streams -show_format %inputFile% > %outputFile%