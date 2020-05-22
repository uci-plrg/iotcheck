#!/bin/bash

# Device conflict
#python3 ModelCheck.py ../jpf-core/ ../logs/valves/ ../smartapps/ appLists/device-interaction/valvesAppList appLists/device-interaction/valvesAppList2
#python3 ModelCheck.py ../jpf-core/ ../logs/switches/ ../smartapps/ appLists/device-interaction/switchesAppList appLists/device-interaction/switchesAppList2
#python3 ModelCheck.py ../jpf-core/ ../logs/lightSwitches/ ../smartapps/ appLists/device-interaction/lightSwitchesAppList appLists/device-interaction/lightSwitchesAppList2
#python3 ModelCheck.py ../jpf-core/ ../logs/dimmers/ ../smartapps/ appLists/device-interaction/dimmersAppList appLists/device-interaction/dimmersAppList2
#python3 ModelCheck.py ../jpf-core/ ../logs/acfanheaterSwitches/ ../smartapps/ appLists/device-interaction/acfanheaterSwitchesAppList appLists/device-interaction/acfanheaterSwitchesAppList2
#python3 ModelCheck.py ../jpf-core/ ../logs/cameraSwitches/ ../smartapps/ appLists/device-interaction/cameraSwitchesAppList appLists/device-interaction/cameraSwitchesAppList2
#python3 ModelCheck.py ../jpf-core/ ../logs/ventfanSwitches/ ../smartapps/ appLists/device-interaction/ventfanSwitchesAppList appLists/device-interaction/ventfanSwitchesAppList2
#python3 ModelCheck.py ../jpf-core/ ../logs/speeches/ ../smartapps/ appLists/device-interaction/speechesAppList appLists/device-interaction/speechesAppList2
#python3 ModelCheck.py ../jpf-core/ ../logs/nonHueLights/ ../smartapps/ appLists/device-interaction/nonHueLightsAppList appLists/device-interaction/nonHueLightsAppList2
#python3 ModelCheck.py ../jpf-core/ ../logs/hueLights/ ../smartapps/ appLists/device-interaction/hueLightsAppList appLists/device-interaction/hueLightsAppList2
#python3 ModelCheck.py ../jpf-core/ ../logs/thermostats/ ../smartapps/ appLists/device-interaction/thermostatsAppList appLists/device-interaction/thermostatsAppList2
#python3 ModelCheck.py ../jpf-core/ ../logs/alarms/ ../smartapps/ appLists/device-interaction/alarmsAppList appLists/device-interaction/alarmsAppList2
#python3 ModelCheck.py ../jpf-core/ ../logs/locks/ ../smartapps/ appLists/device-interaction/locksAppList appLists/device-interaction/locksAppList2
#python3 ModelCheck.py ../jpf-core/ ../logs/musicPlayers/ ../smartapps/ appLists/device-interaction/musicPlayersAppList
#python3 ModelCheck.py ../jpf-core/ ../logs/relaySwitches/ ../smartapps/ appLists/device-interaction/relaySwitchesAppList appLists/device-interaction/relaySwitchesAppList2
#python3 ModelCheck.py ../jpf-core/ ../logs/cameras/ ../smartapps/ appLists/device-interaction/camerasAppList appLists/device-interaction/camerasAppList2

# Physical conflict
#python3 ModelCheck.py ../jpf-core/ ../logs/ ../smartapps/ appLists/physical-interaction/soundsensorAppList appLists/physical-interaction/soundAppList
#python3 ModelCheck.py ../jpf-core/ ../logs/ ../smartapps/ appLists/physical-interaction/motionsensorAppList appLists/physical-interaction/motionAppList
#python3 ModelCheck.py ../jpf-core/ ../logs/ ../smartapps/ appLists/physical-interaction/illuminancesensorAppList appLists/physical-interaction/lightAppList
#python3 ModelCheck.py ../jpf-core/ ../logs/ ../smartapps/ appLists/physical-interaction/watersensorAppList appLists/physical-interaction/watervalveAppList

# Global variable conflict
#python3 ModelCheck.py ../jpf-core/ ../logs/globalStateVariables/ ../smartapps/ appLists/global-state-variable-interaction/globalstatevariableAppList appLists/global-state-variable-interaction/globalstatevariableAppList2

# Example runs - conflicts
#python3 ModelCheck.py ../jpf-core/ ../logs/exampleConflicts/ ../smartapps/ appLists/examples/conflictAppList appLists/examples/conflictAppList2

# Example runs - non-conflicts
python3 ModelCheck.py ../jpf-core/ ../logs/exampleNonConflicts/ ../smartapps/ appLists/examples/nonConflictAppList appLists/examples/nonConflictAppList2

# Single apps
#python3 ModelCheck.py ../jpf-core/ ../logs/singleApps/ ../smartapps/ appLists/single/singleAppList3 appLists/single/singleAppList2
#python3 ModelCheck.py ../jpf-core/ ../logs/singleApps/ ../smartapps/ appLists/single/singleAppList appLists/single/singleAppList2
