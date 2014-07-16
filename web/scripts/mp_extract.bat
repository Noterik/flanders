@ECHO OFF


ECHO Mplayer: %1

ECHO Video file: %2

ECHO MetaData file: %3


set mplayerPath=%1
set inputFile=%2
set outputFile=%3


echo "%mplayerPath%\mplayer.exe -vo null -frames 1 -identify %inputFile% > %outputFile%"
%mplayerPath%\mplayer.exe -vo null -frames 1 -identify %inputFile% > %outputFile%