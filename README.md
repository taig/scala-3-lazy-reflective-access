# Scala 3 reflective access in lazy val

```
$> sbt nativeImageRunAgent
%> curl http://localhost:8080/joke
$> CTRL+C
# Inspect "dirty" ./target/native-image-configs/reflect-config.json
```

```
# Change scalaVersion in build.sbt to 2.13.7
$> sbt nativeImageRunAgent
%> curl http://localhost:8080/joke
# Ignore "Unsupported reflection method: methodTypeDescriptor" errors
$> CTRL+C
# Inspect "clean" ./target/native-image-configs/reflect-config.json
```