
## Monument Simulator

To build:

```
javac --release 8 ./monumentsimulator/*.java ./monumentsimulator/tile/*.java
```

To run:

```
java monumentsimulator.MonumentSimulator
```

To package:

```
jar cmf ./manifest.mf ./MonumentSimulator.jar ./monumentsimulator/*.class ./monumentsimulator/tile/*.class
```


