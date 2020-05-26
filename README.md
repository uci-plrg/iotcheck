# IoTCheck
This is the repository for IoTCheck, a framework that model-checks smart home apps. The following instructions explain how to quickly get started with IoTCheck. For more information about IoTCheck components, please see the [Wiki page of this repository](https://github.com/iotuser22/iotcheck/wiki).

## Getting Started
One easy way to install IoTCheck is using our [Vagrant-packaged IoTCheck](https://github.com/iotuser22/iotcheck-vagrant). Nevertheless, if the necessary components are present on our system, we could also download IoTCheck directly from this repository and set it up correctly to run on our system (we developed IoTCheck on Ubuntu 16.04.6 LTS (Xenial Xerus). 
Please see [this script](https://github.com/iotuser22/iotcheck-vagrant/blob/master/bootstrap.sh) for the necessary components and [this script](https://github.com/iotuser22/iotcheck-vagrant/blob/master/data/setup.sh) for the necessary steps to correctly install IoTCheck after downloading it from this repository.

## Examples
We have prepared 2 example runs to quickly test that the IoTCheck installation is working perfectly and to understand the output of IoTCheck runs.

1. First, we need to get into the directory `iotcheck/smartthings-infrastructure`.

```
    $ cd iotcheck/smartthings-infrastructure/
```

2. Run the `iotcheck.sh` script for both non-conflict and conflict examples. Each example takes about 1-2 minutes to run and messages will be printed out on the screen.

```
    iotcheck/smartthings-infrastructure $ ./iotcheck.sh -e exampleNonConflicts
```
```
    iotcheck/smartthings-infrastructure $ ./iotcheck.sh -e exampleConflicts
```

3. The results of all IoTCheck runs are collected in `iotcheck/logs/`.

#### Non-Conflict Examples
The log files for the non-conflict example runs can be found in `iotcheck/logs/exampleNonConflicts`. There are two log files for two pairs: 

1. `good-night-house.groovy--nfc-tag-toggle.groovy.log` for the pair `good-night-house.groovy` and `nfc-tag-toggle.groovy`, and 
2. `good-night-house.groovy--single-button-controller.groovy.log` for the pair `good-night-house.groovy` and `single-button-controller.groovy`.

If we open one of the log files, e.g., `good-night-house.groovy--nfc-tag-toggle.groovy.log`, we can see the reported result printed as the following.

```
JavaPathfinder core system v8.0 (rev 61f7af9209cd126105042a7b12ace7e7eb3f5492) - (C) 2005-2014 
United States Government. All rights reserved.


====================================================== system under test
main.main()

====================================================== search started: 5/26/20 3:54 PM

====================================================== results
no errors detected

====================================================== statistics
elapsed time:       00:00:10
states:             new=13,visited=14,backtracked=27,end=0
search:             maxDepth=5,constraints=0
choice generators:  thread=1 (signal=0,lock=1,sharedRef=0,threadApi=0,reschedule=0), data=13
heap:               new=589608,released=444025,maxLive=77394,gcCycles=27
instructions:       47693786
max memory:         1213MB
loaded code:        classes=786,methods=15362

====================================================== search finished: 5/26/20 3:54 PM
```

Both log files will show the line `no errors detected`, because there no conflicts were found between the two apps in each pair.

#### Conflict Examples
The log files for the conflict example runs can be found in `iotcheck/logs/exampleConflicts`. There are two log files for two pairs: 

1. `enhanced-auto-lock-door.groovy--nfc-tag-toggle.groovy.log` for the pair `enhanced-auto-lock-door.groovy` and `nfc-tag-toggle.groovy`, and 
2. `enhanced-auto-lock-door.groovy--single-button-controller.groovy.log` for the pair `enhanced-auto-lock-door.groovy` and `single-button-controller.groovy`.

If we open one of the log files, e.g., `enhanced-auto-lock-door.groovy--nfc-tag-toggle.groovy.log`, we can see the reported result printed as the following.

```
JavaPathfinder core system v8.0 (rev 61f7af9209cd126105042a7b12ace7e7eb3f5492) - (C) 2005-2014 
United States Government. All rights reserved.


====================================================== system under test
main.main()

====================================================== search started: 5/26/20 3:56 PM

====================================================== error 1
gov.nasa.jpf.vm.NoUncaughtExceptionsProperty
java.lang.RuntimeException: Conflict found between the two apps. App1 has written the value: "locked" to 
the attribute: currentLock while App2 is writing the value: "unlocked" to the same variable!
        
        ...

====================================================== results
error #1: gov.nasa.jpf.vm.NoUncaughtExceptionsProperty "java.lang.RuntimeException: Conflict found between..."

====================================================== statistics
elapsed time:       00:00:07
states:             new=5,visited=2,backtracked=6,end=0
search:             maxDepth=3,constraints=0
choice generators:  thread=1 (signal=0,lock=1,sharedRef=0,threadApi=0,reschedule=0), data=2
heap:               new=497975,released=365064,maxLive=79104,gcCycles=6
instructions:       35487521
max memory:         1199MB
loaded code:        classes=796,methods=14914

====================================================== search finished: 5/26/20 3:57 PM
```

This log file shows the line

```
gov.nasa.jpf.vm.NoUncaughtExceptionsProperty
java.lang.RuntimeException: Conflict found between the two apps. App1 has written the value: "locked" to 
the attribute: currentLock while App2 is writing the value: "unlocked" to the same variable!
```

which means that a conflict has been found between the two apps: `enhanced-auto-lock-door.groovy` is `App1` and `nfc-tag-toggle.groovy` is `App1`. `App1` wrote the value `locked` to the shared lock state variable `currentLock`, and `App2` is found to subsequently write to the same variable with the value `unlocked`. Since one attempts to lock and the other unlock the door, IoTCheck reports that these two apps have a conflict.

## Experiments
