#!/usr/bin/python

import itertools
import sys
import os
import glob

# Input parameters:
# - JPF directory
# - JPF logs directory
# - app directory
# - list #1
# - list #2 (if needed)

# Index 0 is always for the Python script itself
jpfLogDir = sys.argv[1]
csvFileOutput = sys.argv[2]
addLogFile = sys.argv[3]

# Extract the status finished/unfinished
def getStatus(text):
	if (text.find('no errors') != -1):
		return 'finished'
	else:
		return 'unfinished'
		
# Extract the states
def getStates(text):
	if not text:
		return '0'
	startStatesInfo = text.find('states:')
	startIndex = text.find('=', startStatesInfo)
	endIndex = text.find(',', startIndex)
	return text[startIndex+1:endIndex]
	
# Extract number of conflicts
def getConflicts(text, startIndex):
	conflictIndex = text.find('conflicts   :', startIndex)
	newLineIndex = text.index('\n', conflictIndex)
	startIndex = conflictIndex + 10	# Assuming that it will be greater than 10
	if conflictIndex == -1:
		returnedText = '0'
		startIndex = len(text)
	else:
		returnedText = text[conflictIndex+14:newLineIndex]
	return startIndex, returnedText

print("Opening log files ...\n\n")
out = open(csvFileOutput, "w+")
with open(addLogFile, 'r') as addFile:
	addText = addFile.read()
	startIndex = 0
	for filename in glob.glob(os.path.join(jpfLogDir, '*.log')):
		with open(filename, 'r') as f:
			text = f.read()
			status = getStatus(text) # Getting status "no errors/finished" or "not finished"
			states = getStates(text)
			startIndex, conflicts = getConflicts(addText, startIndex)
			lastSlash = filename.rfind('/')
			lastDoubleDash = filename.rfind('--')
			fname = filename[lastSlash+1:lastDoubleDash]
			lineReport = fname + ',' + status + ',' + states + ',' + conflicts + '\n'
			out.write(lineReport)
			print(lineReport)
out.close()
		


