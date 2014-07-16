@ECHO OFF


ECHO Rtmpdump: %1
ECHO Video stream: %2
ECHO Video file: %3
ECHO MetaData file: %4


set rtmpdumpPath=%1
set inputStream=%2
set inputFile=%3
set outputFile=%4


echo "%rtmpdumpPath%\rtmpdump.exe -r %inputStream% -m 15 -y mp4:%inputFile% -o NUL -A 0 -B 1 2>%outputFile%"
%rtmpdumpPath%\rtmpdump.exe -r %inputStream% -m 15 -y mp4:%inputFile% -o NUL -A 0 -B 1 2>%outputFile%