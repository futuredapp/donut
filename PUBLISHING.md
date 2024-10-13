# Publishing

Each new version should have entry in [CHANGELOG.md](./CHANGELOG.md) document.

## Snapshots

Snapshots are built, signed and published to Maven Central snapshot repo using [CI workflow](.github/workflows/publish_snapshot.yml) with each commit to the `master` branch.

Snapshots versions grouped based on major version of library and the snapshot version can be configured in project-level `gradle.properties` file:
```properties
VERSION_NAME=2.X.X-SNAPSHOT
```

## Releases

Releases are built, signed and published to Maven Central using [CI workflow](.github/workflows/publish_release.yml), after GitHub release is published.
The `VERSION_NAME` property is set to match the GitHub release name:
```shell
./gradlew publish -PVERSION_NAME=${{ github.event.release.name }}
```
