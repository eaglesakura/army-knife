fastlane documentation
================
# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```
xcode-select --install
```

Install _fastlane_ using
```
[sudo] gem install fastlane -NV
```
or alternatively using `brew cask install fastlane`

# Available Actions
## project
### project doctor
```
fastlane project doctor
```

### project docker_pull
```
fastlane project docker_pull
```

### project docker_push
```
fastlane project docker_push
```

### project docker_build
```
fastlane project docker_build
```


----

## Android
### android clean
```
fastlane android clean
```

### android test
```
fastlane android test
```

### android test_lowmemory
```
fastlane android test_lowmemory
```

### android assemble
```
fastlane android assemble
```

### android assemble_lowmemory
```
fastlane android assemble_lowmemory
```


----

## ci
### ci setup
```
fastlane ci setup
```

### ci deploy
```
fastlane ci deploy
```


----

This README.md is auto-generated and will be re-generated every time [fastlane](https://fastlane.tools) is run.
More information about fastlane can be found on [fastlane.tools](https://fastlane.tools).
The documentation of fastlane can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
