# fwla-center

Central server for FWLA-Center system.

## [Changelog](./documentation/changelog/README.md)

## Build

```shell
./mvnw clean install
docker build -t ihrigb/fwla-center:latest -t ihrigb/fwla-center:<version> .
docker push ihrigb/fwla-center:latest
docker push ihrigb/fwla-center:<version>
```

## Email receiving and parsing

TBD

## Stored entities and their usage 

TBD

## Configuration Properties

TBD

## Links

### Related Projects

* [fwla-center-display](http://github.com/FWLA/fwla-center-display)
* [fwla-center-admin](http://github.com/FWLA/fwla-center-admin)
* [fwla-center-assembly](http://github.com/FWLA/fwla-center-assembly)
* [fwla-center-control](http://github.com/FWLA/fwla-center-control)
