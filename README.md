# AINA-2017

This project was created by Ales Omerzel for the purpose of the research paper:

Pournaras, E., Nikolic, J., Omerzel, A. and Helbing, D., 2017, March. [Engineering Democratization in Internet of Things Data Analytics](https://pdfs.semanticscholar.org/ce27/96de888eec3d4a4c6d661b6959b3653ee9bc.pdf). In 2017 IEEE 31st International Conference on Advanced Information Networking and Applications (AINA) (pp. 994-1003). IEEE.

## Dependencies
* Weka library:  https://weka.wikispaces.com
* GraphView-4.0.1: https://github.com/jjoe64/GraphView

**Note**
Dependencies are already part of the module and can be found under *AINA-2017/nervousnet/lib*.

## Installation

One can download this repo and import *AINA-2017/nervousnet* as module to get it into existing Android project. However, if you don't have a project yet, follow the instructions below. 

**Note**
Instructions are given using Android Studio 2.1.2 (build 143.2915827) on MacBook Pro Retina 2014.

1) Start Android Studio and create new project File -> New.
2) Insert any application name and click Next.
3) Select minimum SDK to *API 21: Android 6.0* and click Next. 
4) Select *Add No Activity* and click Finish. 
5) Click File -> New -> Import Module ...
6) Search for AINA-2017 repo that you cloned to your local machine and select root directory *AINA-2017/nervousnet* and click Finish.  
7) Connect your Android 6.0 (or later) phone with USB to your computer and make sure your phone is running in *developer mode* with permission for installation via USB.
8) Now click **run**. Project will start building.
9) Select your phone as deployment target when *Select Deployment Target* window pops up.
10) Be aware of any notification on your phone about installation. Allow access if needed.
11) Application will get installed on the phone and start automatically. It is ready for use.

## Usage

When application is installed and is successfully running, press *Start* button to start collecting sensor data. 

## Pull Data

All sensor data is stored in SQLite database. There are multiple ways to access this database. One way is by using the following command to transfer whole database to your working directory:

```bash
adb backup -f ./data.ab -noapk ch.ethz.coss.nervousnetgen
(echo -ne "\x1f\x8b\x08\x00\x00\x00\x00\x00" ; dd if=data.ab bs=1 skip=24) | gzip -dc - | tar -xvf -
```

#### How to Explore Fetched Database Data?

Our recommendation is [DB Browser for SQLite](https://sqlitebrowser.org/).

## Configuration

### Sensors

Application supports *light* and *accelerometer* sensor data collection. 

Sensors configuration can be found in *nervousnet/src/main/assets/sensors_configuration.json*. Light sensor configuration is the following: 

```Json
{
    "sensorName" : "Light_v2",
    "androidSensorType" : 5,
    "parametersNames" : [
      "Lux"
    ],
    "parametersTypes" : [
      "double"
    ],
    "metadata" : [
      "not supported yet"
    ],
    "androidParametersPositions" : [ 0 ],
    "samplingPeriod" : 500000
  }
```

Field *sensorName* holds name for sensor and can be anything. *androidSensorType* defines sensor type according to Android documentation. In this case value 5 represents *light sensor*. 

Then names for each sensor parameter and type are specified in *parametersNames* and *parametersTypes*, respectively. *androidParametersPositions* field is used to map and order values from the sensor output to the parameters specified in *parametersNames*. For example, accelerometer sensor outputs three values x, y and z axis and such configuration gives us freedom to order axis in the database in a way we want. 

One of the most important fields is **samplingPeriod** which is frequency of sensor data collection in microseconds.

---
**Note**
Re-install application when applying new configuration.
---

### Virtual Sensors

Application supports also virtual sensors. *Virtual sensor* is similar to a regular sensor and it can combine parameters of multiple sensors. In the configuration can be specified what are the parameters from each sensor and what is parameters' order.


```Json
{
  "sensors" : {
    "Accelerometer_v2" : [
      "accX",
      "accY",
      "accZ"
    ],
    "Light_v2" : [
      "Lux"
    ]
  },
  "samplingRate" : 60000,
  "slidingWindow" : 86400000,
  "virtualSensorName" : "VirtualSensorACCLight"
}
```

**Note**
In order to include sensor in a virtual sensor, it must be configured in *nervousnet/src/main/assets/sensors_configuration.json*.

`Accelerometer_v2` refers to the sensor in *nervousnet/src/main/assets/sensors_configuration.json* and `["accX", "accY", "accZ"]` are selected parameters to be included in a virtual sensor (in this case all of them). `Light_v2` is the second selected sensor for the same virtual sensor with its parameter `Lux`. 

`slidingWindow` is a time period which is used to aggregate all collected data in that period and compute a possible state. 

`samplingRate` is a frequency of moving sliding window in microseconds and each time window moves new possible state is computed. 

Possible state is a set of centroids computed by kMeans on collected data in a window.
