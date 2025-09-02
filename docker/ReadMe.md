**Expected folder structure:**

```
root-folder
	└── ah5-common-java-spring/
	└── ah5-blacklist-java-spring/
```

# BUILD IMAGES

1) Update the version numbers in the system Dockerfile and entrypoint.sh.

2) Run the system build command from the _root-folder_: `docker build -f ./ah5-blacklist-java-spring/docker/Dockerfile-Blacklist -t arrowhead-blacklist:5.0.0 .`

3) Run the database build command from the _root-folder_: `docker build -f ./ah5-blacklist-java-spring/docker/Dockerfile-Blacklist-DB -t arrowhead-blacklist-db:5.0.0 .`